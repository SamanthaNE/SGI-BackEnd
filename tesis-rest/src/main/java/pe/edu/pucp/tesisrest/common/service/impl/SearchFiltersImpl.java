package pe.edu.pucp.tesisrest.common.service.impl;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pe.edu.pucp.tesisrest.common.dto.request.SearchPersonByRequest;
import pe.edu.pucp.tesisrest.common.dto.response.SearchPersonByResponse;
import pe.edu.pucp.tesisrest.common.model.person.Person;
import pe.edu.pucp.tesisrest.common.repository.PersonRepository;
import pe.edu.pucp.tesisrest.common.service.SearchFiltersService;
import pe.edu.pucp.tesisrest.common.util.ValidationUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SearchFiltersImpl implements SearchFiltersService {

    private final ValidationUtils validationUtils;
    private final PersonRepository personRepository;
    @Override
    public SearchPersonByResponse findByPersonNameContainingWords(SearchPersonByRequest request) {
        validationUtils.validateKeyCode(request.getKeyCode());
        SearchPersonByResponse response = new SearchPersonByResponse();

        List<Person> personList =
                personRepository.findAll((root, query, criteriaBuilder) -> {
                    String[] words = request.getPersonData().split("\\s+");
                    List<Predicate> predicates = new ArrayList<>();

                    for (String word : words) {
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("personName")), "%" + word.toLowerCase() + "%"));
                    }

                    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                });

        response.setResultList(personList);

        return response;
    }
}
