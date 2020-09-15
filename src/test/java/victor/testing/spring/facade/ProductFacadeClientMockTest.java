package victor.testing.spring.facade;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.ProductCategory;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.infra.SafetyServiceClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.web.ProductDto;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("db-mem")
@Transactional
public class ProductFacadeClientMockTest {
   @MockBean
   public SafetyServiceClient mockSafetyClient;
   @Autowired
   private ProductRepo productRepo;
   @Autowired
   private ProductFacade productFacade;
   @Autowired
   private SupplierRepo supplierRepo;
   @MockBean
   private Clock clock;

   private LocalDateTime currentTime = LocalDateTime.now();

   @BeforeEach
   public void setupTime() {
      when(clock.instant()).thenAnswer(call -> currentTime.toInstant(ZoneId.systemDefault().getRules().getOffset(currentTime)));
      when(clock.getZone()).thenReturn(ZoneId.systemDefault());
   }

   @Test
   public void throwsForUnsafeProduct() {
      Assertions.assertThrows(IllegalStateException.class, () -> {
         when(mockSafetyClient.isSafe("upc")).thenReturn(false);
         productFacade.createProduct(new ProductDto("name", "upc",-1L, ProductCategory.HOME));
      });
   }

   @Test
   public void fullOk() {
      Supplier supplier = new Supplier().setActive(true);
      supplierRepo.save(supplier);
      when(mockSafetyClient.isSafe("upc")).thenReturn(false);

      currentTime = LocalDateTime.parse("2020-01-01T20:00:00");

      productFacade.createProduct(new ProductDto("name", "upc", supplier.getId(), ProductCategory.HOME));

      Product product = productRepo.findAll().get(0);
      assertThat(product.getName()).isEqualTo("name");
      assertThat(product.getUpc()).isEqualTo("upc");
      assertThat(product.getSupplier()).isEqualTo(supplier);
      assertThat(product.getCategory()).isEqualTo(ProductCategory.HOME);
      assertThat(product.getCreateDate()).isEqualTo(currentTime);
   }

   // TODO Fixed Time
   // @TestConfiguration public static class ClockConfig {  @Bean  @Primary  public Clock fixedClock() {}}

   // TODO Variable Time
   // when(clock.instant()).thenAnswer(call -> currentTime.toInstant(ZoneId.systemDefault().getRules().getOffset(currentTime)));
   // when(clock.getZone()).thenReturn(ZoneId.systemDefault());
}
