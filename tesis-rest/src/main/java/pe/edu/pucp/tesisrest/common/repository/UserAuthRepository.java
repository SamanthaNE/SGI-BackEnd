package pe.edu.pucp.tesisrest.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.pucp.tesisrest.common.model.person.UserAuth;

@Repository
public interface UserAuthRepository extends JpaRepository<UserAuth, String> {
    UserAuth findByIdPerson(String idPerson);
}
