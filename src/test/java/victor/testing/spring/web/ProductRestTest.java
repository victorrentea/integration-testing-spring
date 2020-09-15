package victor.testing.spring.web;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import victor.testing.spring.domain.ProductCategory;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.facade.ProductSearchCriteria;
import victor.testing.spring.facade.ProductSearchResult;
import victor.testing.spring.infra.SafetyServiceClient;
import victor.testing.spring.repo.SupplierRepo;

import java.net.URI;
import java.net.URL;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductRestTest {
   @MockBean
   private SafetyServiceClient safetyClient;
   @Autowired
   private SupplierRepo supplierRepo;
//    @Autowired
//    private TestRestTemplate rest; // vs RestTemplate + base URL + .withBasicAuth("spring", "secret")

   @Value("http://localhost:${local.server.port}")
   private String baseUri;

   @Test
   public void testSearch() {
      RestTemplate rest = new RestTemplate();
      rest.setUriTemplateHandler(new DefaultUriBuilderFactory(baseUri));


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
      assertThat(searchResponse.getBody()).allMatch(p -> "Tree".equals(p.getName()));
   }


}
