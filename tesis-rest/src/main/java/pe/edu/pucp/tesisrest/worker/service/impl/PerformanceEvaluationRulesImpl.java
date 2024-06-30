package pe.edu.pucp.tesisrest.worker.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import pe.edu.pucp.tesisrest.common.enums.ActiveEnums;
import pe.edu.pucp.tesisrest.common.enums.ResultCodeEnum;
import pe.edu.pucp.tesisrest.common.model.perfomance.*;
import pe.edu.pucp.tesisrest.common.util.ValidationUtils;
import pe.edu.pucp.tesisrest.worker.dto.*;
import pe.edu.pucp.tesisrest.worker.dto.request.DisableRuleRequest;
import pe.edu.pucp.tesisrest.worker.dto.request.NewRuleRequest;
import pe.edu.pucp.tesisrest.worker.dto.request.PerformanceEvaluationRulesListRequest;
import pe.edu.pucp.tesisrest.worker.dto.response.DisableRuleResponse;
import pe.edu.pucp.tesisrest.worker.dto.response.NewRuleResponse;
import pe.edu.pucp.tesisrest.worker.dto.response.PerformanceEvaluationRulesListResponse;
import pe.edu.pucp.tesisrest.worker.repository.*;
import pe.edu.pucp.tesisrest.worker.service.PerformanceEvaluationRulesService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class PerformanceEvaluationRulesImpl implements PerformanceEvaluationRulesService {

    private final ValidationUtils validationUtils;
    private final EntityManager entityManager;
    private final QualificationRuleRepository qualificationRuleRepository;
    private final QualificationFactorRepository qualificationFactorRepository;
    private final FactAttributeRepository factAttributeRepository;
    private final FactRangeRepository factRangeRepository;
    private final PublicationEvaluationRepository publicationEvaluationRepository;
    private final ProjectEvaluationRepository projectEvaluationRepository;

    @Override
    public PerformanceEvaluationRulesListResponse getPerformanceEvaluationRulesList(PerformanceEvaluationRulesListRequest request) {
        validationUtils.validateKeyCode(request.getKeyCode());
        PerformanceEvaluationRulesListResponse response = new PerformanceEvaluationRulesListResponse();

        List<PerformanceEvaluationSubcategoryDto> subcategories;

        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT ");
        sql.append(" pec.id_category, ");
        sql.append(" pec.category_name ");

        sql.append(" FROM performance_evaluation_category pec ");
        sql.append(" WHERE pec.category_status = 'Activo' ");

        Query query = entityManager.createNativeQuery(sql.toString());
        List<Object[]> resultList = query.getResultList();

        if(!CollectionUtils.isEmpty(resultList)){
            for (Object[] item : resultList) {
                PerformanceEvaluationCategoryDto categoryDto = new PerformanceEvaluationCategoryDto();

                categoryDto.setIdCategory(item[0] != null ? (int) item[0] : null);
                categoryDto.setCategoryName(item[1] != null ? (String) item[1] : null);

                subcategories = getSubcategoriesByCategoryID(categoryDto.getIdCategory());

                categoryDto.setSubcategories(subcategories);

                response.getResult().add(categoryDto);
            }
        }
        else{
            response.setValues(ResultCodeEnum.NO_RESULTS.getCode(), ResultCodeEnum.NO_RESULTS.getMessage());
        }

        response.setTotal((long) response.getResult().size());

        return response;
    }

    @Override
    @Transactional
    public NewRuleResponse addNewRule(NewRuleRequest request) {
        validationUtils.validateKeyCode(request.getKeyCode());
        NewRuleResponse response = new NewRuleResponse();

        try {
            QualificationRule rule = new QualificationRule();
            rule.setRuleName(request.getRuleName());
            rule.setIdSubcategory(request.getIdSubcategory());
            rule.setScientificType(request.getScientificType());
            rule.setStatus(ActiveEnums.ACTIVE.getCode());

            qualificationRuleRepository.save(rule);

            Integer lastSeqFA = factAttributeRepository.findLastSeq(rule.getIdRule());
            Integer lastSeqFR = factRangeRepository.findLastSeq(rule.getIdRule());
            // Determinate the value of lastSeq
            Integer lastSeq;
            if (lastSeqFA != null && lastSeqFR != null) {
                lastSeq = Math.max(lastSeqFA, lastSeqFR);
            } else if (lastSeqFA != null) {
                lastSeq = lastSeqFA;
            } else if (lastSeqFR != null) {
                lastSeq = lastSeqFR;
            } else {
                lastSeq = 1;
            }

            // Save fact attributes related
            //saveFactAttributesV0(rule.getIdRule(), request.getScientificType(), request.getFactAttributeList(), lastSeq);

            // Save fact ranges related
            //saveFactRangesV0(rule.getIdRule(), request.getScientificType(), request.getFactRangeList(), lastSeq);

            // Save criteria facts related
            for (NewFactGeneralDto item : request.getFactGeneralList()) {
                if(Objects.equals(item.getType(), "textual")){
                    saveFactAttributes(rule.getIdRule(), request.getScientificType(), item, lastSeq, null);
                } else {
                    saveFactRanges(rule.getIdRule(), request.getScientificType(), item, lastSeq, null);
                }
                lastSeq++;
            }

            //Create new multiplication factor
            if(!request.getMultFactorList().isEmpty()){
                QualificationFactor multiplyFactor = new QualificationFactor();
                multiplyFactor.setIdRule(rule.getIdRule());
                multiplyFactor.setScientificType(request.getScientificType());

                qualificationFactorRepository.save(multiplyFactor);

                // Save multiplication factors criteria related
                for (NewFactGeneralDto item : request.getMultFactorList()) {
                    if(Objects.equals(item.getType(), "textual")){
                        saveFactAttributes(rule.getIdRule(), request.getScientificType(), item, lastSeq, multiplyFactor.getIdFactor());
                    } else {
                        saveFactAttributes(rule.getIdRule(), request.getScientificType(), item, lastSeq, multiplyFactor.getIdFactor());
                    }
                    lastSeq++;
                }
            }

            NewRuleDto newRuleDto = new NewRuleDto();
            newRuleDto.setRule(rule);

            response.setNewRule(newRuleDto);

        } catch (Exception e) {
            throw e;
        }

        return response;
    }

    @Override
    public DisableRuleResponse disableRule(DisableRuleRequest request) {
        validationUtils.validateKeyCode(request.getKeyCode());
        DisableRuleResponse response =  new DisableRuleResponse();

        QualificationRule rule = qualificationRuleRepository.findByIdRule(request.getIdRule());

        if(publicationEvaluationRepository.findPublicationEvaluationByIdRule(request.getIdRule()) == null &&
            projectEvaluationRepository.findProjectEvaluationByIdRule(request.getIdRule()) == null){

            rule.setStatus(ActiveEnums.INACTIVE.getCode());

            qualificationRuleRepository.save(rule);

            response.setMessageConfirm("La regla " + rule.getRuleName() + " ha sido deshabilitada.");
        }
        else{
            response.setMessageConfirm("La regla " + rule.getRuleName() + " no puede ser deshabilitada porque ya ha sido utilizada para alguna calificación.");
        }

        return response;
    }

    private void saveFactAttributes(int idRule, String scientificType, NewFactGeneralDto item, Integer lastSeq, Integer idFactor) {
        FactAttributeId factAttributeId = new FactAttributeId();
        factAttributeId.setId_rule(idRule);
        factAttributeId.setSeq(lastSeq);

        FactAttribute factAttribute = new FactAttribute();
        factAttribute.setId(factAttributeId);
        factAttribute.setScientificType(scientificType);
        factAttribute.setAttribute(item.getAttribute());
        factAttribute.setConditionType(item.getConditionType());
        factAttribute.setAttributeValue(item.getAttributeValue());
        factAttribute.setScore(item.getScore());
        factAttribute.setConnector(item.getConnector());

        if(idFactor != null){
            factAttribute.setIdQFactor(idFactor);
        }

        factAttributeRepository.save(factAttribute);
    }

    private void saveFactRanges(int idRule, String scientificType, NewFactGeneralDto item, Integer lastSeq, Integer idFactor) {
        FactRangeId factRangeId = new FactRangeId();
        factRangeId.setId_rule(idRule);
        factRangeId.setSeq(lastSeq);

        FactRange factRange = new FactRange();
        factRange.setId(factRangeId);
        factRange.setScientificType(scientificType);
        factRange.setAttribute(item.getAttribute());
        factRange.setConditionType(item.getConditionType());
        factRange.setMinValue(item.getMinValue());
        factRange.setMaxValue(item.getMaxValue());
        factRange.setScore(item.getScore());
        factRange.setConnector(item.getConnector());

        if(idFactor != null){
            factRange.setIdQFactor(idFactor);
        }

        factRangeRepository.save(factRange);
    }

    private List<PerformanceEvaluationSubcategoryDto> getSubcategoriesByCategoryID(int idCategory) {
        List<PerformanceEvaluationSubcategoryDto> subcategories = new ArrayList<>();
        List<PerformanceEvaluationRuleDto> rules;

        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT ");
        sql.append(" pes.id_subcategory, ");
        sql.append(" pes.subcategory_name ");

        sql.append(" FROM performance_evaluation_subcategory pes ");
        sql.append(" WHERE pes.id_category = :idCategory ");
        sql.append(" AND pes.subcategory_status = 'Activo' ");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("idCategory", idCategory);

        List<Object[]> resultList = query.getResultList();
        if(!CollectionUtils.isEmpty(resultList)){
            for (Object[] item : resultList) {
                PerformanceEvaluationSubcategoryDto subcategoryDto = new PerformanceEvaluationSubcategoryDto();

                subcategoryDto.setIdSubcategory(item[0] != null ? (int) item[0] : null);
                subcategoryDto.setSubcategoryName(item[1] != null ? (String) item[1] : null);

                rules = getRulesBySubcategoryID(subcategoryDto.getIdSubcategory());

                subcategoryDto.setRules(rules);

                subcategories.add(subcategoryDto);
            }
        }

        return subcategories;
    }

    private List<PerformanceEvaluationRuleDto> getRulesBySubcategoryID(int idSubcategory) {
        List<PerformanceEvaluationRuleDto> rules = new ArrayList<>();;

        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT ");
        sql.append(" qr.id_rule, ");
        sql.append(" qr.qr_name ");

        sql.append(" FROM qualification_rule qr ");
        sql.append(" WHERE qr.id_subcategory = :idSubcategory ");
        sql.append(" AND qr.status = 1 ");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("idSubcategory", idSubcategory);

        List<Object[]> resultList = query.getResultList();
        if(!CollectionUtils.isEmpty(resultList)){
            for (Object[] item : resultList) {
                PerformanceEvaluationRuleDto ruleDto = new PerformanceEvaluationRuleDto();

                ruleDto.setIdRule(item[0] != null ? (int) item[0] : null);
                ruleDto.setRuleName(item[1] != null ? (String) item[1] : null);

                rules.add(ruleDto);
            }
        }

        return rules;
    }

    /* -------------------------------------------------------------- */

    private void saveFactAttributesV0(int generatedRuleID, String scientificType, List<NewFactAttributeDto> factAttributeList, Integer lastSeq) {
        for (NewFactAttributeDto item : factAttributeList) {
            FactAttributeId factAttributeId = new FactAttributeId();
            factAttributeId.setId_rule(generatedRuleID);
            factAttributeId.setSeq(lastSeq);

            FactAttribute factAttribute = new FactAttribute();
            factAttribute.setId(factAttributeId);
            factAttribute.setScientificType(scientificType);
            factAttribute.setAttribute(item.getAttribute());
            factAttribute.setConditionType(item.getConditionType());
            factAttribute.setAttributeValue(item.getAttributeValue());
            factAttribute.setScore(item.getScore());
            factAttribute.setConnector(item.getConnector());

            factAttributeRepository.save(factAttribute);
            lastSeq++;
        }
    }

    private void saveFactRangesV0(int generatedRuleID, String scientificType, List<NewFactRangeDto> factRangeList, Integer lastSeq) {
        for (NewFactRangeDto item : factRangeList) {
            FactRangeId factRangeId = new FactRangeId();
            factRangeId.setId_rule(generatedRuleID);
            factRangeId.setSeq(lastSeq);

            FactRange factRange = new FactRange();
            factRange.setId(factRangeId);
            factRange.setScientificType(scientificType);
            factRange.setAttribute(item.getAttribute());
            factRange.setConditionType(item.getConditionType());
            factRange.setMinValue(item.getMinValue());
            factRange.setMaxValue(item.getMaxValue());
            factRange.setScore(item.getScore());
            factRange.setConnector(item.getConnector());

            factRangeRepository.save(factRange);
            lastSeq++;
        }
    }
}

