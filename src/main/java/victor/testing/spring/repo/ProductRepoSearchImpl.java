package victor.testing.spring.repo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import victor.testing.spring.facade.ProductSearchResult;
import victor.testing.spring.facade.ProductSearchCriteria;
import wiremock.org.apache.commons.lang3.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
@RequiredArgsConstructor
public class ProductRepoSearchImpl implements ProductRepoSearch {
    private final EntityManager em;

    @Override
    public List<ProductSearchResult> search(ProductSearchCriteria criteria) {
        String jpql = "SELECT new victor.testing.spring.facade.ProductSearchResult(p.id, p.name)" +
                " FROM Product p " +
                " WHERE 1=1 ";

        Map<String, Object> paramMap = new HashMap<>();

        if (StringUtils.isNotEmpty(criteria.name)) {
            jpql += "  AND UPPER(p.name) LIKE UPPER('%' || :name || '%')   ";
            paramMap.put("name", criteria.name);
        }
        if (criteria.supplierId != null) {
            jpql += "  AND p.supplier.id = :supplierId   ";
            paramMap.put("supplierId", criteria.supplierId);
        }
        if (criteria.category != null) {
            jpql += "  AND p.category = :category   ";
            paramMap.put("category", criteria.category);
        }

        TypedQuery<ProductSearchResult> query = em.createQuery(jpql, ProductSearchResult.class);
        for (String paramName : paramMap.keySet()) {
            query.setParameter(paramName, paramMap.get(paramName));
        }
        return query.getResultList();
    }
}
