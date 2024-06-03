package pe.edu.pucp.tesisrest.common.util;

import jakarta.persistence.Query;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class QueryUtils {

    public void setPagination(Query query, Integer page, Integer size) {
        query.setFirstResult(size * (page - 1));
        query.setMaxResults(size);
    }

    public Pageable setPagination(Integer page, Integer size) {
        return PageRequest.of(page - 1, size);
    }

    public Pageable setPagination(Integer page, Integer size, Sort sort) {
        return PageRequest.of(page - 1, size, sort);
    }
}
