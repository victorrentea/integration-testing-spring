package victor.testing.spring.repo;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import victor.testing.spring.domain.Supplier;

public abstract class TestBaseRepo {
   protected Supplier supplier;
   @Autowired
   private SupplierRepo supplierRepo;

   @BeforeEach
   public void persistSupplier() {
       supplier = new Supplier();
       supplierRepo.save(supplier); // after this line, the supplier instance has ID
   }
}
