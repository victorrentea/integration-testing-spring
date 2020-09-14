package victor.testing.spring.repo;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.SomeSpringApplication;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.Product.Category;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.facade.ProductSearchCriteria;
import victor.testing.spring.facade.ProductSearchResult;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@CucumberContextConfiguration
@SpringBootTest
@ActiveProfiles({"db-mem", "test"})
public class ProductSearchSteps {
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private SupplierRepo supplierRepo;

    private ProductSearchCriteria criteria = new ProductSearchCriteria();

    private Product product;

    @Given("^Supplier \"([^\"]*)\" exists$")
    public void supplierExists(String supplierName) {
        log.debug("Persisting supplier {}", supplierName);
        supplierRepo.save(new Supplier().setName(supplierName));
    }

    @Given("^One product exists$")
    public void aProductExists() {
        product = new Product();
    }

    @And("^That product has name \"([^\"]*)\"$")
    public void thatProductHasName(String productName) {
        product.setName(productName);
    }

    @And("^That product has supplier \"([^\"]*)\"$")
    public void thatProductHasSupplier(String supplierName) {
        product.setSupplier(supplierRepo.findByName(supplierName));
    }

    @And("^That product has category \"([^\"]*)\"$")
    public void thatProductHasCategory(Category category) {
        product.setCategory(category);
    }


    @When("^The search criteria name is \"([^\"]*)\"$")
    public void theSearchCriteriaNameIs(String productName) {
        criteria.name = productName;
    }

    @And("^The search criteria supplier is \"([^\"]*)\"$")
    public void theSearchCriteriaSupplierIs(String supplierName) {
        criteria.supplierId = supplierRepo.findByName(supplierName).getId();
    }

    @And("^The search criteria category is \"([^\"]*)\"$")
    public void theSearchCriteriaCategoryIs(Category category) {
        criteria.category = category;
    }

    @Then("^That product is returned by search$")
    public void thatProductIsReturned() {
        productRepo.save(product);
        List<ProductSearchResult> results = productRepo.search(criteria);
        assertThat(results).hasSize(1);
        assertThat(results.get(0).id).isEqualTo(product.getId());
    }

    @Then("^No products are returned by search$")
    public void noProductsAreReturnedBySearch() {
        productRepo.save(product);
        List<ProductSearchResult> results = productRepo.search(criteria);
        assertThat(results).isEmpty();
    }
}
