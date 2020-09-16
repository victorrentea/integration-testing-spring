package victor.testing.spring.facade;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.ProductCategory;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.tools.WireMockExtension;
import victor.testing.spring.web.ProductDto;
import wiremock.org.apache.commons.io.IOUtils;

import java.io.FileReader;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = "safety.service.url.base=http://localhost:8089")
@ActiveProfiles("db-mem")
public class ProductFacadeClientWireMockTest {
   @Autowired
   public SafetyClient mockSafetyClient;
   @Autowired
   private ProductRepo productRepo;
   @Autowired
   private SupplierRepo supplierRepo;
   @Autowired
   private ProductFacade productFacade;

   @RegisterExtension
   public WireMockExtension wireMock = new WireMockExtension(8089);

   @Test
   public void throwsForUnsafeProduct() {
      Assertions.assertThrows(IllegalStateException.class, () -> {
         productFacade.createProduct(new ProductDto("name", "UNSAFE",-1L, ProductCategory.HOME));
      });
   }
   @Test
   public void throwsForUnsafeProductProgrammaticWireMock() {
      Assertions.assertThrows(IllegalStateException.class, () -> {
         WireMock.stubFor(get(urlEqualTo("/product/customXX/safety"))
             .willReturn(aResponse()
                 .withStatus(200)
                 .withHeader("Content-Type", "application/json")
                 .withBody("{\"entries\": [{\"category\": \"DETERMINED\",\"detailsUrl\": \"http://wikipedia.com\"}]}"))); // override


         productFacade.createProduct(new ProductDto("name", "customXX",-1L, ProductCategory.HOME));
      });
   }
   @Test
   @SneakyThrows
   public void throwsForUnsafeProductProgrammaticWireMockFromFileTemplatized() {
      Assertions.assertThrows(IllegalStateException.class, () -> {
         String template;
         try (FileReader reader = new FileReader("C:\\workspace\\integration-testing-spring\\src\\test\\java\\victor\\testing\\spring\\facade\\inTemplate.json")) {
            template = IOUtils.toString(reader);
         }
         template.replace("{{}}", "DYNAMIC STUFF");
         WireMock.stubFor(get(urlEqualTo("/product/customXX/safety"))
             .willReturn(aResponse()
                 .withStatus(200)
                 .withHeader("Content-Type", "application/json")
                 .withBody(  template ))); // override


         productFacade.createProduct(new ProductDto("name", "customXX",-1L, ProductCategory.HOME));
      });
   }

   @Test
   public void fullOk() {
      long supplierId = supplierRepo.save(new Supplier()).getId();

      ProductDto dto = new ProductDto("name", "SAFE", supplierId, ProductCategory.HOME);
      productFacade.createProduct(dto);

      Product product = productRepo.findAll().get(0);
      LocalDateTime today = LocalDateTime.parse("2014-12-22T10:15:30.00");

      assertThat(product.getName()).isEqualTo("name");
      assertThat(product.getUpc()).isEqualTo("SAFE");
      assertThat(product.getSupplier().getId()).isEqualTo(supplierId);
      assertThat(product.getCategory()).isEqualTo(ProductCategory.HOME);
      assertThat(product.getCreateDate()).isEqualTo(today);
   }

   @TestConfiguration
   public static class TestConfig {
      @Bean
      @Primary
      public Clock clockFixed() {
         return Clock.fixed(Instant.parse("2014-12-22T10:15:30.00Z"), ZoneId.systemDefault());
      }
   }



   // TODO Fixed Time
   // @TestConfiguration public static class ClockConfig {  @Bean  @Primary  public Clock fixedClock() {}}

   // TODO Variable Time
   // when(clock.instant()).thenAnswer(call -> currentTime.toInstant(ZoneId.systemDefault().getRules().getOffset(currentTime)));
   // when(clock.getZone()).thenReturn(ZoneId.systemDefault());
}
