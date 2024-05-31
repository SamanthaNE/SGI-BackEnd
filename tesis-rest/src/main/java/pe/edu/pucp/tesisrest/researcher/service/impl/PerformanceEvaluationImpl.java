package pe.edu.pucp.tesisrest.researcher.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import pe.edu.pucp.tesisrest.common.enums.ResultCodeEnum;
import pe.edu.pucp.tesisrest.common.util.ValidationUtils;
import pe.edu.pucp.tesisrest.researcher.dto.AuthorResearcherDto;
import pe.edu.pucp.tesisrest.researcher.dto.PublicationAuthorDetailDto;
import pe.edu.pucp.tesisrest.researcher.dto.PublicationAuthorDto;
import pe.edu.pucp.tesisrest.researcher.dto.request.PublicationAuthorDetailRequest;
import pe.edu.pucp.tesisrest.researcher.dto.request.PublicationAuthorListRequest;
import pe.edu.pucp.tesisrest.researcher.dto.response.PublicationAuthorDetailResponse;
import pe.edu.pucp.tesisrest.researcher.dto.response.PublicationAuthorListResponse;
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
        sql.append(" ORDER BY sp.cover_date ");

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
        Map<String, Object> parameters = new HashMap<>();
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT ");
        sql.append(" pu.idPublication, ");
        sql.append(" pu.Title, ");
        sql.append(" pu.PublishedInDesc, ");
        sql.append(" pu.PublicationDate, ");
        sql.append(" rtc.idResource_Type_COAR, ");
        sql.append(" rtc.Nombre ");
        sql.append(" pu.Abstract, ");
        sql.append(" pu.Volume, ");
        sql.append(" pu.StartPage, ");
        sql.append(" pu.EndPage, ");

        sql.append(" FROM publication pu ");
        sql.append(" JOIN publication_author au ON pu.idPublication = au.idPublication ");
        sql.append(" JOIN resource_type_coar rtc ON pu.idResource_Type_COAR = rtc.idResource_Type_COAR ");
        sql.append(" WHERE pe.idPublication = :idPublication ");

        parameters.put("idPublication", request.getPublicationId());

        Query query = entityManager.createNativeQuery(sql.toString());
        Set<Map.Entry<String, Object>> entrySet = parameters.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        PublicationAuthorDetailResponse response = new PublicationAuthorDetailResponse();
        List<AuthorResearcherDto> authors;

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

                response.setPublicationAuthorDetail(pubAuthorDto);
            }
        }
        else{
            response.setValues(ResultCodeEnum.NO_RESULTS.getCode(), ResultCodeEnum.NO_RESULTS.getMessage());
        }

        return response;
    }

    private List<AuthorResearcherDto> getAuthorsOfPublication(Long publicationId){
        StringBuilder sql = new StringBuilder();
        Map<String, Object> parameters = new HashMap<>();

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

        parameters.put("publication_id", publicationId);

        Query query = entityManager.createNativeQuery(sql.toString());
        Set<Map.Entry<String, Object>> entrySet = parameters.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        List<Object[]> resultList = query.getResultList();

        if(!CollectionUtils.isEmpty(resultList)){
            for (Object[] item : resultList) {
                AuthorResearcherDto authorDto = new AuthorResearcherDto();

                authorDto.setIdPerson(item[0] != null ? Long.valueOf(item[0].toString()) : null);
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
