package victor.testing.spring.facade;

import victor.testing.spring.domain.ProductCategory;

public class ProductSearchCriteria { // smells like JSON
    public String name;
    public ProductCategory category;
    public Long supplierId;
}
