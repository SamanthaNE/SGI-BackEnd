package pe.edu.pucp.tesisrest.worker.service.impl;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pe.edu.pucp.tesisrest.common.dto.ResearchGroupSciProdDetailDto;
import pe.edu.pucp.tesisrest.common.enums.ProjectAttributesEnums;
import pe.edu.pucp.tesisrest.common.enums.PublicationAttributesEnums;
import pe.edu.pucp.tesisrest.common.model.funding.Funding;
import pe.edu.pucp.tesisrest.common.model.logentry.LogEntry;
import pe.edu.pucp.tesisrest.common.model.perfomance.*;
import pe.edu.pucp.tesisrest.common.model.project.Project;
import pe.edu.pucp.tesisrest.common.model.project.ProjectEvaluation;
import pe.edu.pucp.tesisrest.common.model.project.ProjectFunded;
import pe.edu.pucp.tesisrest.common.model.publication.Publication;
import pe.edu.pucp.tesisrest.common.model.publication.PublicationEvaluation;
import pe.edu.pucp.tesisrest.common.model.scopus.ScopusPublication;
import pe.edu.pucp.tesisrest.common.repository.PublicationRepository;
import pe.edu.pucp.tesisrest.common.service.impl.CommonImpl;
import pe.edu.pucp.tesisrest.researcher.repository.FundingRepository;
import pe.edu.pucp.tesisrest.researcher.repository.ProjectFundedRepository;
import pe.edu.pucp.tesisrest.researcher.repository.ProjectRepository;
import pe.edu.pucp.tesisrest.researcher.repository.scopus.ScopusPublicationRepository;
import pe.edu.pucp.tesisrest.worker.repository.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class InferenceEngineServiceImpl {

    private final EntityManager entityManager;

    private final PublicationRepository publicationRepository;
    private final ScopusPublicationRepository scopusPublicationRepository;

    private final ProjectRepository projectRepository;

    private final QualificationRuleRepository qualificationRuleRepository;
    private final FactRangeRepository factRangeRepository;
    private final FactAttributeRepository factAttributeRepository;
    private final QualificationFactorRepository qualificationFactorRepository;
    private final ProjectFundedRepository projectFundedRepository;
    private final FundingRepository fundingRepository;
    private final PublicationEvaluationRepository publicationEvaluationRepository;
    private final ProjectEvaluationRepository projectEvaluationRepository;
    private final CommonImpl commonImpl;
    private final PerformanceEvaluationRepository performanceEvaluationRepository;
    private final OrgUnitEvaluationCategoryRangeRepository orgUnitEvaluationCategoryRangeRepository;
    private final OrgUnitCategoryScoreRepository orgUnitCategoryScoreRepository;
    private final PerformanceEvaluationSubcategoryRepository performanceEvaluationSubcategoryRepository;

    public Integer assignScoreToScientificProduction(LogEntry logEntry) {
        Integer status = -1;

        if(logEntry.getTableName().equals("publication")) {
            Publication publication = publicationRepository.findPublicationByIdPublication(logEntry.getRecordId());

            if(publication != null) {
                ScopusPublication scopusPublication = scopusPublicationRepository.findScopusPublicationByDoi(publication.getDOI());

                if(scopusPublication != null) {
                    status = processConditionsPublication(scopusPublication, logEntry.getRecordId());
                }
            }

        } else if(logEntry.getTableName().equals("project")) {
            Project project = projectRepository.findProjectByIdProject(String.valueOf(logEntry.getRecordId()));
            ProjectFunded projectFunded = projectFundedRepository.findProjectFundedByIdProject(project.getIdProject());

            Funding funding = new Funding();
            if(projectFunded != null) {
                funding = fundingRepository.findFundingByIdFunding(projectFunded.getId().getIdFunding());
            }

            status = processConditionsProject(project, funding, logEntry.getRecordId());
        }

        return status;
    }

    private Integer processConditionsPublication(ScopusPublication scopusPublication, Integer recordId) {
        BigDecimal score = BigDecimal.ZERO;

        int idRuleExecuted = -1;
        int idSubcategory = -1;

        List<QualificationRule> rules = qualificationRuleRepository.getAllByScientificTypeAndStatus("publication", 1);

        for(QualificationRule rule : rules) {
            List<FactAttribute> conditionsAttribute = factAttributeRepository.findAllByIdRuleAndIdQFactor(rule.getIdRule(), 0, "publication");
            //List<FactRange> conditionsRange = factRangeRepository.findAllByIdRuleAndIdQFactor(rule.getIdRule(), 0);

            if(conditionsAttribute != null && !conditionsAttribute.isEmpty()) {
                for(FactAttribute factAttribute : conditionsAttribute) {
                    if(factAttribute.getAttribute().equals(PublicationAttributesEnums.SUB_TYPE_DESCRIPTION.getAttribute())){
                        if(scopusPublication.getSubTypeDescription().equals(factAttribute.getAttributeValue())){
                            score = factAttribute.getScore();
                            idRuleExecuted = rule.getIdRule();
                            idSubcategory = rule.getIdSubcategory();
                            break;
                        }
                    } else if (factAttribute.getAttribute().equals(PublicationAttributesEnums.AGGREGATION_TYPE.getAttribute())){
                        if(scopusPublication.getAggregationType().equals(factAttribute.getAttributeValue())){
                            score = factAttribute.getScore();
                            idRuleExecuted = rule.getIdRule();
                            idSubcategory = rule.getIdSubcategory();
                            break;
                        }
                    }
                }
            }

            /*
            if(conditionsRange != null) {
                for(FactRange factRange : conditionsRange) {

                }
            }
            */

            QualificationFactor factor = qualificationFactorRepository.findQualificationFactorByIdRule(rule.getIdRule());

            if(factor != null) {
                List<FactAttribute> conditionsAttributeFactor = factAttributeRepository.findAllByIdRuleAndIdQFactor(rule.getIdRule(), factor.getIdFactor(), "publication");
                //List<FactRange> conditionsRangeFactor = factRangeRepository.findAllByIdRuleAndIdQFactor(rule.getIdRule(), factor.getIdFactor());

                if(conditionsAttributeFactor != null && !conditionsAttributeFactor.isEmpty()) {
                    for(FactAttribute factAttributeFactor : conditionsAttributeFactor) {
                        if(factAttributeFactor.getAttribute().equals(PublicationAttributesEnums.SUB_TYPE_DESCRIPTION.getAttribute())){
                            if(scopusPublication.getSubTypeDescription().equals(factAttributeFactor.getAttributeValue())){
                                score = score.multiply(factAttributeFactor.getScore());
                                break;
                            }
                        } else if (factAttributeFactor.getAttribute().equals(PublicationAttributesEnums.AGGREGATION_TYPE.getAttribute())){
                            if(scopusPublication.getAggregationType().equals(factAttributeFactor.getAttributeValue())){
                                score = score.multiply(factAttributeFactor.getScore());
                                break;
                            }
                        }
                    }
                }

                /*
                if(conditionsRangeFactor != null) {
                    for(FactRange factRangeFactor : conditionsRangeFactor) {

                    }
                }
                */
            }
        }

        if(idRuleExecuted != -1 && idSubcategory != -1 && score.compareTo(BigDecimal.ZERO) > 0) {
            PublicationEvaluation publicationEvaluation = new PublicationEvaluation();
            publicationEvaluation.setIdPublication(recordId);
            publicationEvaluation.setScore(score);
            publicationEvaluation.setIdRule(idRuleExecuted);
            publicationEvaluation.setIdSubcategory(idSubcategory);
            publicationEvaluation.setTimestamp(new Timestamp(System.currentTimeMillis()));

            publicationEvaluationRepository.save(publicationEvaluation);
            return 1;
        }

        return 0;
    }

    private Integer processConditionsProject(Project project, Funding funding, Integer recordId) {
        BigDecimal score = BigDecimal.ZERO;
        int seqIni = 0, seqEnd = 1;

        int idRuleExecuted = -1;
        int idSubcategory = -1;

        List<QualificationRule> rules = qualificationRuleRepository.getAllByScientificTypeAndStatus("project", 1);

        for(QualificationRule rule : rules) {
            List<FactAttribute> conditionsAttribute = factAttributeRepository.findAllByIdRuleAndIdQFactor(rule.getIdRule(), 0, "project");
            List<FactRange> conditionsRange = factRangeRepository.findAllByIdRuleAndIdQFactor(rule.getIdRule(), 0, "project");

            if((conditionsAttribute != null && !conditionsAttribute.isEmpty()) && (conditionsRange != null && !conditionsRange.isEmpty())) {
                seqEnd = Math.max(conditionsAttribute.getLast().getIdFA().getSeq(), conditionsRange.getLast().getIdFR().getSeq());
            } else if(conditionsAttribute != null && !conditionsAttribute.isEmpty()) {
                seqEnd = conditionsAttribute.getLast().getIdFA().getSeq();
            } else if(conditionsRange != null && !conditionsRange.isEmpty()){
                seqEnd = conditionsRange.getLast().getIdFR().getSeq();
            }

            do{
                if(conditionsAttribute != null && !conditionsAttribute.isEmpty()){
                    for (int i = seqIni; i < seqEnd; i++) {
                        FactAttribute factAttribute = conditionsAttribute.get(i);
                        if(factAttribute.getAttribute().equals(ProjectAttributesEnums.STATUS_CONCYTEC.getAttribute())){
                            if(project.getIdProjectStatusTypeCONCYTEC().equals(factAttribute.getAttributeValue())) {
                                score = factAttribute.getScore();
                                idRuleExecuted = rule.getIdRule();
                                idSubcategory = rule.getIdSubcategory();
                                break;
                            }
                        } else if (factAttribute.getAttribute().equals(ProjectAttributesEnums.FUNDING_TYPE.getAttribute())) {
                            if(funding.getIdFundingType().equals(factAttribute.getAttributeValue())) {
                                score = factAttribute.getScore();
                                idRuleExecuted = rule.getIdRule();
                                idSubcategory = rule.getIdSubcategory();
                                break;
                            }
                        }
                    }
                }

                if(conditionsRange != null && !conditionsRange.isEmpty()) {
                    for (int i = seqIni; i < seqEnd; i++) {
                        FactRange factRange = conditionsRange.get(i);
                        if(factRange.getAttribute().equals(ProjectAttributesEnums.FUNDING_AMOUNT.getAttribute())) {
                            if (funding.getAmount() != null) {
                                switch (factRange.getConditionType()) {
                                    case "[]" -> {
                                        if (funding.getAmount().compareTo(factRange.getMinValue()) >= 0 &&
                                                funding.getAmount().compareTo(factRange.getMaxValue()) <= 0) {
                                            score = factRange.getScore();
                                            idRuleExecuted = rule.getIdRule();
                                            idSubcategory = rule.getIdSubcategory();
                                            break;
                                        }
                                    }
                                    case ">" -> {
                                        if (funding.getAmount().compareTo(factRange.getMinValue()) >= 0) {
                                            score = factRange.getScore();
                                            idRuleExecuted = rule.getIdRule();
                                            idSubcategory = rule.getIdSubcategory();
                                            break;
                                        }
                                    }
                                    case "<" -> {
                                        if (funding.getAmount().compareTo(factRange.getMinValue()) <= 0) {
                                            score = factRange.getScore();
                                            idRuleExecuted = rule.getIdRule();
                                            idSubcategory = rule.getIdSubcategory();
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                seqIni++;
            }while (seqIni < seqEnd);

            QualificationFactor factor = qualificationFactorRepository.findQualificationFactorByIdRule(rule.getIdRule());

            if(factor != null) {
                List<FactAttribute> conditionsAttributeFactor = factAttributeRepository.findAllByIdRuleAndIdQFactor(rule.getIdRule(), factor.getIdFactor(), "project");
                List<FactRange> conditionsRangeFactor = factRangeRepository.findAllByIdRuleAndIdQFactor(rule.getIdRule(), factor.getIdFactor(), "project");

                if((conditionsAttributeFactor != null && !conditionsAttributeFactor.isEmpty()) && (conditionsRangeFactor != null && !conditionsRangeFactor.isEmpty())) {
                    seqEnd = Math.max(conditionsAttributeFactor.getLast().getIdFA().getSeq(), conditionsRangeFactor.getLast().getIdFR().getSeq()) - seqIni;
                } else if(conditionsAttributeFactor != null && !conditionsAttributeFactor.isEmpty()) {
                    seqEnd = conditionsAttributeFactor.getLast().getIdFA().getSeq() - seqIni;
                } else if(conditionsRangeFactor != null && !conditionsRangeFactor.isEmpty()){
                    seqEnd = conditionsRangeFactor.getLast().getIdFR().getSeq() - seqIni;;
                }

                seqIni = 0;

                do{
                    if(conditionsAttributeFactor != null && !conditionsAttributeFactor.isEmpty()) {
                        for (int i = seqIni; i < seqEnd; i++) {
                            FactAttribute factAttributeFactor = conditionsAttributeFactor.get(i);
                            if(factAttributeFactor.getAttribute().equals(ProjectAttributesEnums.STATUS_CONCYTEC.getAttribute())){
                                if(project.getIdProjectStatusTypeCONCYTEC().equals(factAttributeFactor.getAttributeValue())) {
                                    score = score.multiply(factAttributeFactor.getScore());
                                    break;
                                }
                            } else if (factAttributeFactor.getAttribute().equals(ProjectAttributesEnums.FUNDING_TYPE.getAttribute())) {
                                if(funding.getIdFundingType().equals(factAttributeFactor.getAttributeValue())) {
                                    score = score.multiply(factAttributeFactor.getScore());
                                    break;
                                }
                            }
                        }
                    }

                    if(conditionsRangeFactor != null && !conditionsRangeFactor.isEmpty()) {
                        for (int i = seqIni; i < seqEnd; i++) {
                            FactRange factRangeFactor = conditionsRangeFactor.get(i);
                            if(factRangeFactor.getAttribute().equals(ProjectAttributesEnums.FUNDING_AMOUNT.getAttribute())) {
                                if (funding.getAmount() != null) {
                                    switch (factRangeFactor.getConditionType()) {
                                        case "[]" -> {
                                            if (funding.getAmount().compareTo(factRangeFactor.getMinValue()) >= 0 &&
                                                    funding.getAmount().compareTo(factRangeFactor.getMaxValue()) <= 0) {
                                                score = score.multiply(factRangeFactor.getScore());
                                                break;
                                            }
                                        }
                                        case ">" -> {
                                            if (funding.getAmount().compareTo(factRangeFactor.getMinValue()) >= 0) {
                                                score = score.multiply(factRangeFactor.getScore());
                                                break;
                                            }
                                        }
                                        case "<" -> {
                                            if (funding.getAmount().compareTo(factRangeFactor.getMinValue()) <= 0) {
                                                score = score.multiply(factRangeFactor.getScore());
                                                break;
                                            }
                                        }
                                    }
                                    }
                            }
                        }
                    }

                    seqIni++;
                }while(seqIni < seqEnd);
            }
        }

        if(idRuleExecuted != -1 && idSubcategory != -1 && score.compareTo(BigDecimal.ZERO) > 0) {
            ProjectEvaluation projectEvaluation = new ProjectEvaluation();
            projectEvaluation.setIdProject(String.valueOf(recordId));
            projectEvaluation.setIdRule(idRuleExecuted);
            projectEvaluation.setIdSubcategory(idSubcategory);
            projectEvaluation.setScore(score);
            projectEvaluation.setTimestamp(new Timestamp(System.currentTimeMillis()));

            projectEvaluationRepository.save(projectEvaluation);

            return 1;
        }

        return 0;
    }

    public void updateScoreToResearchGroup(LogEntry logEntry) {
        String idOrgUnitSelected = null;
        Integer idEvaluation, idPECategory;

        if(logEntry.getTableName().equals("publication")) {
            PublicationEvaluation publicationEvaluation;

            List<PublicationEvaluation> publicationEvaluationList = publicationEvaluationRepository.findListOfPublicationEvaluationByIdPublicationOrderByTimestampAsc(logEntry.getRecordId());
            if(publicationEvaluationList != null && !publicationEvaluationList.isEmpty()) {
                publicationEvaluation = publicationEvaluationList.getFirst();
            } else {
                publicationEvaluation = publicationEvaluationRepository.findPublicationEvaluationByIdPublication(logEntry.getRecordId());
            }

            List<ResearchGroupSciProdDetailDto> researchGroups = commonImpl.getResearchGroupNamesOfPublication(Long.valueOf(logEntry.getRecordId()));

            if(researchGroups != null && !researchGroups.isEmpty()) {
                for(ResearchGroupSciProdDetailDto researchGroup : researchGroups) {
                    if(researchGroup.getSeqPerson().equals("1")){
                        idOrgUnitSelected = researchGroup.getIdOrgUnit();
                        break;
                    }
                }

                updateScore(idOrgUnitSelected, publicationEvaluation.getIdSubcategory(), publicationEvaluation.getScore());
            }

        } else if(logEntry.getTableName().equals("project")) {
            ProjectEvaluation projectEvaluation;

            List<ProjectEvaluation> projectEvaluationList = projectEvaluationRepository.findListProjectEvaluationByIdProjectOrderByTimestampAsc(String.valueOf(logEntry.getRecordId()));
            if(projectEvaluationList != null && !projectEvaluationList.isEmpty()) {
                projectEvaluation = projectEvaluationList.getFirst();
            } else {
                projectEvaluation = projectEvaluationRepository.findProjectEvaluationByIdProject(String.valueOf(logEntry.getRecordId()));
            }

            List<ResearchGroupSciProdDetailDto> researchGroups = commonImpl.getResearchGroupNamesOfProject(Long.valueOf(logEntry.getRecordId()));

            if(researchGroups != null && !researchGroups.isEmpty()) {
                for(ResearchGroupSciProdDetailDto researchGroup : researchGroups) {
                    if(researchGroup.getIdPersonRole().equals("INVESTIGADOR_PRINCIPAL")){
                        idOrgUnitSelected = researchGroup.getIdOrgUnit();
                        break;
                    }
                }

                updateScore(idOrgUnitSelected, projectEvaluation.getIdSubcategory(), projectEvaluation.getScore());
            }
        }
    }

    private void updateScore(String idOrgUnitSelected, Integer idSubcategory, BigDecimal score) {
        PerformanceEvaluation performanceEvaluation;
        Integer idPECategory;

        if(idOrgUnitSelected != null) {
            performanceEvaluation = validatePerformanceEvaluationExists(idOrgUnitSelected);
            idPECategory = performanceEvaluationSubcategoryRepository.findIdCategoryByIdSubcategory(idSubcategory);

            OrgUnitCategoryScore orgUnitCategoryScore = orgUnitCategoryScoreRepository.findByIdEvaluationAndIdCategory(performanceEvaluation.getIdEvaluation(), idPECategory);

            if(orgUnitCategoryScore != null) {
                orgUnitCategoryScore.setScore(orgUnitCategoryScore.getScore().add(score));
                orgUnitCategoryScore.setQuantity(orgUnitCategoryScore.getQuantity() + 1);

                orgUnitCategoryScoreRepository.save(orgUnitCategoryScore);
            } else {
                OrgUnitCategoryScore newOrgUnitCategoryScore = new OrgUnitCategoryScore();
                newOrgUnitCategoryScore.setIdEvaluation(performanceEvaluation.getIdEvaluation());
                newOrgUnitCategoryScore.setIdCategory(idPECategory);
                newOrgUnitCategoryScore.setScore(score);
                newOrgUnitCategoryScore.setQuantity(1);

                orgUnitCategoryScoreRepository.save(newOrgUnitCategoryScore);

                //To register the remaining categories initially
                List<Integer> idCategories = performanceEvaluationSubcategoryRepository.findAllIdCategories();
                for(Integer idCategory : idCategories) {
                    if(!idCategory.equals(idPECategory)) {
                        OrgUnitCategoryScore defaultOrgUnitCategoryScore = new OrgUnitCategoryScore();
                        defaultOrgUnitCategoryScore.setIdEvaluation(performanceEvaluation.getIdEvaluation());
                        defaultOrgUnitCategoryScore.setIdCategory(idCategory);
                        defaultOrgUnitCategoryScore.setScore(BigDecimal.valueOf(0));
                        defaultOrgUnitCategoryScore.setQuantity(0);

                        orgUnitCategoryScoreRepository.save(defaultOrgUnitCategoryScore);
                    }
                }
            }

            performanceEvaluation.setTotalScore(performanceEvaluation.getTotalScore().add(score));
            performanceEvaluationRepository.save(performanceEvaluation);
        }
    }

    private PerformanceEvaluation validatePerformanceEvaluationExists(String idOrgUnitSelected) {
        PerformanceEvaluation performanceEvaluation = performanceEvaluationRepository.findPerformanceEvaluationByIdOrgUnit(idOrgUnitSelected);

        if(performanceEvaluation != null) {
            return performanceEvaluation;
        } else{
            PerformanceEvaluation newPerformanceEvaluation = new PerformanceEvaluation();
            newPerformanceEvaluation.setIdOrgUnit(idOrgUnitSelected);
            newPerformanceEvaluation.setTotalScore(BigDecimal.valueOf(0));

            LocalDate currentYear = LocalDate.of(LocalDate.now().getYear(), 1, 1);
            newPerformanceEvaluation.setEvaluationYear(Date.valueOf(currentYear));

            Integer categoryRange = orgUnitEvaluationCategoryRangeRepository.findIdCategoryBetweenMinValueAndMaxValue(newPerformanceEvaluation.getTotalScore());
            newPerformanceEvaluation.setIdCategoryRange(categoryRange);

            performanceEvaluationRepository.save(newPerformanceEvaluation);

            return newPerformanceEvaluation;
        }
    }

}
