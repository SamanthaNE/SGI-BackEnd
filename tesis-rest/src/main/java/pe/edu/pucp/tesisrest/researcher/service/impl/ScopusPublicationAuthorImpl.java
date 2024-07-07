package pe.edu.pucp.tesisrest.researcher.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import pe.edu.pucp.tesisrest.common.dto.ResearchGroupDto;
import pe.edu.pucp.tesisrest.researcher.dto.ScopusAuthorDto;
import pe.edu.pucp.tesisrest.researcher.dto.ScopusPublicationAuthorDto;
import pe.edu.pucp.tesisrest.researcher.dto.request.ResearchGroupAuthorListRequest;
import pe.edu.pucp.tesisrest.researcher.dto.request.ScopusPublicationAuthorListRequest;
import pe.edu.pucp.tesisrest.researcher.dto.request.ScopusPublicationRequest;
import pe.edu.pucp.tesisrest.researcher.dto.response.ResearchGroupAuthorListResponse;
import pe.edu.pucp.tesisrest.researcher.dto.response.ScopusPublicationAuthorListResponse;
import pe.edu.pucp.tesisrest.researcher.dto.response.ScopusPublicationResponse;
import pe.edu.pucp.tesisrest.common.enums.ResultCodeEnum;
import pe.edu.pucp.tesisrest.common.model.scopus.ScopusAuthor;
import pe.edu.pucp.tesisrest.researcher.repository.scopus.ScopusAuthorRepository;
import pe.edu.pucp.tesisrest.researcher.service.ScopusPublicationAuthorService;
import pe.edu.pucp.tesisrest.common.util.ValidationUtils;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class ScopusPublicationAuthorImpl implements ScopusPublicationAuthorService {

    private final ScopusAuthorRepository scopusAuthorRepository;
    private final ValidationUtils validationUtils;
    private final EntityManager entityManager;

    @Override
    public ScopusPublicationAuthorListResponse searchScopusPublicationOfAuthor(ScopusPublicationAuthorListRequest request) {
        validationUtils.validateKeyCode(request.getKeyCode());
        Map<String, Object> parameters = new HashMap<>();
        StringBuilder sql = new StringBuilder();

        Optional<ScopusAuthor> optionalAuthor = scopusAuthorRepository.findByAuthid(request.getAuthid());
        ScopusPublicationAuthorListResponse response = new ScopusPublicationAuthorListResponse();
        List<ScopusAuthorDto> authors;

        if (optionalAuthor.isPresent()) {
            ScopusAuthor author = optionalAuthor.get();
            System.out.println("Found author: " + author.getScopusAuthorId());

            sql.append(" SELECT ");
            sql.append(" sp.scopus_publication_id, ");
            sql.append(" sp.title, ");
            sql.append(" sp.publication_name, ");
            sql.append(" sp.cover_date, ");
            sql.append(" sp.sub_type_description, ");
            sql.append(" sp.aggregation_type ");

            sql.append(" FROM scopus_publication sp ");
            sql.append(" JOIN scopus_author_publication sap ON sp.scopus_publication_id = sap.scopus_publication_id ");
            sql.append(" WHERE sap.scopus_author_id = :author_id ");
            sql.append(" AND sap.estado = 1");

            parameters.put("author_id", author.getScopusAuthorId());

            if (StringUtils.hasText(request.getTitle())) {
                sql.append(" AND LOWER(sp.title) LIKE LOWER(CONCAT('%', :title, '%')) ");
                parameters.put("title", request.getTitle().toLowerCase());
            }

            if (StringUtils.hasText(request.getPublisher())) {
                sql.append(" AND LOWER(sp.publication_name) LIKE LOWER(CONCAT('%', :publicationName, '%')) ");
                parameters.put("publicationName", request.getPublisher().toLowerCase());
            }

            if (StringUtils.hasText(request.getSubTypeDescription())) {
                sql.append(" AND sp.sub_type_description = :subTypeDescription ");
                parameters.put("subTypeDescription", request.getSubTypeDescription());
            }

            if (StringUtils.hasText(request.getAggregationType())) {
                sql.append(" AND sp.aggregation_type = :aggregationType ");
                parameters.put("aggregationType", request.getAggregationType());
            }

            if (StringUtils.hasText(request.getYear())) {
                sql.append(" AND EXTRACT(YEAR FROM sp.cover_date) = :year ");
                parameters.put("year", request.getYear());
            }

            sql.append(" ORDER BY sp.cover_date DESC");

            Query query = entityManager.createNativeQuery(sql.toString());
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }

            List<Object[]> resultList = query.getResultList();

            if(!CollectionUtils.isEmpty(resultList)){
                for (Object[] item : resultList) {
                    ScopusPublicationAuthorDto pubAuthorDto = new ScopusPublicationAuthorDto();

                    pubAuthorDto.setScopusPublicationId(item[0] != null ? Long.valueOf(item[0].toString()) : null);
                    pubAuthorDto.setTitle(item[1] != null ? item[1].toString() : null);
                    pubAuthorDto.setPublicationName(item[2] != null ? item[2].toString() : null);
                    pubAuthorDto.setCoverDate(item[3] != null ? item[3].toString() : null);
                    pubAuthorDto.setSubTypeDescription(item[4] != null ? item[4].toString() : null);
                    pubAuthorDto.setAggregationType(item[5] != null ? item[5].toString() : null);

                    authors = getAuthorsOfPublication(pubAuthorDto.getScopusPublicationId());

                    pubAuthorDto.setAuthorsList(authors);

                    response.getResult().add(pubAuthorDto);
                }
            }
            else{
                System.out.println("Message: " + ResultCodeEnum.NO_RESULTS.getMessage());
                response.setValues(ResultCodeEnum.NO_RESULTS.getCode(), ResultCodeEnum.NO_RESULTS.getMessage());
                return response;
            }

            response.setTotal((long) response.getResult().size());
            return response;
        } else {
            System.out.println("No author found with authid: " + request.getAuthid());
            response.setValues(ResultCodeEnum.NO_RESULTS.getCode(), ResultCodeEnum.NO_RESULTS.getMessage());
            return response;
        }
    }

    private List<ScopusAuthorDto> getAuthorsOfPublication(Long publicationId){
        StringBuilder sql = new StringBuilder();
        Map<String, Object> parameters = new HashMap<>();

        List<ScopusAuthorDto> authors = new ArrayList<>();

        sql.append(" SELECT ");
        sql.append(" sap.scopus_author_id, ");
        sql.append(" sap.seq, ");
        sql.append(" sa.authid, ");
        sql.append(" sa.author_name ");

        sql.append(" FROM scopus_author sa ");
        sql.append(" JOIN scopus_author_publication sap ON sa.scopus_author_id = sap.scopus_author_id ");
        sql.append(" WHERE sap.scopus_publication_id = :scopus_publication_id");

        parameters.put("scopus_publication_id", publicationId);

        Query query = entityManager.createNativeQuery(sql.toString());
        Set<Map.Entry<String, Object>> entrySet = parameters.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        List<Object[]> resultList = query.getResultList();

        if(!CollectionUtils.isEmpty(resultList)){
            for (Object[] item : resultList) {
                ScopusAuthorDto authorDto = new ScopusAuthorDto();

                authorDto.setScopusAuthorId(item[0] != null ? Long.valueOf(item[0].toString()) : null);
                authorDto.setSeq(item[1] != null ? Integer.valueOf(item[1].toString()) : null);
                authorDto.setAuthid(item[2] != null ? item[2].toString() : null);
                authorDto.setAuthorName(item[3] != null ? item[3].toString() : null);

                authors.add(authorDto);
            }
        }

        return authors;
    }


    @Override
    public ScopusPublicationResponse getScopusPublicationById(ScopusPublicationRequest request) {
        validationUtils.validateKeyCode(request.getKeyCode());
        StringBuilder sql = new StringBuilder();
        Map<String, Object> parameters = new HashMap<>();

        sql.append(" SELECT ");
        sql.append(" sp.scopus_publication_id, ");
        sql.append(" sp.title, ");
        sql.append(" sp.publication_name, ");
        sql.append(" sp.cover_date, ");
        sql.append(" sp.sub_type_description, ");
        sql.append(" sp.aggregation_type ");

        sql.append(" FROM scopus_publication sp ");
        sql.append(" WHERE sp.scopus_publication_id = :scopus_publication_id ");

        parameters.put("scopus_publication_id", request.getScopusPublicationId());

        ScopusPublicationResponse response = new ScopusPublicationResponse();
        List<ScopusAuthorDto> authors;

        Query query = entityManager.createNativeQuery(sql.toString());
        Set<Map.Entry<String, Object>> entrySet = parameters.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        List<Object[]> resultList = query.getResultList();

        if(!CollectionUtils.isEmpty(resultList)){
            for (Object[] item : resultList) {
                ScopusPublicationAuthorDto pubAuthorDto = new ScopusPublicationAuthorDto();

                pubAuthorDto.setScopusPublicationId(item[0] != null ? Long.valueOf(item[0].toString()) : null);
                pubAuthorDto.setTitle(item[1] != null ? item[1].toString() : null);
                pubAuthorDto.setPublicationName(item[2] != null ? item[2].toString() : null);
                pubAuthorDto.setCoverDate(item[3] != null ? item[3].toString() : null);
                pubAuthorDto.setSubTypeDescription(item[4] != null ? item[4].toString() : null);
                pubAuthorDto.setAggregationType(item[5] != null ? item[5].toString() : null);

                authors = getAuthorsOfPublication(pubAuthorDto.getScopusPublicationId());

                pubAuthorDto.setAuthorsList(authors);

                response.setScopusPublicationAuthor(pubAuthorDto);
            }
        }
        else{
            response.setValues(ResultCodeEnum.NO_RESULTS.getCode(), ResultCodeEnum.NO_RESULTS.getMessage());
        }

        return response;
    }

    @Override
    public ResearchGroupAuthorListResponse searchResearchGroupOfAuthor(ResearchGroupAuthorListRequest request) {
        validationUtils.validateKeyCode(request.getKeyCode());
        Map<String, Object> parameters = new HashMap<>();
        StringBuilder sql = new StringBuilder();

        List<ResearchGroupDto> groups = new ArrayList<>();

        sql.append(" SELECT ");
        sql.append(" org.idOrgUnit, ");
        sql.append(" pa.Status, ");
        sql.append(" org.Name, ");
        sql.append(" org.Acronym, ");
        sql.append(" org.PartOf, ");
        sql.append(" pa.idPerson_Role, ");
        sql.append(" orggro.Status, ");
        sql.append(" orggro.Category ");

        sql.append(" FROM person_affiliation pa ");
        sql.append(" JOIN orgunit org ON pa.idOrgUnit = org.idOrgUnit ");
        sql.append(" JOIN orgunit_group orggro ON org.idOrgUnit = orggro.idOrgUnit ");
        sql.append(" WHERE org.idOrgUnit_Type = \"GRUPO\"");
        sql.append(" AND pa.idPerson = :idPerson");

        parameters.put("idPerson", request.getIdPerson());

        ResearchGroupAuthorListResponse response = new ResearchGroupAuthorListResponse();

        Query query = entityManager.createNativeQuery(sql.toString());
        Set<Map.Entry<String, Object>> entrySet = parameters.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        List<Object[]> resultList = query.getResultList();

        if(!CollectionUtils.isEmpty(resultList)){
            for (Object[] item : resultList) {
                ResearchGroupDto groupDto = new ResearchGroupDto();

                groupDto.setIdOrgUnit(item[0] != null ? item[0].toString() : null);
                groupDto.setStatus(item[1] != null ? item[1].toString() : null);
                groupDto.setName(item[2] != null ? item[2].toString() : null);
                groupDto.setAcronym(item[3] != null ? item[3].toString() : null);
                groupDto.setPartOf(item[4] != null ? item[4].toString() : null);
                groupDto.setIdPersonRole(item[5] != null ? item[5].toString() : null);
                groupDto.setStatusGroup(item[6] != null ? item[6].toString() : null);
                groupDto.setCategoryGroup(item[7] != null ? item[7].toString() : null);

                response.getResult().add(groupDto);
            }
        }
        else{
            response.setValues(ResultCodeEnum.NO_RESULTS.getCode(), ResultCodeEnum.NO_RESULTS.getMessage());
        }

        response.setTotal((long) response.getResult().size());

        return response;
    }
}
