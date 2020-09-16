package victor.testing.spring.facade;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.ProductCategory;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.tools.WireMockExtension;
import victor.testing.spring.web.ProductDto;

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
//         when(mockSafetyClient.isSafe("upc")).thenReturn(false);
         productFacade.createProduct(new ProductDto("name", "UNSAFE",-1L, ProductCategory.HOME));
      });
   }

   @Test
   public void fullOk() {
      long supplierId = supplierRepo.save(new Supplier()).getId();
//      when(mockSafetyClient.isSafe("upc")).thenReturn(true);

      ProductDto dto = new ProductDto("name", "SAFE", supplierId, ProductCategory.HOME);
      productFacade.createProduct(dto);

      Product product = productRepo.findAll().get(0);

      assertThat(product.getName()).isEqualTo("name");
      assertThat(product.getUpc()).isEqualTo("SAFE");
      assertThat(product.getSupplier().getId()).isEqualTo(supplierId);
      assertThat(product.getCategory()).isEqualTo(ProductCategory.HOME);
      assertThat(product.getCreateDate()).isNotNull();
   }


   // TODO Fixed Time
   // @TestConfiguration public static class ClockConfig {  @Bean  @Primary  public Clock fixedClock() {}}

   // TODO Variable Time
   // when(clock.instant()).thenAnswer(call -> currentTime.toInstant(ZoneId.systemDefault().getRules().getOffset(currentTime)));
   // when(clock.getZone()).thenReturn(ZoneId.systemDefault());
}
