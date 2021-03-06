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

@Transactional
@SpringBootTest
@ActiveProfiles("db-h2")
@Sql(scripts = "classpath:/supplier.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
//@Tag("integration")
//@ContextConfiguration(initializers = WaitForDatabase.class)
public class ProductRepoSearchTest {
    @Autowired
    private ProductRepo repo;

    private ProductSearchCriteria criteria = new ProductSearchCriteria();

    @RegisterExtension
    public CommonDataExtension commonData = new CommonDataExtension();

    public ProductRepoSearchTest() {
        System.out.println("New test class instance");
    }
    @BeforeEach
    public void initialize() {
        assertThat(repo.count()).isEqualTo(0); // good idea for larger projects
    }
    @Test
    public void noCriteria() {
        repo.save(new Product());
        assertThat(repo.search(criteria)).hasSize(1);
    }

    @Test
//    @Commit // for letting the Test Tx commit so that you can debug it after
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

    @Test
    public void bySupplierMatch() {
        repo.save(new Product().setSupplier(commonData.getSupplier()));
        criteria.supplierId = commonData.getSupplier().getId();
        assertThat(repo.search(criteria)).hasSize(1);
    }

    @Test
    public void bySupplierNoMatch() {
        repo.save(new Product().setSupplier(commonData.getSupplier()));
        criteria.supplierId = -1L;
        assertThat(repo.search(criteria)).isEmpty();
    }


    // TODO base test class persisting supplier

    // TODO replace with composition
}

