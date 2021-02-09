package victor.testing.spring.repo;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import victor.testing.spring.domain.Supplier;

public class CommonDataExtension implements BeforeEachCallback {
    private final Supplier supplier = new Supplier();

    public Supplier getSupplier() {
        return supplier;
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        ApplicationContext spring = SpringExtension.getApplicationContext(context);
        // TODO
    }
}
