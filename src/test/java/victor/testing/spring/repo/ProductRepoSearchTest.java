package victor.testing.spring.repo;

import com.github.tomakehurst.wiremock.core.MappingsSaver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.WaitForDatabase;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.domain.User;
import victor.testing.spring.facade.ProductSearchCriteria;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("db-mysql")
@Tag("integration")
@ContextConfiguration(initializers = WaitForDatabase.class)
@SpringBootTest
@Transactional
//@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
//@Sql(scripts = "classpath:/clear-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class ProductRepoSearchTest /*extends RepoTestBase */{
    @Autowired
    private ProductRepo repo;
    @Autowired
    private SupplierRepo supplierRepo;

    // gets reinstantiated for each @Test as the test class is always a fresh one
    private ProductSearchCriteria criteria = new ProductSearchCriteria();

    @RegisterExtension
    public CommonDataExtension data = new CommonDataExtension();


    @BeforeEach
    public final void before() {
//        assertThat(repo.findAll()).isEmpty(); // useful for real-world large project
//        repo.deleteAll();
//        supplierRepo.deleteAll();app
    }
    @Test
    public void noCriteria() {
        repo.save(new Product("A"));
        assertThat(repo.search(criteria)).hasSize(1);
    }
    @Test
    public void noCriteriaBis() {
        repo.save(new Product("B"));
        assertThat(repo.search(criteria)).hasSize(1);
    }
    @Test
    public void byASupplier() {
        Supplier supplier = new Supplier("Sup");
        repo.save(new Product("B").setSupplier(supplier).setCreatedBy(data.getUser()));

        criteria.supplierId = supplier.getId();
        assertThat(repo.search(criteria)).hasSize(1);

        criteria.supplierId = -1L;
        assertThat(repo.search(criteria)).isEmpty();
    }
    //- Profiles for DB connection details
    //- Test Interdependencies:
    //  > Assert initially empty
    //  > Manual Cleanup
    //  + @Test supplierId - run in middle
    //  > @Sql
    //  > @DirtiesContext
    //- @Transactional tests
    //  > p6spy
    //  > nested transactions
    //- Insert Static Data: USER
    //  > JPA: inherit fixture
    //  > JPA: compose fixture (JUnit5)
    //  > ActiveProfile insertDummyData << show case only
    //  > @Sql
    //- Cucumber integration
    //- Run on dockerized DB: @Tag @ContextConfiguration

}

