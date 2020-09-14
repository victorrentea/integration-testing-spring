package victor.testing.spring.repo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import victor.testing.spring.facade.ProductSearchResult;
import victor.testing.spring.facade.ProductSearchCriteria;

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
        String jpql = "SELECT new ro.victor.unittest.spring.facade.ProductSearchResult(p.id, p.name)" +
                " FROM Product p " +
                " WHERE 1=1 ";

        Map<String, Object> paramMap = new HashMap<>();


//        TODO
//        if (isNotEmpty(criteria.name)) {
//            jpql += "  AND p.name = :name   ";
//            paramMap.put("name", criteria.name);
//        }

        TypedQuery<ProductSearchResult> query = em.createQuery(jpql, ProductSearchResult.class);
        for (String paramName : paramMap.keySet()) {
            query.setParameter(paramName, paramMap.get(paramName));
        }
        return query.getResultList();
    }
}
