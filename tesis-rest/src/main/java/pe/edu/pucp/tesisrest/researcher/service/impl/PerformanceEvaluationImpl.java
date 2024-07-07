package pe.edu.pucp.tesisrest.researcher.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import pe.edu.pucp.tesisrest.common.dto.*;
import pe.edu.pucp.tesisrest.common.enums.ResultCodeEnum;
import pe.edu.pucp.tesisrest.common.repository.OrgUnitRepository;
import pe.edu.pucp.tesisrest.common.service.CommonService;
import pe.edu.pucp.tesisrest.common.util.ValidationUtils;
import pe.edu.pucp.tesisrest.researcher.dto.*;
import pe.edu.pucp.tesisrest.researcher.dto.request.*;
import pe.edu.pucp.tesisrest.researcher.dto.response.*;
import pe.edu.pucp.tesisrest.researcher.service.PerformanceEvaluationService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class PerformanceEvaluationImpl implements PerformanceEvaluationService {

    private final ValidationUtils validationUtils;
    private final EntityManager entityManager;
    private final OrgUnitRepository orgunitRepository;
    private final CommonService commonService;

    @Override
    public PublicationAuthorListResponse getPublicationAuthorList(PublicationAuthorListRequest request) {
        validationUtils.validateKeyCode(request.getKeyCode());
        Map<String, Object> parameters = new HashMap<>();
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT ");
        sql.append(" pu.idPublication, ");
        sql.append(" pu.Title, ");
        sql.append(" pu.PublishedInDesc, ");
        sql.append(" CASE WHEN pu.PublicationDate REGEXP '^(19|20)[0-9]{2}-(0[1-9]|1[0-2])-([0-2][0-9]|3[01])$' THEN pu.PublicationDate ELSE NULL END AS PublicationDate, ");
        sql.append(" rtc.idResource_Type_COAR, ");
        sql.append(" rtc.Nombre ");

        sql.append(" FROM publication pu ");
        sql.append(" JOIN publication_author au ON pu.idPublication = au.idPublication ");
        sql.append(" JOIN resource_type_coar rtc ON pu.idResource_Type_COAR = rtc.idResource_Type_COAR ");
        sql.append(" WHERE au.ScopusAuthorID = :scopusAuthorId ");

        parameters.put("scopusAuthorId", request.getScopusAuthorId());

        if (StringUtils.hasText(request.getTitle())) {
            sql.append(" AND LOWER(pu.Title) LIKE LOWER(CONCAT('%', :title, '%')) ");
            parameters.put("title", request.getTitle().toLowerCase());
        }

        if (StringUtils.hasText(request.getPublishedIn())) {
            sql.append(" AND LOWER(pu.PublishedInDesc) LIKE LOWER(CONCAT('%', :publishedIn, '%')) ");
            parameters.put("publishedIn", request.getPublishedIn().toLowerCase());
        }

        if (StringUtils.hasText(request.getResourceType())) {
            sql.append(" AND rtc.idResource_Type_COAR = :resourceType ");
            parameters.put("resourceType", request.getResourceType());
        }

        if (StringUtils.hasText(request.getYear())) {
            sql.append(" AND EXTRACT(YEAR FROM pu.PublicationDate) = :year ");
            parameters.put("year", request.getYear());
        }

        sql.append(" ORDER BY pu.PublicationDate DESC");

        Query query = entityManager.createNativeQuery(sql.toString());
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        /*
        sql.append(" ORDER BY pu.PublicationDate DESC ");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("scopusAuthorId", request.getScopusAuthorId());

         */
        List<Object[]> resultList = query.getResultList();

        PublicationAuthorListResponse response = new PublicationAuthorListResponse();
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

                authors = commonService.getAuthorsOfPublication(pubAuthorDto.getPublicationId());

                pubAuthorDto.setAuthorsList(authors);

                response.getResult().add(pubAuthorDto);
            }
        }
        else{
            response.setValues(ResultCodeEnum.NO_RESULTS.getCode(), ResultCodeEnum.NO_RESULTS.getMessage());
        }

        response.setTotal((long) response.getResult().size());

        return response;
    }

    @Override
    public PublicationAuthorDetailResponse getPublicationDetailById(PublicationAuthorDetailRequest request) {
        validationUtils.validateKeyCode(request.getKeyCode());
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT ");
        sql.append(" pu.idPublication, ");
        sql.append(" pu.Title, ");
        sql.append(" pu.PublishedInDesc, ");
        sql.append(" CASE WHEN pu.PublicationDate REGEXP '^(19|20)[0-9]{2}-(0[1-9]|1[0-2])-([0-2][0-9]|3[01])$' THEN pu.PublicationDate ELSE NULL END AS PublicationDate, ");
        sql.append(" rtc.idResource_Type_COAR, ");
        sql.append(" rtc.Nombre, ");
        sql.append(" pu.Abstract, ");
        sql.append(" pu.Volume, ");
        sql.append(" pu.StartPage, ");
        sql.append(" pu.EndPage ");

        sql.append(" FROM publication pu ");
        sql.append(" JOIN publication_author au ON pu.idPublication = au.idPublication ");
        sql.append(" JOIN resource_type_coar rtc ON pu.idResource_Type_COAR = rtc.idResource_Type_COAR ");
        sql.append(" WHERE pu.idPublication = :idPublication ");

        Query query = entityManager.createNativeQuery(sql.toString());

        query.setParameter("idPublication", request.getPublicationId());

        PublicationAuthorDetailResponse response = new PublicationAuthorDetailResponse();
        List<AuthorResearcherDto> authors;
        List<ProjectDto> projects;
        List<EvaluationDetailDto> evaluationDetail;
        List<ResearchGroupSciProdDetailDto> researchGroups;

        List<Object[]> resultList = query.getResultList();

        if(!CollectionUtils.isEmpty(resultList)){
            for (Object[] item : resultList) {
                PublicationDetailDto pubAuthorDto = new PublicationDetailDto();

                pubAuthorDto.setPublicationId(item[0] != null ? Long.valueOf(item[0].toString()) : null);
                pubAuthorDto.setTitle(item[1] != null ? item[1].toString() : null);
                pubAuthorDto.setPublishedIn(item[2] != null ? item[2].toString() : null);
                pubAuthorDto.setPublicationDate(item[3] != null ? (Date) item[3] : null);
                pubAuthorDto.setIdResourceTypeCOAR(item[4] != null ? item[4].toString() : null);
                pubAuthorDto.setResourceTypeCOARName(item[5] != null ? item[5].toString() : null);
                pubAuthorDto.setAbstractPublication(item[6] != null ? item[6].toString() : null);
                pubAuthorDto.setVolume(item[7] != null ? item[7].toString() : null);
                pubAuthorDto.setStartPage(item[8] != null ? item[8].toString() : null);
                pubAuthorDto.setEndPage(item[9] != null ? item[9].toString() : null);

                authors = commonService.getAuthorsOfPublication(pubAuthorDto.getPublicationId());
                pubAuthorDto.setAuthorsList(authors);

                projects = commonService.getProjectsRelatedToPublication(pubAuthorDto.getPublicationId());
                pubAuthorDto.setRelatedProjects(projects);

                evaluationDetail = commonService.getEvaluationDetailOfPublication(pubAuthorDto.getPublicationId());
                if (evaluationDetail != null && !evaluationDetail.isEmpty()) {
                    pubAuthorDto.setEvaluationDetail(evaluationDetail.getFirst());
                } else {
                    pubAuthorDto.setEvaluationDetail(null);
                }

                researchGroups = commonService.getResearchGroupNamesOfPublication(pubAuthorDto.getPublicationId());
                pubAuthorDto.setResearchGroups(researchGroups);

                response.setPublicationAuthorDetail(pubAuthorDto);
            }
        }
        else{
            response.setValues(ResultCodeEnum.NO_RESULTS.getCode(), ResultCodeEnum.NO_RESULTS.getMessage());
        }

        return response;
    }

    @Override
    public ProjectAuthorListResponse getProjectAuthorList(ProjectAuthorListRequest request) {
        validationUtils.validateKeyCode(request.getKeyCode());
        Map<String, Object> parameters = new HashMap<>();
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT ");
        sql.append(" pro.idProject, ");
        sql.append(" pro.Title, ");
        sql.append(" pro.Abstract, ");
        sql.append(" pro.StartDate, ");
        sql.append(" pro.EndDate, ");
        sql.append(" pro.idProject_Status_Type_CONCYTEC ");

        sql.append(" FROM project pro ");
        sql.append(" JOIN project_team_pucp ptp ON ptp.idProject = pro.idProject ");
        sql.append(" JOIN person pe ON ptp.idPerson = pe.idPerson ");
        sql.append(" WHERE pe.idPerson = :idPerson ");

        parameters.put("idPerson", request.getIdPerson());

        if (StringUtils.hasText(request.getTitle())) {
            sql.append(" AND LOWER(pro.title) LIKE LOWER(CONCAT('%', :title, '%')) ");
            parameters.put("title", request.getTitle().toLowerCase());
        }

        if (StringUtils.hasText(request.getStatus())) {
            sql.append(" AND pro.idProject_Status_Type_CONCYTEC = :status ");
            parameters.put("status", request.getStatus());
        }

        if (StringUtils.hasText(request.getStartDate())) {
            DateTimeFormatter DateTimeFormatter = null;
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDate = LocalDate.parse(request.getStartDate(), inputFormatter);
            String formattedStartDate = startDate.format(outputFormatter);

            sql.append(" AND pro.StartDate >= :startDate ");
            parameters.put("startDate", formattedStartDate);
        }

        if (StringUtils.hasText(request.getEndDate())) {
            DateTimeFormatter DateTimeFormatter = null;
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDate = LocalDate.parse(request.getEndDate(), inputFormatter);
            String formattedStartDate = startDate.format(outputFormatter);

            sql.append(" AND pro.EndDate <= :EndDate ");
            parameters.put("endDate", formattedStartDate);
        }

        sql.append(" ORDER BY pro.StartDate DESC");

        Query query = entityManager.createNativeQuery(sql.toString());
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        //Query query = entityManager.createNativeQuery(sql.toString());

        //query.setParameter("idPerson", request.getIdPerson());

        List<Object[]> resultList = query.getResultList();

        ProjectAuthorListResponse response = new ProjectAuthorListResponse();
        List<FundingListDto> fundings;

        if(!CollectionUtils.isEmpty(resultList)){
            for (Object[] item : resultList) {
                ProjectDto proAuthorDto = new ProjectDto();

                proAuthorDto.setIdProject(item[0] != null ? Long.valueOf(item[0].toString()) : null);
                proAuthorDto.setTitle(item[1] != null ? item[1].toString() : null);
                proAuthorDto.setDescription(item[2] != null ? item[2].toString() : null);
                proAuthorDto.setStartDate(item[3] != null ? (Date) item[3] : null);
                proAuthorDto.setEndDate(item[4] != null ? (Date) item[4] : null);

                if (item[5] != null) {
                    switch (item[5].toString()) {
                        case "POR_INICIAR":
                            proAuthorDto.setIdProjectStatusTypeCONCYTEC("Por iniciar");
                            break;
                        case "EN_EJECUCION":
                            proAuthorDto.setIdProjectStatusTypeCONCYTEC("En ejecución");
                            break;
                        case "EN_PROCESO_CIERRRE":
                            proAuthorDto.setIdProjectStatusTypeCONCYTEC("En proceso de cierre");
                            break;
                        case "CERRADO":
                            proAuthorDto.setIdProjectStatusTypeCONCYTEC("Cerrado");
                            break;
                        case "CANCELADO":
                            proAuthorDto.setIdProjectStatusTypeCONCYTEC("Cancelado");
                            break;
                        default:
                            proAuthorDto.setIdProjectStatusTypeCONCYTEC(item[5].toString());
                            break;
                    }
                }

                //proAuthorDto.setIdProjectStatusTypeCONCYTEC(item[5] != null ? item[5].toString() : null);

                fundings = commonService.getFundingsOfProject(proAuthorDto.getIdProject());

                proAuthorDto.setRelatedFundingList(fundings);

                response.getResult().add(proAuthorDto);
            }
        }
        else{
            response.setValues(ResultCodeEnum.NO_RESULTS.getCode(), ResultCodeEnum.NO_RESULTS.getMessage());
        }

        response.setTotal((long) response.getResult().size());

        return response;
    }

    @Override
    public ProjectAuthorDetailResponse getProjectDetailById(ProjectAuthorDetailRequest request) {
        validationUtils.validateKeyCode(request.getKeyCode());
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT ");
        sql.append(" pro.idProject, ");
        sql.append(" pro.Title, ");
        sql.append(" pro.Abstract, ");
        sql.append(" pro.StartDate, ");
        sql.append(" pro.EndDate, ");
        sql.append(" pro.idProject_Status_Type_CONCYTEC ");

        sql.append(" FROM project pro ");
        sql.append(" WHERE pro.idProject = :idProject ");

        Query query = entityManager.createNativeQuery(sql.toString());

        query.setParameter("idProject", request.getIdProject());

        ProjectAuthorDetailResponse response = new ProjectAuthorDetailResponse();
        List<FundingListDto> fundings;
        List<ResearcherDto> researchers;
        List<EvaluationDetailDto> evaluationDetail;
        List<ResearchGroupSciProdDetailDto> researchGroupList;

        try {
            Object[] result = (Object[]) query.getSingleResult();
            ProjectDetailDto proDetailDto = new ProjectDetailDto();

            proDetailDto.setIdProject(result[0] != null ? Long.valueOf(result[0].toString()) : null);
            proDetailDto.setTitle(result[1] != null ? (String) result[1] : null);
            proDetailDto.setDescription(result[2] != null ? (String) result[2] : null);
            proDetailDto.setStartDate(result[3] != null ? (Date) result[3] : null);
            proDetailDto.setEndDate(result[4] != null ? (Date) result[4] : null);

            if (result[5] != null) {
                switch (result[5].toString()) {
                    case "POR_INICIAR":
                        proDetailDto.setIdProjectStatusTypeCONCYTEC("Por iniciar");
                        break;
                    case "EN_EJECUCION":
                        proDetailDto.setIdProjectStatusTypeCONCYTEC("En ejecución");
                        break;
                    case "EN_PROCESO_CIERRRE":
                        proDetailDto.setIdProjectStatusTypeCONCYTEC("En proceso de cierre");
                        break;
                    case "CERRADO":
                        proDetailDto.setIdProjectStatusTypeCONCYTEC("Cerrado");
                        break;
                    case "CANCELADO":
                        proDetailDto.setIdProjectStatusTypeCONCYTEC("Cancelado");
                        break;
                    default:
                        proDetailDto.setIdProjectStatusTypeCONCYTEC(result[5].toString());
                        break;
                }
            }

            //proDetailDto.setIdProjectStatusTypeCONCYTEC(result[5] != null ? (String) result[5] : null);

            fundings = commonService.getFundingsOfProject(proDetailDto.getIdProject());
            proDetailDto.setRelatedFundingList(fundings);

            researchers = commonService.getProjectTeam(proDetailDto.getIdProject());
            proDetailDto.setResearchers(researchers);

            evaluationDetail = commonService.getEvaluationDetailOfProject(proDetailDto.getIdProject());
            if (evaluationDetail != null && !evaluationDetail.isEmpty()) {
                proDetailDto.setEvaluationDetail(evaluationDetail.getFirst());
            } else {
                proDetailDto.setEvaluationDetail(null);
            }

            researchGroupList = commonService.getResearchGroupNamesOfProject(proDetailDto.getIdProject());
            proDetailDto.setResearchGroups(researchGroupList);

            response.setProjectAuthorDetail(proDetailDto);
        } catch (NoResultException e) {
            response.setValues(ResultCodeEnum.NO_RESULTS.getCode(), ResultCodeEnum.NO_RESULTS.getMessage());
        }

        return response;
    }

    @Override
    public FundingListResponse getFundingRelatedList(FundingListRequest request) {
        validationUtils.validateKeyCode(request.getKeyCode());
        FundingListResponse response = new FundingListResponse();

        if(!commonService.getFundingsOfProject(request.getIdProject()).isEmpty()){
            response.setResult(commonService.getFundingsOfProject(request.getIdProject()));
        }
        else{
            response.setValues(ResultCodeEnum.NO_RESULTS.getCode(), ResultCodeEnum.NO_RESULTS.getMessage());
        }

        response.setTotal((long) response.getResult().size());

        return response;
    }

    @Override
    public FundingListResponse getFundingRelatedDetailByPersonId(FundingListRequest request) {
        validationUtils.validateKeyCode(request.getKeyCode());
        Map<String, Object> parameters = new HashMap<>();
        FundingListResponse response = new FundingListResponse();

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
        sql.append(" JOIN project_team_pucp ptp ON ptp.idProject = pf.idProject ");
        sql.append(" JOIN person pe ON ptp.idPerson = pe.idPerson ");
        sql.append(" WHERE pe.idPerson = :idPerson");

        parameters.put("idPerson", request.getIdPerson());

        if (StringUtils.hasText(request.getTitle())) {
            sql.append(" AND LOWER(f.Name) LIKE LOWER(CONCAT('%', :title, '%')) ");
            parameters.put("title", request.getTitle().toLowerCase());
        }

        if (StringUtils.hasText(request.getIdentifier())) {
            sql.append(" AND f.Identifier = :identifier ");
            parameters.put("identifier", request.getIdentifier());
        }

        if (StringUtils.hasText(request.getOrgUnit())) {
            sql.append(" AND fu.idOrgUnit = :orgUnit ");
            parameters.put("orgUnit", request.getOrgUnit());
        }

        if (StringUtils.hasText(request.getFundingType())) {
            sql.append(" AND f.idFunding_Type = :fundingType ");
            parameters.put("fundingType", request.getFundingType());
        }


        Query query = entityManager.createNativeQuery(sql.toString());
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        //Query query = entityManager.createNativeQuery(sql.toString());

        //query.setParameter("idPerson", request.getIdPerson());

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

                response.getResult().add(fundingListDto);
            }
        }
        else {
            response.setValues(ResultCodeEnum.NO_RESULTS.getCode(), ResultCodeEnum.NO_RESULTS.getMessage());
        }

        response.setTotal((long) response.getResult().size());

        return response;
    }

    @Override
    public ResearchGroupAuthorListResponse getResearchGroupListOfResearcher(ResearchGroupAuthorListRequest request) {
        validationUtils.validateKeyCode(request.getKeyCode());
        ResearchGroupAuthorListResponse response = new ResearchGroupAuthorListResponse();

        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT ");
        sql.append(" pa.idPerson_Role, ");
        sql.append(" pa.Status, ");
        sql.append(" org.idOrgUnit, ");
        sql.append(" org.Name, ");
        sql.append(" org.Acronym, ");
        sql.append(" org.PartOf, ");
        sql.append(" orggro.Status, ");
        sql.append(" orggro.Category, ");
        sql.append(" orggro.EvaluationYear ");


        sql.append(" FROM person_affiliation pa ");
        sql.append(" JOIN orgunit org ON pa.idOrgUnit = org.idOrgUnit ");
        sql.append(" JOIN orgunit_group orggro ON org.idOrgUnit = orggro.idOrgUnit ");
        sql.append(" WHERE org.idOrgUnit_Type = \"GRUPO\" ");
        sql.append(" AND pa.idPerson = :idPerson ");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("idPerson", request.getIdPerson());

        List<Object[]> resultList = query.getResultList();

        if(!CollectionUtils.isEmpty(resultList)){
            for (Object[] item : resultList) {
                ResearchGroupDto researchGroupDto = new ResearchGroupDto();

                researchGroupDto.setIdPersonRole(item[0] != null ? item[0].toString() : null);
                researchGroupDto.setStatus(item[1] != null ? item[1].toString() : null);
                researchGroupDto.setIdOrgUnit(item[2] != null ? item[2].toString() : null);
                researchGroupDto.setName(item[3] != null ? item[3].toString() : null);
                researchGroupDto.setAcronym(item[4] != null ? item[4].toString() : null);
                researchGroupDto.setPartOf(item[5] != null ? item[5].toString() : null);
                researchGroupDto.setStatusGroup(item[6] != null ? item[6].toString() : null);
                researchGroupDto.setCategoryGroup(item[7] != null ? item[7].toString() : null);
                researchGroupDto.setEvaluationYear(item[8] != null ? (Integer) item[8] : null);

                researchGroupDto.setNamePartOf(orgunitRepository.findByIdOrgUnit(researchGroupDto.getPartOf()).getName());

                response.getResult().add(researchGroupDto);
            }
        }
        else{
            response.setValues(ResultCodeEnum.NO_RESULTS.getCode(), ResultCodeEnum.NO_RESULTS.getMessage());
        }

        response.setTotal((long) response.getResult().size());

        return response;
    }

    @Override
    public ResearchGroupAuthorDetailResponse getResearchGroupDetailOfResearcher(ResearchGroupAuthorDetailRequest request) {
        validationUtils.validateKeyCode(request.getKeyCode());
        ResearchGroupAuthorDetailResponse response = new ResearchGroupAuthorDetailResponse();

        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT ");
        sql.append(" org.idOrgUnit, ");
        sql.append(" org.Name, ");
        sql.append(" org.Acronym, ");
        sql.append(" org.PartOf, ");
        sql.append(" orggro.Status, ");
        sql.append(" orggro.Category, ");
        sql.append(" orggro.EvaluationYear ");

        sql.append(" FROM orgunit org ");
        sql.append(" JOIN orgunit_group orggro ON org.idOrgUnit = orggro.idOrgUnit ");
        sql.append(" WHERE org.idOrgUnit = :idOrgUnit ");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("idOrgUnit", request.getIdOrgUnit());

        List<Object[]> resultList = query.getResultList();

        List<PublicationDto> publications;
        List<ProjectDto> projects;
        List<ResearchGroupEvaluationDetail> evaluationCategories;

        if(!CollectionUtils.isEmpty(resultList)){
            for (Object[] item : resultList) {
                ResearchGroupDetailDto researchGroupDto = new ResearchGroupDetailDto();

                researchGroupDto.setIdOrgUnit(item[0] != null ? item[0].toString() : null);
                researchGroupDto.setName(item[1] != null ? item[1].toString() : null);
                researchGroupDto.setAcronym(item[2] != null ? item[2].toString() : null);
                researchGroupDto.setPartOf(item[3] != null ? item[3].toString() : null);
                researchGroupDto.setStatusGroup(item[4] != null ? item[4].toString() : null);
                researchGroupDto.setCategoryGroup(item[5] != null ? item[5].toString() : null);
                researchGroupDto.setEvaluationYear(item[6] != null ? (Integer) item[6] : null);

                researchGroupDto.setNamePartOf(orgunitRepository.findByIdOrgUnit(researchGroupDto.getPartOf()).getName());

                researchGroupDto.setResearchersList(commonService.getResearchGroupTeam(researchGroupDto.getIdOrgUnit()));

                // Related publications
                publications = commonService.getPublicationListOfAResearchGroup(researchGroupDto.getIdOrgUnit());
                researchGroupDto.setRelatedPublications(publications);

                // Related projects
                projects = commonService.getProjectListOfAResearchGroup(researchGroupDto.getIdOrgUnit());
                researchGroupDto.setRelatedProjects(projects);

                // Evaluation detail
                evaluationCategories = commonService.getResearchGroupEvaluationDetail(researchGroupDto.getIdOrgUnit());
                researchGroupDto.setResearchGroupEvaluationDetail(evaluationCategories);

                response.setResearchGroupDetail(researchGroupDto);
            }
        }
        else{
            response.setValues(ResultCodeEnum.NO_RESULTS.getCode(), ResultCodeEnum.NO_RESULTS.getMessage());
        }

        return response;
    }

}
