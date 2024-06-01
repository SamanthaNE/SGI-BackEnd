package pe.edu.pucp.tesisrest.common.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pe.edu.pucp.tesisrest.common.dto.request.UserRequest;
import pe.edu.pucp.tesisrest.common.dto.response.UserResponse;
import pe.edu.pucp.tesisrest.common.enums.ResultCodeEnum;

import pe.edu.pucp.tesisrest.common.model.person.UserAuth;
import pe.edu.pucp.tesisrest.common.repository.UserAuthRepository;
import pe.edu.pucp.tesisrest.common.service.UserService;
import pe.edu.pucp.tesisrest.common.util.ValidationUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
        UserResponse userResponse = new UserResponse();

        String idPerson = findByElectronicAddress(request.getEmail());

        System.out.println("Auth" + idPerson);

        if (idPerson != null) {
            String passwordHash = hashPassword(request.getPassword());
            UserAuth userAuth = userAuthRepository.findByIdPerson(idPerson);

            if (userAuth != null) {
                if (passwordHash.equals(userAuth.getPasswordHash())) {
                    userResponse.setValues(ResultCodeEnum.OK.getCode(), "Autenticación correcta");

                    // Aca se manda la info del user
                }
                else {
                    userResponse.setValues(ResultCodeEnum.VALIDATION_ERRORS.getCode(), "Credenciales incorrectas");
                }
            }
        }
        else {
            userResponse.setValues(ResultCodeEnum.NO_RESULTS.getCode(), "Email no encontrado");
        }

        return userResponse;
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
