package pe.edu.pucp.tesisrest.worker.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import pe.edu.pucp.tesisrest.common.dto.ResearchGroupDetailDto;
import pe.edu.pucp.tesisrest.common.dto.ResearchGroupDto;
import pe.edu.pucp.tesisrest.common.enums.ResultCodeEnum;
import pe.edu.pucp.tesisrest.common.repository.OrgUnitRepository;
import pe.edu.pucp.tesisrest.common.service.CommonService;
import pe.edu.pucp.tesisrest.common.util.QueryUtils;
import pe.edu.pucp.tesisrest.common.util.ValidationUtils;
import pe.edu.pucp.tesisrest.worker.dto.request.ResearchGroupDetailRequest;
import pe.edu.pucp.tesisrest.worker.dto.request.ResearchGroupListRequest;
import pe.edu.pucp.tesisrest.worker.dto.response.ResearchGroupDetailResponse;
import pe.edu.pucp.tesisrest.worker.dto.response.ResearchGroupListResponse;
import pe.edu.pucp.tesisrest.worker.service.ResearchGroupService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class ResearchGroupImpl implements ResearchGroupService {

    private final ValidationUtils validationUtils;
    private final EntityManager entityManager;
    private final OrgUnitRepository orgunitRepository;
    private final CommonService commonService;
    private final QueryUtils queryUtils;


    @Override
    public ResearchGroupListResponse getResearchGroupList(ResearchGroupListRequest request) {
        validationUtils.validateKeyCode(request.getKeyCode());
        Map<String, Object> parameters = new HashMap<>();

        ResearchGroupListResponse response = new ResearchGroupListResponse();

        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT ");
        sql.append(" org.idOrgUnit, ");
        sql.append(" org.Name, ");
        sql.append(" org.Acronym, ");
        sql.append(" org.PartOf, ");
        sql.append(" orggro.Status, ");
        sql.append(" orggro.Category ");

        sql.append(" FROM orgunit org");
        sql.append(" JOIN orgunit_group orggro ON org.idOrgUnit = orggro.idOrgUnit ");
        sql.append(" WHERE org.idOrgUnit_Type = \"GRUPO\" ");

        Query query = entityManager.createNativeQuery(sql.toString());
        Set<Map.Entry<String, Object>> entrySet = parameters.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        queryUtils.setPagination(query, request.getPage(), request.getSize());

        List<Object[]> resultList = query.getResultList();

        if(!CollectionUtils.isEmpty(resultList)){
            for (Object[] item : resultList) {
                ResearchGroupDto researchGroupDto = new ResearchGroupDto();

                researchGroupDto.setIdOrgUnit(item[0] != null ? item[0].toString() : null);
                researchGroupDto.setName(item[1] != null ? item[1].toString() : null);
                researchGroupDto.setAcronym(item[2] != null ? item[2].toString() : null);
                researchGroupDto.setPartOf(item[3] != null ? item[3].toString() : null);
                researchGroupDto.setStatusGroup(item[4] != null ? item[4].toString() : null);
                researchGroupDto.setCategoryGroup(item[5] != null ? item[5].toString() : null);

                researchGroupDto.setNamePartOf(orgunitRepository.findByIdOrgUnit(researchGroupDto.getPartOf()).getName());

                response.getResearchGroup().add(researchGroupDto);
            }
        }
        else{
            response.setValues(ResultCodeEnum.NO_RESULTS.getCode(), ResultCodeEnum.NO_RESULTS.getMessage());
        }

        response.setTotal((long) response.getResearchGroup().size());

        return response;
    }

    @Override
    public ResearchGroupDetailResponse getResearchGroupDetail(ResearchGroupDetailRequest request) {
        validationUtils.validateKeyCode(request.getKeyCode());
        ResearchGroupDetailResponse response = new ResearchGroupDetailResponse();

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
