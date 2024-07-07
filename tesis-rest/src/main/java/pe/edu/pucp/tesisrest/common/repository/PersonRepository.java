package pe.edu.pucp.tesisrest.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import pe.edu.pucp.tesisrest.common.model.person.Person;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, String>, JpaSpecificationExecutor<Person> {
}
