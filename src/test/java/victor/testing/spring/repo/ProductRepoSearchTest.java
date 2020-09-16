package victor.testing.spring.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.domain.Product;
import victor.testing.spring.facade.ProductSearchCriteria;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
@ActiveProfiles("db-mem")
public class ProductRepoSearchTest {
    @Autowired
    private ProductRepo repo;

    private ProductSearchCriteria criteria = new ProductSearchCriteria();

    public ProductRepoSearchTest() {
        System.out.println("New test class instance");
    }
    @Test
    public void noCriteria() {
        repo.save(new Product());
        assertThat(repo.search(criteria)).hasSize(1);
    }

    @Test
    public void byNameMatch() {
        criteria.name = "Am";
        repo.save(new Product().setName("naMe"));
        assertThat(repo.search(criteria)).hasSize(1);
    }
    @Test
    public void byNameNoMatch() {
        criteria.name = "nameXX";
        repo.save(new Product().setName("name"));
        assertThat(repo.search(criteria)).isEmpty();
    }


    // TODO base test class persisting supplier

    // TODO replace with composition
}

