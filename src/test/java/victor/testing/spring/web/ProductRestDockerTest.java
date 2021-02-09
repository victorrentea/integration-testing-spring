package victor.testing.spring.web;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import victor.testing.spring.domain.ProductCategory;
import victor.testing.spring.facade.ProductSearchCriteria;
import victor.testing.spring.facade.ProductSearchResult;
import victor.testing.spring.tools.WireMockExtension;
import victor.testing.spring.tools.WaitForSpringActuator;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@Slf4j
@Tag("integration")
public class ProductRestDockerTest {

   @RegisterExtension
   public static WaitForSpringActuator waitExtension = new WaitForSpringActuator("http://localhost:8080");

   @RegisterExtension
   public WireMockExtension wireMockExtension = new WireMockExtension(8089);

   private RestTemplate rest = new RestTemplate();

   @BeforeEach
   public void initialize() {
      rest.setUriTemplateHandler(new DefaultUriBuilderFactory("http://localhost:8080"));
   }

   @Test
   public void testSearchBLACK() {
      long supplierId = 1L; // assumed to exist in DB - @see victor.testing.spring.DummyDataCreator
      log.info("Start");

      ProductDto productDto = new ProductDto("Tree", "SAFE", supplierId, ProductCategory.ME);
      ResponseEntity<Void> createResult = rest.postForEntity("/product/create", productDto, Void.class);
      assertThat(createResult.getStatusCode()).isEqualTo(HttpStatus.OK);
      log.info("Created OK");

      ProductSearchCriteria searchCriteria = new ProductSearchCriteria("Tree", null, null);
      ResponseEntity<List<ProductSearchResult>> searchResponse = rest.exchange(
          "/product/search", HttpMethod.POST,
          new HttpEntity<>(searchCriteria), new ParameterizedTypeReference<>() {
          });

      assertThat(createResult.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(searchResponse.getBody()).allMatch(p -> "Tree".equals(p.getName()));
      log.info("Search OK");
   }


}
