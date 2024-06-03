package pe.edu.pucp.tesisrest.common.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import pe.edu.pucp.tesisrest.common.dto.FundingListDto;
import pe.edu.pucp.tesisrest.common.dto.ProjectDto;
import pe.edu.pucp.tesisrest.common.dto.ResearcherDto;
import pe.edu.pucp.tesisrest.common.service.CommonService;
import pe.edu.pucp.tesisrest.common.util.ValidationUtils;
import pe.edu.pucp.tesisrest.researcher.dto.AuthorResearcherDto;

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
                projectDto.setIdProjectStatusTypeCONCYTEC(item[5] != null ? item[5].toString() : null);

                fundings = getFundingsOfProject(projectDto.getIdProject());

                projectDto.setRelatedFundingList(fundings);

                projects.add(projectDto);
            }
        }

        return projects;
    }
}
