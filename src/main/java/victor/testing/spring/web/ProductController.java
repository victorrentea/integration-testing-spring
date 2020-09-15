package victor.testing.spring.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import victor.testing.spring.facade.ProductFacade;
import victor.testing.spring.facade.ProductSearchCriteria;
import victor.testing.spring.facade.ProductSearchResult;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductFacade facade;

    @PostMapping("product/create")
    public Long create(@RequestBody ProductDto productDto) {
        return facade.createProduct(productDto);
    }
    @PostMapping("product/search")
    public List<ProductSearchResult> search(@RequestBody ProductSearchCriteria criteria) {
        return facade.searchProduct(criteria);
    }


}
