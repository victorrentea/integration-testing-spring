package victor.testing.spring.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.testing.spring.domain.Product;
import victor.testing.spring.infra.SafetyServiceClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.web.ProductDto;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import static java.time.temporal.ChronoUnit.SECONDS;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductFacade {
    private final SafetyServiceClient safetyClient;
    private final ProductRepo productRepo;
    private final Clock clock;

    public ProductDto getProduct(long productId) {
        Product product = productRepo.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));

        boolean safe = safetyClient.isSafe(product.getExternalRef());

        if (!safe) {
            throw new IllegalStateException("Product is not safe: " + productId);
        }

        ProductDto dto = new ProductDto(product);
        dto.sampleDate = product.getSampleDate()
            .orElse(LocalDateTime.now(clock).minus(5, SECONDS))
            .toString();
        return dto;
    }

    public List<ProductSearchResult> searchProduct(ProductSearchCriteria criteria) {
        return productRepo.search(criteria);
    }
}
