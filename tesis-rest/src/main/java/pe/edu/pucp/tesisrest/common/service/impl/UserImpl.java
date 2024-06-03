package pe.edu.pucp.tesisrest.common.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import pe.edu.pucp.tesisrest.common.dto.UserDto;
import pe.edu.pucp.tesisrest.common.dto.request.UserRequest;
import pe.edu.pucp.tesisrest.common.dto.response.UserResponse;
import pe.edu.pucp.tesisrest.common.enums.ResultCodeEnum;

import pe.edu.pucp.tesisrest.common.model.person.UserAuth;
import pe.edu.pucp.tesisrest.common.repository.UserAuthRepository;
import pe.edu.pucp.tesisrest.common.service.UserService;
import pe.edu.pucp.tesisrest.common.util.ValidationUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserImpl implements UserService {

    private final ValidationUtils validationUtils;
    private final EntityManager entityManager;

    private final UserAuthRepository userAuthRepository;

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private String findByElectronicAddress(String email) {
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT ");
        sql.append(" pea.IdPerson ");

        sql.append(" FROM person_electronicaddress pea ");
        sql.append(" WHERE pea.ElectronicAddress = :email ");

        Query query = entityManager.createNativeQuery(sql.toString());

        query.setParameter("email", email);

        Object result;
        try {
            result = query.getSingleResult();
            return (String) result;
        } catch (NoResultException e) {
            result = null;
            return String.valueOf(result);
        } catch (NonUniqueResultException e) {
            throw new IllegalStateException("La consulta devolvió más de un resultado.", e);
        }
    }

    @Override
    public UserResponse autheticateUser(UserRequest request) {
        validationUtils.validateKeyCode(request.getKeyCode());
        UserResponse response = new UserResponse();

        String idPerson = findByElectronicAddress(request.getEmail());

        System.out.println("Auth" + idPerson);

        if (idPerson != null) {
            String passwordHash = hashPassword(request.getPassword());
            UserAuth userAuth = userAuthRepository.findByIdPerson(idPerson);

            if (userAuth != null) {
                if (passwordHash.equals(userAuth.getPasswordHash())) {
                    response.setValues(ResultCodeEnum.OK.getCode(), "Autenticación correcta");

                    // Aca se manda la info del user
                    StringBuilder sql = new StringBuilder();
                    sql.append(" SELECT ");
                    sql.append(" pe.idPerson, ");
                    sql.append(" pe.FirstNames, ");
                    sql.append(" pe.FamilyNames, ");
                    sql.append(" pe.ScopusAuthorID , ");
                    sql.append(" pr.idPerson_Role, ");
                    sql.append(" pr.Nombre, ");
                    sql.append(" pa.idOrgUnit ");

                    sql.append(" FROM person pe ");
                    sql.append(" JOIN person_affiliation pa ON pe.idPerson = pa.idPerson ");
                    sql.append(" JOIN person_role pr ON pa.idPerson_Role = pr.idPerson_Role ");
                    sql.append(" WHERE pr.idPerson_Role = \"INVESTIGADOR\" ");
                    sql.append(" AND pe.idPerson = :idPerson");

                    Query query = entityManager.createNativeQuery(sql.toString());
                    query.setParameter("idPerson", userAuth.getIdPerson());

                    List<Object[]> resultList = query.getResultList();

                    if(!CollectionUtils.isEmpty(resultList)){
                        for (Object[] item : resultList) {
                            UserDto userDto = new UserDto();

                            userDto.setIdPerson(item[0] != null ? item[0].toString() : null);
                            userDto.setFirstNames(item[1] != null ? item[1].toString() : null);
                            userDto.setFamilyNames(item[2] != null ? item[2].toString() : null);
                            userDto.setScopusAuthorId(item[3] != null ? item[3].toString() : null);
                            userDto.setIdPersonRole(item[4] != null ? item[4].toString() : null);
                            userDto.setRoleName(item[5] != null ? item[5].toString() : null);
                            userDto.setIdOrgUnit(item[6] != null ? item[6].toString() : null);

                            response.getUserInfo().add(userDto);
                        }
                    }
                    else{
                        response.setValues(ResultCodeEnum.NO_RESULTS.getCode(), ResultCodeEnum.NO_RESULTS.getMessage());
                    }

                }
                else {
                    response.setValues(ResultCodeEnum.VALIDATION_ERRORS.getCode(), "Credenciales incorrectas");
                }
            }
        }
        else {
            response.setValues(ResultCodeEnum.NO_RESULTS.getCode(), "Email no encontrado");
        }

        return response;
    }

    @Override
    public UserResponse registerUser(UserRequest request) {
        validationUtils.validateKeyCode(request.getKeyCode());
        UserResponse userResponse = new UserResponse();

        String idPerson = findByElectronicAddress(request.getEmail());

        if (idPerson != null) {
            String passwordHash = hashPassword(request.getPassword());
            //UserAuth user = new UserAuth(idPerson, passwordHash);
            UserAuth user = new UserAuth();
            user.setIdPerson(idPerson);
            user.setPasswordHash(passwordHash);

            System.out.println("Reg:" + idPerson);
            System.out.println("Reg:" + passwordHash);

            userAuthRepository.save(user);

            userResponse.setValues(ResultCodeEnum.OK.getCode(), "Contraseña registrada correctamente");
        }
        else {
            userResponse.setValues(ResultCodeEnum.NO_RESULTS.getCode(), "Email no encontrado");
        }

        return userResponse;
    }
}
