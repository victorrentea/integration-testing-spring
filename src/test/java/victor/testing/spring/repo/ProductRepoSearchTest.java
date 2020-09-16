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

    @BeforeEach
    public void initialize() {
        repo.save(new Product());

    }
    @Test
    public void noCriteria() {
        assertThat(repo.search(criteria)).hasSize(1);
    }
    @Test
    public void noCriteri2a() {
//        repo.save(new Product());
        assertThat(repo.search(criteria)).hasSize(1);
    }

    // TODO finish

    // TODO base test class persisting supplier

    // TODO replace with composition
}

