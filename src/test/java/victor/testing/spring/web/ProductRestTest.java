package victor.testing.spring.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import victor.testing.spring.domain.ProductCategory;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.facade.ProductSearchCriteria;
import victor.testing.spring.facade.ProductSearchResult;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductRestTest {
   @MockBean
   private SafetyClient safetyClient;
   @Autowired
   private SupplierRepo supplierRepo;
   @Autowired
   private ProductRepo productRepo;

   @Autowired
   private TestRestTemplate rest; // vs RestTemplate + base URL + .withBasicAuth("spring", "secret")
//   private RestTemplate rest;

//   @Autowired
//   public void initRestTemplate(@Value("http://localhost:${local.server.port}") String baseUri) {
//      rest = new RestTemplate();
//      rest.setUriTemplateHandler(new DefaultUriBuilderFactory(baseUri));
//   }

   @BeforeEach
   public void initialize() {
      productRepo.deleteAll();
      supplierRepo.deleteAll();
   }

   @Test
   public void testSearch() {
      Long supplierId = supplierRepo.save(new Supplier().setActive(true)).getId();
      when(safetyClient.isSafe("UPC")).thenReturn(true);

      var productDto = new ProductDto("Tree", "UPC", supplierId, ProductCategory.ME);

      var createResult = rest.postForEntity("/product/create", productDto, Void.class);
      assertEquals(HttpStatus.OK, createResult.getStatusCode());

      var searchCriteria = new ProductSearchCriteria("Tree", null, null);
      ResponseEntity<List<ProductSearchResult>> searchResponse = rest.exchange(
          "/product/search", HttpMethod.POST,
          new HttpEntity<>(searchCriteria), new ParameterizedTypeReference<>() {
          });

      assertEquals(HttpStatus.OK, searchResponse.getStatusCode());
      assertThat(searchResponse.getBody()).hasSize(1);
      assertThat(searchResponse.getBody()).allMatch(p -> "Tree".equals(p.getName()));
   }


}
