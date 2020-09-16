package victor.testing.spring.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.testing.spring.domain.Product;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.web.ProductDto;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductFacade {
    private final SafetyClient safetyClient;
    private final ProductRepo productRepo;
    private final SupplierRepo supplierRepo;
    private final Clock clock;

    public long createProduct(ProductDto productDto) {
        boolean safe = safetyClient.isSafe(productDto.upc);
        if (!safe) {
            throw new IllegalStateException("Product is not safe: " + productDto.upc);
        }

        Product product = new Product();
        product.setName(productDto.name);
        product.setCategory(productDto.category);
        product.setUpc(productDto.upc);
        product.setSupplier(supplierRepo.getOne(productDto.supplierId));
        product.setCreateDate(LocalDateTime.now(clock));
        productRepo.save(product);
        return product.getId();
    }

    public List<ProductSearchResult> searchProduct(ProductSearchCriteria criteria) {
        return productRepo.search(criteria);
    }
}
