package victor.testing.spring.facade;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.domain.ProductCategory;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.web.ProductDto;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "safety.service.url.base=http://localhost:8089")
@ActiveProfiles("db-mem")
@Transactional
public class ProductFacadeWireMockTest {
   @Autowired
   private ProductRepo productRepo;
   @Autowired
   private ProductFacade productFacade;
   @Autowired
   private SupplierRepo supplierRepo;

   @RegisterExtension
   public WireMockExtension wireMock = new WireMockExtension(8089);

   @Test
   public void throwsForUnsafeProduct() {
      Assertions.assertThrows(IllegalStateException.class, () -> {
         productFacade.createProduct(new ProductDto("name", "UNSAFE", -1L, ProductCategory.HOME));
      });
   }

   @Test
   public void fullOk() {
      Supplier supplier = new Supplier().setActive(true);
      supplierRepo.save(supplier);

      productFacade.createProduct(new ProductDto("name", "SAFE", supplier.getId(), ProductCategory.HOME));

      assertThat(productRepo.findAll()).hasSize(1);
   }

   @Autowired
   private CacheManager cacheManager;

   @Test
   public void fullOkHacked() {
      Supplier supplier = new Supplier().setActive(true);
      supplierRepo.save(supplier);

      cacheManager.getCacheNames().stream().map(cacheManager::getCache).forEach(Cache::clear);

      WireMock.stubFor(get(urlEqualTo("/product/SAFE/safety"))
          .willReturn(aResponse()
              .withStatus(200)
              .withHeader("Content-Type", "application/json")
              .withBody("{\"entries\": [{\"category\": \"UNKNOWN\"}]}"))); // override


      Assertions.assertThrows(IllegalStateException.class, () -> {
         productFacade.createProduct(new ProductDto("name", "SAFE", supplier.getId(), ProductCategory.HOME));
      });
   }
}
