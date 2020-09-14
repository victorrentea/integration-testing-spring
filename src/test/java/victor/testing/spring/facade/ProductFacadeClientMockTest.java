package victor.testing.spring.facade;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.infra.SafetyServiceClient;
import victor.testing.spring.repo.ProductRepo;
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
         Product product = new Product().setExternalRef("EXTREF");
         productRepo.save(product);
         when(mockSafetyClient.isSafe("EXTREF")).thenReturn(false);
         productFacade.getProduct(product.getId());
      });
   }

   @Test
   public void fullOk() {
      Product product = new Product().setExternalRef("EXTREF").setName("Prod");
      Supplier supplier = new Supplier().setActive(true);
      product.setSupplier(supplier);
      productRepo.save(product);
      when(mockSafetyClient.isSafe("EXTREF")).thenReturn(true);
      currentTime = LocalDateTime.parse("2020-01-01T20:00:00");

      ProductDto dto = productFacade.getProduct(product.getId());

      assertThat(dto.productName).isEqualTo("Prod");
      System.out.println(dto.sampleDate);
      assertThat(dto.sampleDate).isEqualTo("2020-01-01T19:59:55");
   }

   // TODO Fixed Time
   // @TestConfiguration public static class ClockConfig {  @Bean  @Primary  public Clock fixedClock() {}}

   // TODO Variable Time
   // when(clock.instant()).thenAnswer(call -> currentTime.toInstant(ZoneId.systemDefault().getRules().getOffset(currentTime)));
   // when(clock.getZone()).thenReturn(ZoneId.systemDefault());
}
