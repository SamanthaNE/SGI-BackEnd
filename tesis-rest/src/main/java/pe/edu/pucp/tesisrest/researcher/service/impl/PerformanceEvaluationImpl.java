package pe.edu.pucp.tesisrest.researcher.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
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
        sql.append(" WHERE au.ScopusAuthorID = :scopusAuthorId ");
        sql.append(" ORDER BY pu.PublicationDate DESC ");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("scopusAuthorId", request.getScopusAuthorId());
        List<Object[]> resultList = query.getResultList();

        PublicationAuthorListResponse response = new PublicationAuthorListResponse();
        List<AuthorResearcherDto> authors;

        if(!CollectionUtils.isEmpty(resultList)){
            for (Object[] item : resultList) {
                PublicationDto pubAuthorDto = new PublicationDto();

                pubAuthorDto.setPublicationId(item[0] != null ? Long.valueOf(item[0].toString()) : null);
                pubAuthorDto.setTitle(item[1] != null ? item[1].toString() : null);
                pubAuthorDto.setPublishedIn(item[2] != null ? item[2].toString() : null);

                if (item[3] != null) {
                    LocalDate publicationDate = LocalDate.parse(item[3].toString());
                    pubAuthorDto.setPublicationDate(publicationDate);
                } else {
                    pubAuthorDto.setPublicationDate(null);
                }
                //pubAuthorDto.setPublicationDate(item[3] != null ? (Date) item[3] : null);
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

        PublicationAuthorDetailResponse response = new PublicationAuthorDetailResponse();
        List<AuthorResearcherDto> authors;
        List<ProjectDto> projects;

        List<Object[]> resultList = query.getResultList();

        if(!CollectionUtils.isEmpty(resultList)){
            for (Object[] item : resultList) {
                PublicationDetailDto pubAuthorDto = new PublicationDetailDto();

                pubAuthorDto.setPublicationId(item[0] != null ? Long.valueOf(item[0].toString()) : null);
                pubAuthorDto.setTitle(item[1] != null ? item[1].toString() : null);
                pubAuthorDto.setPublishedIn(item[2] != null ? item[2].toString() : null);

                if (item[3] != null) {
                    LocalDate publicationDate = LocalDate.parse(item[3].toString());
                    pubAuthorDto.setPublicationDate(publicationDate);
                } else {
                    pubAuthorDto.setPublicationDate(null);
                }

                //pubAuthorDto.setPublicationDate(item[3] != null ? (Date) item[3] : null);
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

        Query query = entityManager.createNativeQuery(sql.toString());

        query.setParameter("idPerson", request.getIdPerson());

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

                if(item[5] != null){
                    System.out.println("CONCYTEC :" + item[5].toString());
                    if (Objects.equals(item[5].toString(), "POR_INICIAR")) {
                        proAuthorDto.setIdProjectStatusTypeCONCYTEC("Por uniciar");
                    } else {
                        if (Objects.equals(item[5].toString(), "EN_EJECUCION")) {
                            proAuthorDto.setIdProjectStatusTypeCONCYTEC("En ejecución");
                        } else {
                            if (Objects.equals(item[5].toString(), "EN_PROCESO_CIERRRE")) {
                                proAuthorDto.setIdProjectStatusTypeCONCYTEC("En proceso de cierre");
                            } else {
                                if (Objects.equals(item[5].toString(), "CERRADO")) {
                                    proAuthorDto.setIdProjectStatusTypeCONCYTEC("Cerrado");
                                } else {
                                    if (Objects.equals(item[5].toString(), "CANCELADO")) {
                                        proAuthorDto.setIdProjectStatusTypeCONCYTEC("Cancelado");
                                    } else {
                                        proAuthorDto.setIdProjectStatusTypeCONCYTEC(item[5].toString());
                                    }
                                }
                            }
                        }
                    }
                }

                proAuthorDto.setIdProjectStatusTypeCONCYTEC(item[5] != null ? item[5].toString() : null);

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
        sql.append(" orggro.Category ");

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
        sql.append(" orggro.Category ");

        sql.append(" FROM orgunit org ");
        sql.append(" JOIN orgunit_group orggro ON org.idOrgUnit = orggro.idOrgUnit ");
        sql.append(" WHERE org.idOrgUnit = :idOrgUnit ");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("idOrgUnit", request.getIdOrgUnit());

        List<Object[]> resultList = query.getResultList();

        if(!CollectionUtils.isEmpty(resultList)){
            for (Object[] item : resultList) {
                ResearchGroupDetailDto researchGroupDto = new ResearchGroupDetailDto();

                researchGroupDto.setIdOrgUnit(item[0] != null ? item[0].toString() : null);
                researchGroupDto.setName(item[1] != null ? item[1].toString() : null);
                researchGroupDto.setAcronym(item[2] != null ? item[2].toString() : null);
                researchGroupDto.setPartOf(item[3] != null ? item[3].toString() : null);
                researchGroupDto.setStatusGroup(item[4] != null ? item[4].toString() : null);
                researchGroupDto.setCategoryGroup(item[5] != null ? item[5].toString() : null);

                researchGroupDto.setNamePartOf(orgunitRepository.findByIdOrgUnit(researchGroupDto.getPartOf()).getName());

                researchGroupDto.setResearchersList(commonService.getResearchGroupTeam(researchGroupDto.getIdOrgUnit()));

                // Publicaciones relacionadas
                // Projectos relacionados

                response.setResearchGroupDetail(researchGroupDto);
            }
        }
        else{
            response.setValues(ResultCodeEnum.NO_RESULTS.getCode(), ResultCodeEnum.NO_RESULTS.getMessage());
        }

        return response;
    }

}
