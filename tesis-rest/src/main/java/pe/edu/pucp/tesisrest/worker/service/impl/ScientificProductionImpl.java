package pe.edu.pucp.tesisrest.worker.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
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
        sql.append(" pu.PublicationDate, ");
        sql.append(" rtc.idResource_Type_COAR, ");
        sql.append(" rtc.Nombre ");

        sql.append(" FROM publication pu ");
        sql.append(" JOIN publication_author au ON pu.idPublication = au.idPublication ");
        sql.append(" JOIN resource_type_coar rtc ON pu.idResource_Type_COAR = rtc.idResource_Type_COAR ");
        sql.append(" ORDER BY pu.PublicationDate DESC;");

        Query query = entityManager.createNativeQuery(sql.toString());
        Set<Map.Entry<String, Object>> entrySet = parameters.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        queryUtils.setPagination(query, request.getPage(), request.getSize());

        List<Object[]> resultList = query.getResultList();

        if(!CollectionUtils.isEmpty(resultList)){
            for (Object[] item : resultList) {
                PublicationDto publicationDto = new PublicationDto();

                publicationDto.setPublicationId(item[0] != null ? Long.valueOf(item[0].toString()) : null);
                publicationDto.setTitle(item[1] != null ? item[1].toString() : null);
                publicationDto.setPublishedIn(item[2] != null ? item[2].toString() : null);
                publicationDto.setPublicationDate(item[3] != null ? item[3].toString() : null);
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

        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT ");
        sql.append(" pu.idPublication, ");
        sql.append(" pu.Title, ");
        sql.append(" pu.PublishedInDesc, ");
        sql.append(" pu.PublicationDate, ");
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
                publicationDto.setPublicationDate(item[3] != null ? item[3].toString() : null);
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
        sql.append(" JOIN project_team_pucp ptp ON ptp.idProject = pro.idProject ");
        sql.append(" JOIN person pe ON ptp.idPerson = pe.idPerson ");
        sql.append(" ORDER BY pro.StartDate DESC");

        Query query = entityManager.createNativeQuery(sql.toString());
        Set<Map.Entry<String, Object>> entrySet = parameters.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        queryUtils.setPagination(query, request.getPage(), request.getSize());

        List<Object[]> resultList = query.getResultList();

        if(!CollectionUtils.isEmpty(resultList)){
            for (Object[] item : resultList) {
                ProjectDto proAuthorDto = new ProjectDto();

                proAuthorDto.setIdProject(item[0] != null ? Long.valueOf(item[0].toString()) : null);
                proAuthorDto.setTitle(item[1] != null ? item[1].toString() : null);
                proAuthorDto.setDescription(item[2] != null ? item[2].toString() : null);
                proAuthorDto.setStartDate(item[3] != null ? (Date) item[3] : null);
                proAuthorDto.setEndDate(item[4] != null ? (Date) item[4] : null);
                proAuthorDto.setIdProjectStatusTypeCONCYTEC(item[5] != null ? item[5].toString() : null);

                fundings = commonService.getFundingsOfProject(proAuthorDto.getIdProject());

                proAuthorDto.setRelatedFundingList(fundings);

                response.getProjects().add(proAuthorDto);
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
            proDetailDto.setIdProjectStatusTypeCONCYTEC(result[5] != null ? (String) result[5] : null);

            fundings = commonService.getFundingsOfProject(proDetailDto.getIdProject());
            proDetailDto.setRelatedFundingList(fundings);

            researchers = commonService.getProjectTeam(proDetailDto.getIdProject());
            proDetailDto.setResearchers(researchers);

            response.setProjectDetailDto(proDetailDto);
        } catch (NoResultException e) {
            response.setValues(ResultCodeEnum.NO_RESULTS.getCode(), ResultCodeEnum.NO_RESULTS.getMessage());
        }

        return response;
    }
}
