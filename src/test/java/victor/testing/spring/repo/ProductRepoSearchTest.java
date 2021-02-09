package victor.testing.spring.repo;

import com.github.tomakehurst.wiremock.core.MappingsSaver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.WaitForDatabase;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.facade.ProductSearchCriteria;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
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
    public void noCriteriaBis() {
        repo.save(new Product());
        assertThat(repo.search(criteria)).hasSize(1);
        assertEquals(1, repo.search(criteria).size());
    }

//    @Test
    public void byNameMatch() {
        // TODO
    }
//    @Test
    public void byNameNoMatch() {
        // TODO
    }

//    @Test
    public void bySupplierMatch() {
        // TODO
    }

//    @Test
    public void bySupplierNoMatch() {
        // TODO
    }

}

