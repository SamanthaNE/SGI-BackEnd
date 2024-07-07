package pe.edu.pucp.tesisrest.worker.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import pe.edu.pucp.tesisrest.common.dto.*;
import pe.edu.pucp.tesisrest.common.enums.ResultCodeEnum;
import pe.edu.pucp.tesisrest.common.service.CommonService;
import pe.edu.pucp.tesisrest.common.util.QueryUtils;
import pe.edu.pucp.tesisrest.common.util.ValidationUtils;
import pe.edu.pucp.tesisrest.researcher.dto.AuthorResearcherDto;
import pe.edu.pucp.tesisrest.worker.dto.request.ProjectDetailRequest;
import pe.edu.pucp.tesisrest.worker.dto.request.ProjectListRequest;
import pe.edu.pucp.tesisrest.worker.dto.request.PublicationDetailRequest;
import pe.edu.pucp.tesisrest.worker.dto.request.PublicationListRequest;
import pe.edu.pucp.tesisrest.worker.dto.response.ProjectDetailResponse;
import pe.edu.pucp.tesisrest.worker.dto.response.ProjectListResponse;
import pe.edu.pucp.tesisrest.worker.dto.response.PublicationDetailResponse;
import pe.edu.pucp.tesisrest.worker.dto.response.PublicationListResponse;
import pe.edu.pucp.tesisrest.worker.service.ScientificProductionService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class ScientificProductionImpl implements ScientificProductionService {

    private final ValidationUtils validationUtils;
    private final QueryUtils queryUtils;
    private final EntityManager entityManager;
    private final CommonService commonService;

    @Override
    public PublicationListResponse getPublicationList(PublicationListRequest request) {
        validationUtils.validateKeyCode(request.getKeyCode());
        Map<String, Object> parameters = new HashMap<>();

        PublicationListResponse response = new PublicationListResponse();
        List<AuthorResearcherDto> authors;

        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT ");
        sql.append(" pu.idPublication, ");
        sql.append(" pu.Title, ");
        sql.append(" pu.PublishedInDesc, ");
        sql.append(" CASE WHEN pu.PublicationDate REGEXP '^(19|20)[0-9]{2}-(0[1-9]|1[0-2])-([0-2][0-9]|3[01])$' THEN pu.PublicationDate ELSE NULL END AS PublicationDate, ");
        sql.append(" rtc.idResource_Type_COAR, ");
        sql.append(" rtc.Nombre ");

        sql.append(" FROM publication pu ");
        sql.append(" JOIN resource_type_coar rtc ON pu.idResource_Type_COAR = rtc.idResource_Type_COAR ");

        if (StringUtils.hasText(request.getTitle())) {
            if(parameters.isEmpty()){
                sql.append(" WHERE LOWER(pu.Title) LIKE LOWER(CONCAT('%', :title, '%')) ");
            } else{
                sql.append(" AND LOWER(pu.Title) LIKE LOWER(CONCAT('%', :title, '%')) ");
            }

            parameters.put("title", request.getTitle().toLowerCase());
        }

        if (StringUtils.hasText(request.getPublishedIn())) {
            if(parameters.isEmpty()){
                sql.append(" WHERE LOWER(pu.PublishedInDesc) LIKE LOWER(CONCAT('%', :publishedIn, '%')) ");
            } else {
                sql.append(" AND LOWER(pu.PublishedInDesc) LIKE LOWER(CONCAT('%', :publishedIn, '%')) ");
            }

            parameters.put("publishedIn", request.getPublishedIn().toLowerCase());
        }

        if (StringUtils.hasText(request.getResourceType())) {
            if(parameters.isEmpty()){
                sql.append(" WHERE rtc.idResource_Type_COAR = :resourceType ");
            } else {
                sql.append(" AND rtc.idResource_Type_COAR = :resourceType ");
            }

            parameters.put("resourceType", request.getResourceType());
        }

        if (StringUtils.hasText(request.getYear())) {
            if(parameters.isEmpty()){
                sql.append(" WHERE EXTRACT(YEAR FROM pu.PublicationDate) = :year ");
            } else {
                sql.append(" AND EXTRACT(YEAR FROM pu.PublicationDate) = :year ");
            }

            parameters.put("year", request.getYear());
        }

        sql.append(" ORDER BY pu.PublicationDate DESC");

        Query query = entityManager.createNativeQuery(sql.toString());
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

/*
        sql.append(" ORDER BY pu.PublicationDate DESC;");

        Query query = entityManager.createNativeQuery(sql.toString());
        Set<Map.Entry<String, Object>> entrySet = parameters.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
*/
        queryUtils.setPagination(query, request.getPage(), request.getSize());

        List<Object[]> resultList = query.getResultList();

        if(!CollectionUtils.isEmpty(resultList)){
            for (Object[] item : resultList) {
                PublicationDto publicationDto = new PublicationDto();

                publicationDto.setPublicationId(item[0] != null ? Long.valueOf(item[0].toString()) : null);
                publicationDto.setTitle(item[1] != null ? item[1].toString() : null);
                publicationDto.setPublishedIn(item[2] != null ? item[2].toString() : null);
                publicationDto.setPublicationDate(item[3] != null ? (Date) item[3] : null);

                publicationDto.setIdResourceTypeCOAR(item[4] != null ? item[4].toString() : null);
                publicationDto.setResourceTypeCOARName(item[5] != null ? item[5].toString() : null);

                authors = commonService.getAuthorsOfPublication(publicationDto.getPublicationId());

                publicationDto.setAuthorsList(authors);

                response.getPublications().add(publicationDto);
            }
        }
        else{
            response.setValues(ResultCodeEnum.NO_RESULTS.getCode(), ResultCodeEnum.NO_RESULTS.getMessage());
        }

        response.setTotal((long) response.getPublications().size());

        return response;
    }

    @Override
    public PublicationDetailResponse getPublicationDetailById(PublicationDetailRequest request) {
        validationUtils.validateKeyCode(request.getKeyCode());

        PublicationDetailResponse response = new PublicationDetailResponse();
        List<AuthorResearcherDto> authors;
        List<ProjectDto> projects;
        List<EvaluationDetailDto> evaluationDetail;
        List<ResearchGroupSciProdDetailDto> researchGroups;

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

        List<Object[]> resultList = query.getResultList();

        if(!CollectionUtils.isEmpty(resultList)){
            for (Object[] item : resultList) {
                PublicationDetailDto publicationDto = new PublicationDetailDto();

                publicationDto.setPublicationId(item[0] != null ? Long.valueOf(item[0].toString()) : null);
                publicationDto.setTitle(item[1] != null ? item[1].toString() : null);
                publicationDto.setPublishedIn(item[2] != null ? item[2].toString() : null);
                publicationDto.setPublicationDate(item[3] != null ? (Date) item[3] : null);

                publicationDto.setIdResourceTypeCOAR(item[4] != null ? item[4].toString() : null);
                publicationDto.setResourceTypeCOARName(item[5] != null ? item[5].toString() : null);
                publicationDto.setAbstractPublication(item[6] != null ? item[6].toString() : null);
                publicationDto.setVolume(item[7] != null ? item[7].toString() : null);
                publicationDto.setStartPage(item[8] != null ? item[8].toString() : null);
                publicationDto.setEndPage(item[9] != null ? item[9].toString() : null);

                authors = commonService.getAuthorsOfPublication(publicationDto.getPublicationId());
                publicationDto.setAuthorsList(authors);

                projects = commonService.getProjectsRelatedToPublication(publicationDto.getPublicationId());
                publicationDto.setRelatedProjects(projects);

                evaluationDetail = commonService.getEvaluationDetailOfPublication(publicationDto.getPublicationId());
                if (evaluationDetail != null && !evaluationDetail.isEmpty()) {
                    publicationDto.setEvaluationDetail(evaluationDetail.getFirst());
                } else {
                    publicationDto.setEvaluationDetail(null);
                }

                researchGroups = commonService.getResearchGroupNamesOfPublication(publicationDto.getPublicationId());
                publicationDto.setResearchGroups(researchGroups);

                response.setPublicationDetail(publicationDto);
            }
        }
        else{
            response.setValues(ResultCodeEnum.NO_RESULTS.getCode(), ResultCodeEnum.NO_RESULTS.getMessage());
        }

        return response;
    }

    @Override
    public ProjectListResponse getProjectList(ProjectListRequest request) {
        validationUtils.validateKeyCode(request.getKeyCode());
        Map<String, Object> parameters = new HashMap<>();

        ProjectListResponse response = new ProjectListResponse();
        List<FundingListDto> fundings;

        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT ");
        sql.append(" pro.idProject, ");
        sql.append(" pro.Title, ");
        sql.append(" pro.Abstract, ");
        sql.append(" pro.StartDate, ");
        sql.append(" pro.EndDate, ");
        sql.append(" pro.idProject_Status_Type_CONCYTEC ");

        sql.append(" FROM project pro ");

        if (StringUtils.hasText(request.getTitle())) {
            if(parameters.isEmpty()){
                sql.append(" WHERE LOWER(pro.title) LIKE LOWER(CONCAT('%', :title, '%')) ");
            } else {
                sql.append(" AND LOWER(pro.title) LIKE LOWER(CONCAT('%', :title, '%')) ");
            }

            parameters.put("title", request.getTitle().toLowerCase());
        }

        if (StringUtils.hasText(request.getStatus())) {
            if(parameters.isEmpty()) {
                sql.append(" WHERE pro.idProject_Status_Type_CONCYTEC = :status ");
            } else {
                sql.append(" AND pro.idProject_Status_Type_CONCYTEC = :status ");
            }

            parameters.put("status", request.getStatus());
        }

        if (StringUtils.hasText(request.getStartDate())) {
            DateTimeFormatter DateTimeFormatter = null;
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDate = LocalDate.parse(request.getStartDate(), inputFormatter);
            String formattedStartDate = startDate.format(outputFormatter);

            if(parameters.isEmpty()) {
                sql.append(" WHERE pro.StartDate >= :startDate ");
            }
            else{
                sql.append(" AND pro.StartDate >= :startDate ");
            }

            parameters.put("startDate", formattedStartDate);
        }

        if (StringUtils.hasText(request.getEndDate())) {
            DateTimeFormatter DateTimeFormatter = null;
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDate = LocalDate.parse(request.getEndDate(), inputFormatter);
            String formattedStartDate = startDate.format(outputFormatter);

            if(parameters.isEmpty()) {
                sql.append(" WHERE pro.EndDate <= :endDate ");
            } else {
                sql.append(" AND pro.EndDate <= :EndDate ");
            }

            parameters.put("endDate", formattedStartDate);
        }

        sql.append(" ORDER BY pro.StartDate DESC");

        Query query = entityManager.createNativeQuery(sql.toString());
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        queryUtils.setPagination(query, request.getPage(), request.getSize());

        List<Object[]> resultList = query.getResultList();

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

                fundings = commonService.getFundingsOfProject(projectDto.getIdProject());

                projectDto.setRelatedFundingList(fundings);

                response.getProjects().add(projectDto);
            }
        }
        else{
            response.setValues(ResultCodeEnum.NO_RESULTS.getCode(), ResultCodeEnum.NO_RESULTS.getMessage());
        }

        response.setTotal((long) response.getProjects().size());

        return response;
    }

    @Override
    public ProjectDetailResponse getProjectDetailById(ProjectDetailRequest request) {
        validationUtils.validateKeyCode(request.getKeyCode());

        ProjectDetailResponse response = new ProjectDetailResponse();
        List<FundingListDto> fundings;
        List<ResearcherDto> researchers;
        List<EvaluationDetailDto> evaluationDetail;
        List<ResearchGroupSciProdDetailDto> researchGroupList;

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

            response.setProjectDetailDto(proDetailDto);
        } catch (NoResultException e) {
            response.setValues(ResultCodeEnum.NO_RESULTS.getCode(), ResultCodeEnum.NO_RESULTS.getMessage());
        }

        return response;
    }
}
