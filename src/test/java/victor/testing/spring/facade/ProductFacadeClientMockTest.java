package victor.testing.spring.facade;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.ProductCategory;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.web.ProductDto;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductFacadeClientMockTest {
   @Mock
   public SafetyClient mockSafetyClient;
   @Mock
   private ProductRepo productRepo;
   @Mock
   private SupplierRepo supplierRepo;
   @InjectMocks
   private ProductFacade productFacade;

   @Test
   public void throwsForUnsafeProduct() {
      Assertions.assertThrows(IllegalStateException.class, () -> {
         when(mockSafetyClient.isSafe("upc")).thenReturn(false);
         productFacade.createProduct(new ProductDto("name", "upc",-1L, ProductCategory.HOME));
      });
   }

   @Test
   public void fullOk() {
      Supplier supplier = new Supplier();
      long supplierId = 13L;
      when(supplierRepo.getOne(supplierId)).thenReturn(supplier);
      when(mockSafetyClient.isSafe("upc")).thenReturn(true);

      productFacade.createProduct(new ProductDto("name", "upc", 13L, ProductCategory.HOME));

      // Yuck!
      ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
      verify(productRepo).save(productCaptor.capture());
      Product product = productCaptor.getValue();

      assertThat(product.getName()).isEqualTo("name");
      assertThat(product.getUpc()).isEqualTo("upc");
      assertThat(product.getSupplier()).isEqualTo(supplier);
      assertThat(product.getCategory()).isEqualTo(ProductCategory.HOME);
      assertThat(product.getCreateDate()).isNotNull();
   }


   // TODO Fixed Time
   // @TestConfiguration public static class ClockConfig {  @Bean  @Primary  public Clock fixedClock() {}}

   // TODO Variable Time
   // when(clock.instant()).thenAnswer(call -> currentTime.toInstant(ZoneId.systemDefault().getRules().getOffset(currentTime)));
   // when(clock.getZone()).thenReturn(ZoneId.systemDefault());
}
