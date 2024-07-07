package pe.edu.pucp.tesisrest.common.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import pe.edu.pucp.tesisrest.common.dto.*;
import pe.edu.pucp.tesisrest.common.service.CommonService;
import pe.edu.pucp.tesisrest.researcher.dto.AuthorResearcherDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommonImpl implements CommonService {

    private final EntityManager entityManager;

    @Override
    public List<FundingListDto> getFundingsOfProject(Long idProject){
        List<FundingListDto> funding = new ArrayList<>();
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT ");
        sql.append(" pf.idFunding, ");
        sql.append(" pf.FundedAs, ");
        sql.append(" pf.Categoria, ");
        sql.append(" f.CurrCode, ");
        sql.append(" f.Amount, ");
        sql.append(" f.Identifier, ");
        sql.append(" ft.Nombre, ");
        sql.append(" org.OriginalName ");

        sql.append(" FROM project_funded pf ");
        sql.append(" JOIN funding f ON pf.idFunding = f.idFunding ");
        sql.append(" JOIN funding_type ft ON f.idFunding_Type = ft.idFunding_Type ");
        sql.append(" JOIN funder fu ON f.idFunding = fu.idFunding ");
        sql.append(" JOIN orgunit org ON fu.idOrgUnit = org.idOrgUnit ");
        sql.append(" WHERE pf.idProject = :idProject");

        Query query = entityManager.createNativeQuery(sql.toString());

        query.setParameter("idProject", idProject);

        List<Object[]> resultList = query.getResultList();

        if(!CollectionUtils.isEmpty(resultList)){
            for (Object[] item : resultList) {
                FundingListDto fundingListDto = new FundingListDto();

                fundingListDto.setIdFunding(item[0] != null ? Long.valueOf(item[0].toString()) : null);
                fundingListDto.setFundedAs(item[1] != null ? item[1].toString() : null);
                fundingListDto.setCategory(item[2] != null ? item[2].toString() : null);
                fundingListDto.setCurrCode(item[3] != null ? item[3].toString() : null);
                fundingListDto.setAmount(item[4] != null ? Double.parseDouble(item[4].toString()) : null);
                fundingListDto.setIdentifier(item[5] != null ? item[5].toString() : null);
                fundingListDto.setFundingType(item[6] != null ? item[6].toString() : null);
                fundingListDto.setFundedBy(item[7] != null ? item[7].toString() : null);

                funding.add(fundingListDto);
            }
        }

        return funding;
    }

    @Override
    public List<AuthorResearcherDto> getAuthorsOfPublication(Long publicationId){
        StringBuilder sql = new StringBuilder();

        List<AuthorResearcherDto> authors = new ArrayList<>();

        sql.append(" SELECT ");
        sql.append(" au.idPerson, ");
        sql.append(" au.idOrgUnit, ");
        sql.append(" au.Surname, ");
        sql.append(" au.GivenName, ");
        sql.append(" au.ScopusAuthorID ");

        sql.append(" FROM publication pu ");
        sql.append(" JOIN publication_author au ON pu.idPublication = au.idPublication ");
        sql.append(" WHERE pu.idPublication = :publication_id");

        Query query = entityManager.createNativeQuery(sql.toString());

        query.setParameter("publication_id", publicationId);

        List<Object[]> resultList = query.getResultList();

        if(!CollectionUtils.isEmpty(resultList)){
            for (Object[] item : resultList) {
                AuthorResearcherDto authorDto = new AuthorResearcherDto();

                authorDto.setIdPerson(item[0] != null ? item[0].toString() : null);
                authorDto.setIdOrgUnit(item[1] != null ? item[1].toString() : null);
                authorDto.setSurname(item[2] != null ? item[2].toString() : null);
                authorDto.setGivenName(item[3] != null ? item[3].toString() : null);
                authorDto.setGivenName(item[3] != null ? item[3].toString() : null);

                authors.add(authorDto);
            }
        }

        return authors;
    }

    @Override
    public List<ResearcherDto> getProjectTeam(Long idProject) {
        List<ResearcherDto> researchers = new ArrayList<>();
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT ");
        sql.append(" ptp.idPerson_Role, ");
        sql.append(" ptp.Tipo_Recurso, ");
        sql.append(" pe.idPerson, ");
        sql.append(" pe.FirstNames, ");
        sql.append(" pe.FamilyNames ");

        sql.append(" FROM project_team_pucp ptp ");
        sql.append(" JOIN person pe ON ptp.idPerson = pe.idPerson ");
        sql.append(" WHERE ptp.idProject = :idProject ");

        Query query = entityManager.createNativeQuery(sql.toString());

        query.setParameter("idProject", idProject);

        List<Object[]> resultList = query.getResultList();

        if(!CollectionUtils.isEmpty(resultList)){
            for (Object[] item : resultList) {
                ResearcherDto researcherDto = new ResearcherDto();

                //researcherDto.setIdRolePerson(item[0] != null ? item[0].toString() : null);

                if (item[0] != null) {
                    switch (item[0].toString()) {
                        case "ASISTENTE_INVESTIGACION":
                            researcherDto.setIdRolePerson("Asistente de investigación");
                            break;
                        case "CO_INVESTIGADOR":
                            researcherDto.setIdRolePerson("Co-investigador");
                            break;
                        case "INVESTIGADOR_PRINCIPAL":
                            researcherDto.setIdRolePerson("Investigador principal");
                            break;
                        case "COORDINADOR_GRUPO":
                            researcherDto.setIdRolePerson("Coordinador del grupo");
                            break;
                        default:
                            researcherDto.setIdRolePerson(item[0].toString());
                            break;
                    }
                }

                researcherDto.setRoleName(item[1] != null ? item[1].toString() : null);
                researcherDto.setIdPerson(item[2] != null ? item[2].toString() : null);
                researcherDto.setFirstNames(item[3] != null ? item[3].toString() : null);
                researcherDto.setFamilyNames(item[4] != null ? item[4].toString() : null);

                researchers.add(researcherDto);
            }
        }

        return researchers;
    }

    @Override
    public List<ResearcherDto> getResearchGroupTeam(String idOrgUnit) {
        List<ResearcherDto> researchers = new ArrayList<>();
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT ");
        sql.append(" pe.idPerson, ");
        sql.append(" pe.FirstNames, ");
        sql.append(" pe.FamilyNames, ");
        sql.append(" pa.idPerson_Role ");

        sql.append(" FROM person pe ");
        sql.append(" JOIN person_affiliation pa ON pe.idPerson = pa.idPerson ");
        sql.append(" WHERE pa.Status = \"ACTIVO\" ");
        sql.append(" AND pa.idOrgUnit = :idOrgUnit ");

        Query query = entityManager.createNativeQuery(sql.toString());

        query.setParameter("idOrgUnit", idOrgUnit);

        List<Object[]> resultList = query.getResultList();

        if(!CollectionUtils.isEmpty(resultList)){
            for (Object[] item : resultList) {
                ResearcherDto researcherDto = new ResearcherDto();

                researcherDto.setIdPerson(item[0] != null ? item[0].toString() : null);
                researcherDto.setFirstNames(item[1] != null ? item[1].toString() : null);
                researcherDto.setFamilyNames(item[2] != null ? item[2].toString() : null);
                researcherDto.setIdRolePerson(item[3] != null ? item[3].toString() : null);

                researchers.add(researcherDto);
            }
        }

        return researchers;
    }

    @Override
    public List<ProjectDto> getProjectsRelatedToPublication(Long publicationId) {
        List<ProjectDto> projects = new ArrayList<>();
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT ");
        sql.append(" pro.idProject, ");
        sql.append(" pro.Title, ");
        sql.append(" pro.Abstract, ");
        sql.append(" pro.StartDate, ");
        sql.append(" pro.EndDate, ");
        sql.append(" pro.idProject_Status_Type_CONCYTEC ");

        sql.append(" FROM project pro ");
        sql.append(" JOIN publication_originatesfrom pof ON pro.idProject = pof.idProject ");
        sql.append(" WHERE pof.idPublication = :idPublication ");

        Query query = entityManager.createNativeQuery(sql.toString());

        query.setParameter("idPublication", publicationId);

        List<Object[]> resultList = query.getResultList();
        List<FundingListDto> fundings;

        if(!CollectionUtils.isEmpty(resultList)){
            for (Object[] item : resultList) {
                ProjectDto projectDto = new ProjectDto();

                projectDto.setIdProject(item[0] != null ? Long.valueOf(item[0].toString()) : null);
                projectDto.setTitle(item[1] != null ? item[1].toString() : null);
                projectDto.setDescription(item[2] != null ? item[2].toString() : null);
                projectDto.setStartDate(item[3] != null ? (Date) item[3] : null);
                projectDto.setEndDate(item[4] != null ? (Date) item[4] : null);

                if (item[5] != null) {
                    switch (item[5].toString()) {
                        case "POR_INICIAR":
                            projectDto.setIdProjectStatusTypeCONCYTEC("Por iniciar");
                            break;
                        case "EN_EJECUCION":
                            projectDto.setIdProjectStatusTypeCONCYTEC("En ejecución");
                            break;
                        case "EN_PROCESO_CIERRRE":
                            projectDto.setIdProjectStatusTypeCONCYTEC("En proceso de cierre");
                            break;
                        case "CERRADO":
                            projectDto.setIdProjectStatusTypeCONCYTEC("Cerrado");
                            break;
                        case "CANCELADO":
                            projectDto.setIdProjectStatusTypeCONCYTEC("Cancelado");
                            break;
                        default:
                            projectDto.setIdProjectStatusTypeCONCYTEC(item[5].toString());
                            break;
                    }
                }

                //projectDto.setIdProjectStatusTypeCONCYTEC(item[5] != null ? item[5].toString() : null);

                fundings = getFundingsOfProject(projectDto.getIdProject());

                projectDto.setRelatedFundingList(fundings);

                projects.add(projectDto);
            }
        }

        return projects;
    }

    @Override
    public List<EvaluationDetailDto> getEvaluationDetailOfPublication(Long publicationId) {
        List<EvaluationDetailDto> evaluationDetaiList = new ArrayList<>();
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT ");
        sql.append(" pec.category_name, ");
        sql.append(" pes.subcategory_name, ");
        sql.append(" pe.score ");

        sql.append(" FROM publication_evaluation pe ");
        sql.append(" JOIN performance_evaluation_subcategory pes ON pe.id_subcategory = pes.id_subcategory ");
        sql.append(" JOIN performance_evaluation_category pec ON pes.id_category = pec.id_category ");
        sql.append(" WHERE pe.idPublication = :idPublication ");
        sql.append(" ORDER BY pe.timestamp ASC ");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("idPublication", publicationId);

        List<Object[]> resultList = query.getResultList();

        if(!CollectionUtils.isEmpty(resultList)){
            for (Object[] item : resultList) {
                EvaluationDetailDto evaluationDetailDto = new EvaluationDetailDto();
                evaluationDetailDto.setCategoryName(item[0] != null ? item[0].toString() : null);
                evaluationDetailDto.setSubcategoryName(item[1] != null ? item[1].toString() : null);
                evaluationDetailDto.setEvaluationScore(item[2] != null ? (BigDecimal) item[2]: null);

                evaluationDetaiList.add(evaluationDetailDto);
            }
        }

        return evaluationDetaiList;
    }

    @Override
    public List<EvaluationDetailDto> getEvaluationDetailOfProject(Long idProject) {
        List<EvaluationDetailDto> evaluationDetaiList = new ArrayList<>();
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT ");
        sql.append(" pec.category_name, ");
        sql.append(" pes.subcategory_name, ");
        sql.append(" pe.score ");

        sql.append(" FROM project_evaluation pe ");
        sql.append(" JOIN performance_evaluation_subcategory pes ON pe.id_subcategory = pes.id_subcategory ");
        sql.append(" JOIN performance_evaluation_category pec ON pes.id_category = pec.id_category ");
        sql.append(" WHERE pe.idProject = :idProject ");
        sql.append(" ORDER BY pe.timestamp ASC ");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("idProject", idProject);

        List<Object[]> resultList = query.getResultList();

        if(!CollectionUtils.isEmpty(resultList)){
            for (Object[] item : resultList) {
                EvaluationDetailDto evaluationDetailDto = new EvaluationDetailDto();
                evaluationDetailDto.setCategoryName(item[0] != null ? item[0].toString() : null);
                evaluationDetailDto.setSubcategoryName(item[1] != null ? item[1].toString() : null);
                evaluationDetailDto.setEvaluationScore(item[2] != null ? (BigDecimal) item[2]: null);

                evaluationDetaiList.add(evaluationDetailDto);
            }
        }

        return evaluationDetaiList;
    }

    @Override
    public List<PublicationDto> getPublicationListOfAResearchGroup(String idOrgUnit) {
        List<PublicationDto> publicationList = new ArrayList<>();
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT ");
        sql.append(" pu.idPublication, ");
        sql.append(" pu.Title, ");
        sql.append(" pu.PublishedInDesc, ");
        sql.append(" CASE WHEN pu.PublicationDate REGEXP '^(19|20)[0-9]{2}-(0[1-9]|1[0-2])-([0-2][0-9]|3[01])$' THEN pu.PublicationDate ELSE NULL END AS PublicationDate, ");
        sql.append(" rtc.idResource_Type_COAR, ");
        sql.append(" rtc.Nombre ");

        sql.append(" FROM publication_author pa ");
        sql.append(" JOIN publication pu ON pa.idPublication = pu.idPublication ");
        sql.append(" JOIN resource_type_coar rtc ON pu.idResource_Type_COAR = rtc.idResource_Type_COAR ");
        sql.append(" WHERE pa.idOrgUnit = :idOrgUnit ");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("idOrgUnit", idOrgUnit);

        List<Object[]> resultList = query.getResultList();
        List<AuthorResearcherDto> authors;

        if(!CollectionUtils.isEmpty(resultList)){
            for (Object[] item : resultList) {
                PublicationDto pubAuthorDto = new PublicationDto();

                pubAuthorDto.setPublicationId(item[0] != null ? Long.valueOf(item[0].toString()) : null);
                pubAuthorDto.setTitle(item[1] != null ? item[1].toString() : null);
                pubAuthorDto.setPublishedIn(item[2] != null ? item[2].toString() : null);
                pubAuthorDto.setPublicationDate(item[3] != null ? (Date) item[3] : null);
                pubAuthorDto.setIdResourceTypeCOAR(item[4] != null ? item[4].toString() : null);
                pubAuthorDto.setResourceTypeCOARName(item[5] != null ? item[5].toString() : null);

                authors = getAuthorsOfPublication(pubAuthorDto.getPublicationId());

                pubAuthorDto.setAuthorsList(authors);

                publicationList.add(pubAuthorDto);
            }
        }

        return publicationList;
    }

    @Override
    public List<ProjectDto> getProjectListOfAResearchGroup(String idOrgUnit) {
        List<ProjectDto> projects = new ArrayList<>();
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT ");
        sql.append(" pro.idProject, ");
        sql.append(" pro.Title, ");
        sql.append(" pro.StartDate, ");
        sql.append(" pro.EndDate, ");
        sql.append(" pro.idProject_Status_Type_CONCYTEC ");

        sql.append(" FROM project pro ");
        sql.append(" JOIN project_team_pucp ptp ON ptp.idProject = pro.idProject ");
        sql.append(" JOIN orgunit org ON ptp.idOrgUnit = org.idOrgUnit ");
        sql.append(" WHERE org.idOrgUnit = :idOrgUnit ");

        Query query = entityManager.createNativeQuery(sql.toString());

        query.setParameter("idOrgUnit", idOrgUnit);

        List<Object[]> resultList = query.getResultList();
        List<FundingListDto> fundings;

        if(!CollectionUtils.isEmpty(resultList)){
            for (Object[] item : resultList) {
                ProjectDto projectDto = new ProjectDto();

                projectDto.setIdProject(item[0] != null ? Long.valueOf(item[0].toString()) : null);
                projectDto.setTitle(item[1] != null ? item[1].toString() : null);
                projectDto.setStartDate(item[2] != null ? (Date) item[2] : null);
                projectDto.setEndDate(item[3] != null ? (Date) item[3] : null);

                if (item[4] != null) {
                    switch (item[4].toString()) {
                        case "POR_INICIAR":
                            projectDto.setIdProjectStatusTypeCONCYTEC("Por iniciar");
                            break;
                        case "EN_EJECUCION":
                            projectDto.setIdProjectStatusTypeCONCYTEC("En ejecución");
                            break;
                        case "EN_PROCESO_CIERRRE":
                            projectDto.setIdProjectStatusTypeCONCYTEC("En proceso de cierre");
                            break;
                        case "CERRADO":
                            projectDto.setIdProjectStatusTypeCONCYTEC("Cerrado");
                            break;
                        case "CANCELADO":
                            projectDto.setIdProjectStatusTypeCONCYTEC("Cancelado");
                            break;
                        default:
                            projectDto.setIdProjectStatusTypeCONCYTEC(item[4].toString());
                            break;
                    }
                }

                //projectDto.setIdProjectStatusTypeCONCYTEC(item[5] != null ? item[5].toString() : null);

                fundings = getFundingsOfProject(projectDto.getIdProject());

                projectDto.setRelatedFundingList(fundings);

                projects.add(projectDto);
            }
        }

        return projects;
    }

    @Override
    public List<ResearchGroupSciProdDetailDto> getResearchGroupNamesOfPublication(Long publicationId) {
        List<ResearchGroupSciProdDetailDto> researchGroups = new ArrayList<>();
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT ");
        sql.append(" org.Name, ");
        sql.append(" pa.seqAuthor, ");
        sql.append(" org.idOrgUnit ");

        sql.append(" FROM publication_author pa ");
        sql.append(" JOIN orgunit org ON pa.idOrgUnit = org.idOrgUnit ");
        sql.append(" WHERE pa.idPublication = :publicationId ");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("publicationId", publicationId);

        List<Object[]> resultList = query.getResultList();

        if(!CollectionUtils.isEmpty(resultList)){
            for (Object[] item : resultList) {
                ResearchGroupSciProdDetailDto researchGroup = new ResearchGroupSciProdDetailDto();
                researchGroup.setName(item[0] != null ? item[0].toString() : null);
                researchGroup.setSeqPerson(item[1] != null ? item[1].toString() : null);
                researchGroup.setIdOrgUnit(item[2] != null ? item[2].toString() : null);

                researchGroups.add(researchGroup);
            }
        }

        return researchGroups;
    }

    @Override
    public List<ResearchGroupSciProdDetailDto> getResearchGroupNamesOfProject(Long idProject) {
        List<ResearchGroupSciProdDetailDto> researchGroups = new ArrayList<>();
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT ");
        sql.append(" org.Name, ");
        sql.append(" ptp.idPerson_Role, ");
        sql.append(" org.idOrgUnit ");

        sql.append(" FROM project_team_pucp ptp ");
        sql.append(" JOIN orgunit org ON ptp.idOrgUnit = org.idOrgUnit ");
        sql.append(" WHERE ptp.idProject = :idProject ");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("idProject", idProject);

        List<Object[]> resultList = query.getResultList();

        if(!CollectionUtils.isEmpty(resultList)){
            for (Object[] item : resultList) {
                ResearchGroupSciProdDetailDto researchGroup = new ResearchGroupSciProdDetailDto();
                researchGroup.setName(item[0] != null ? item[0].toString() : null);
                researchGroup.setIdPersonRole(item[1] != null ? item[1].toString() : null);
                researchGroup.setIdOrgUnit(item[2] != null ? item[2].toString() : null);

                researchGroups.add(researchGroup);
            }
        }

        return researchGroups;
    }

    @Override
    public List<ResearchGroupEvaluationDetail> getResearchGroupEvaluationDetail(String idOrgUnit) {
        List<ResearchGroupEvaluationDetail> researchGroupEvaluationDetails = new ArrayList<>();
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT ");
        sql.append(" pec.category_name, ");
        sql.append(" ocs.score, ");
        sql.append(" ocs.quantity, ");
        sql.append(" pec.minScore ");

        sql.append(" FROM performance_evaluation pe ");
        sql.append(" JOIN orgunit_category_score ocs ON pe.id_evaluation = ocs.id_evaluation ");
        sql.append(" JOIN performance_evaluation_category pec ON ocs.id_category = pec.id_category ");
        sql.append(" WHERE pe.idOrgUnit = :idOrgUnit ");
        sql.append(" AND pe.evaluation_year like \"2024%\" ");
        sql.append(" AND pec.category_status = \"Activo\" ");


        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("idOrgUnit", idOrgUnit);

        List<Object[]> resultList = query.getResultList();

        if(!CollectionUtils.isEmpty(resultList)){
            for (Object[] item : resultList) {
                ResearchGroupEvaluationDetail researchGroupEvaluationDetail = new ResearchGroupEvaluationDetail();
                researchGroupEvaluationDetail.setCategoryName(item[0] != null ? item[0].toString() : null);
                researchGroupEvaluationDetail.setCategoryScore(item[1] != null ? (BigDecimal) item[1] : null);
                researchGroupEvaluationDetail.setQuantity(item[2] != null ? (Integer) item[2] : null);
                researchGroupEvaluationDetail.setMinimumScore(item[3] != null ? (BigDecimal) item[3] : null);

                researchGroupEvaluationDetails.add(researchGroupEvaluationDetail);
            }
        }

        return researchGroupEvaluationDetails;
    }
}
