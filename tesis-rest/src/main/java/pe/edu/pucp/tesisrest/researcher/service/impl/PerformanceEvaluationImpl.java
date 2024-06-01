package pe.edu.pucp.tesisrest.researcher.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import pe.edu.pucp.tesisrest.common.enums.ResultCodeEnum;
import pe.edu.pucp.tesisrest.common.util.ValidationUtils;
import pe.edu.pucp.tesisrest.researcher.dto.*;
import pe.edu.pucp.tesisrest.researcher.dto.request.*;
import pe.edu.pucp.tesisrest.researcher.dto.response.*;
import pe.edu.pucp.tesisrest.researcher.service.PerformanceEvaluationService;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class PerformanceEvaluationImpl implements PerformanceEvaluationService {

    private final ValidationUtils validationUtils;
    private final EntityManager entityManager;

    @Override
    public PublicationAuthorListResponse getPublicationAuthorList(PublicationAuthorListRequest request) {
        validationUtils.validateKeyCode(request.getKeyCode());
        Map<String, Object> parameters = new HashMap<>();
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

        parameters.put("scopusAuthorId", request.getScopusAuthorId());

        Query query = entityManager.createNativeQuery(sql.toString());
        Set<Map.Entry<String, Object>> entrySet = parameters.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        PublicationAuthorListResponse response = new PublicationAuthorListResponse();
        List<AuthorResearcherDto> authors;

        List<Object[]> resultList = query.getResultList();

        if(!CollectionUtils.isEmpty(resultList)){
            for (Object[] item : resultList) {
                PublicationAuthorDto pubAuthorDto = new PublicationAuthorDto();

                pubAuthorDto.setPublicationId(item[0] != null ? Long.valueOf(item[0].toString()) : null);
                pubAuthorDto.setTitle(item[1] != null ? item[1].toString() : null);
                pubAuthorDto.setPublishedIn(item[2] != null ? item[2].toString() : null);
                pubAuthorDto.setPublicationDate(item[3] != null ? item[3].toString() : null);
                pubAuthorDto.setIdResourceTypeCOAR(item[4] != null ? item[4].toString() : null);
                pubAuthorDto.setResourceTypeCOARName(item[5] != null ? item[5].toString() : null);

                authors = getAuthorsOfPublication(pubAuthorDto.getPublicationId());

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
        List<ProjectAuthorDto> projects;

        List<Object[]> resultList = query.getResultList();

        if(!CollectionUtils.isEmpty(resultList)){
            for (Object[] item : resultList) {
                PublicationAuthorDetailDto pubAuthorDto = new PublicationAuthorDetailDto();

                pubAuthorDto.setPublicationId(item[0] != null ? Long.valueOf(item[0].toString()) : null);
                pubAuthorDto.setTitle(item[1] != null ? item[1].toString() : null);
                pubAuthorDto.setPublishedIn(item[2] != null ? item[2].toString() : null);
                pubAuthorDto.setPublicationDate(item[3] != null ? item[3].toString() : null);
                pubAuthorDto.setIdResourceTypeCOAR(item[4] != null ? item[4].toString() : null);
                pubAuthorDto.setResourceTypeCOARName(item[5] != null ? item[5].toString() : null);
                pubAuthorDto.setAbstractPublication(item[6] != null ? item[6].toString() : null);
                pubAuthorDto.setVolume(item[7] != null ? item[7].toString() : null);
                pubAuthorDto.setStartPage(item[8] != null ? item[8].toString() : null);
                pubAuthorDto.setEndPage(item[9] != null ? item[9].toString() : null);

                authors = getAuthorsOfPublication(pubAuthorDto.getPublicationId());
                pubAuthorDto.setAuthorsList(authors);

                projects = getRelatedProjects(pubAuthorDto.getPublicationId());
                pubAuthorDto.setRelatedProjects(projects);

                response.setPublicationAuthorDetail(pubAuthorDto);
            }
        }
        else{
            response.setValues(ResultCodeEnum.NO_RESULTS.getCode(), ResultCodeEnum.NO_RESULTS.getMessage());
        }

        return response;
    }

    private List<ProjectAuthorDto> getRelatedProjects(Long publicationId) {
        List<ProjectAuthorDto> projects = new ArrayList<>();
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
                ProjectAuthorDto projectDto = new ProjectAuthorDto();

                projectDto.setIdProject(item[0] != null ? Long.valueOf(item[0].toString()) : null);
                projectDto.setTitle(item[1] != null ? item[1].toString() : null);
                projectDto.setDescription(item[2] != null ? item[2].toString() : null);
                projectDto.setStartDate(item[3] != null ? item[3].toString() : null);
                projectDto.setEndDate(item[4] != null ? item[4].toString() : null);
                projectDto.setIdProjectStatusTypeCONCYTEC(item[5] != null ? item[5].toString() : null);

                fundings = getFundingsOfProject(projectDto.getIdProject());

                projectDto.setRelatedFundingList(fundings);

                projects.add(projectDto);
            }
        }

        return projects;
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
                ProjectAuthorDto proAuthorDto = new ProjectAuthorDto();

                proAuthorDto.setIdProject(item[0] != null ? Long.valueOf(item[0].toString()) : null);
                proAuthorDto.setTitle(item[1] != null ? item[1].toString() : null);
                proAuthorDto.setDescription(item[2] != null ? item[2].toString() : null);
                proAuthorDto.setStartDate(item[3] != null ? item[3].toString() : null);
                proAuthorDto.setEndDate(item[4] != null ? item[4].toString() : null);
                proAuthorDto.setIdProjectStatusTypeCONCYTEC(item[5] != null ? item[5].toString() : null);

                fundings = getFundingsOfProject(proAuthorDto.getIdProject());

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
            ProjectAuthorDetailDto proDetailDto = new ProjectAuthorDetailDto();

            proDetailDto.setIdProject(result[0] != null ? Long.valueOf(result[0].toString()) : null);
            proDetailDto.setTitle(result[1] != null ? (String) result[1] : null);
            proDetailDto.setDescription(result[2] != null ? (String) result[2] : null);
            proDetailDto.setStartDate(result[3] != null ? (String) result[3] : null);
            proDetailDto.setEndDate(result[4] != null ? (String) result[4] : null);
            proDetailDto.setIdProjectStatusTypeCONCYTEC(result[5] != null ? (String) result[5] : null);

            fundings = getFundingsOfProject(proDetailDto.getIdProject());
            proDetailDto.setRelatedFundingList(fundings);

            researchers = getProjectTeam(proDetailDto.getIdProject());
            proDetailDto.setResearchers(researchers);

            response.setProjectAuthorDetail(proDetailDto);
        } catch (NoResultException e) {
            response.setValues(ResultCodeEnum.NO_RESULTS.getCode(), ResultCodeEnum.NO_RESULTS.getMessage());
        }

        return response;
    }

    private List<ResearcherDto> getProjectTeam(Long idProject) {
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

                researcherDto.setIdRolePerson(item[0] != null ? item[0].toString() : null);
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
    public FundingListResponse getFundingRelatedList(FundingListRequest request) {
        validationUtils.validateKeyCode(request.getKeyCode());
        FundingListResponse response = new FundingListResponse();

        if(!getFundingsOfProject(request.getIdProject()).isEmpty()){
            response.setResult(getFundingsOfProject(request.getIdProject()));
        }
        else{
            response.setValues(ResultCodeEnum.NO_RESULTS.getCode(), ResultCodeEnum.NO_RESULTS.getMessage());
        }

        response.setTotal((long) response.getResult().size());

        return response;
    }

    private List<FundingListDto> getFundingsOfProject(Long idProject){
        List<FundingListDto> funding = new ArrayList<>();
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT ");
        sql.append(" pf.idFunding, ");
        sql.append(" pf.FundedAs, ");
        sql.append(" pf.Categoria, ");
        sql.append(" f.CurrCode, ");
        sql.append(" f.Amount, ");
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
                fundingListDto.setFundingType(item[5] != null ? item[5].toString() : null);
                fundingListDto.setFundedBy(item[6] != null ? item[6].toString() : null);

                funding.add(fundingListDto);
            }
        }

        return funding;
    }

    private List<AuthorResearcherDto> getAuthorsOfPublication(Long publicationId){
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
}
