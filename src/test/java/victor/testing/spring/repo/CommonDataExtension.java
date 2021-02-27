package victor.testing.spring.repo;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.domain.User;

public class CommonDataExtension implements BeforeEachCallback {
    private final User user = new User();

    public User getUser() {
        return user;
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        ApplicationContext spring = SpringExtension.getApplicationContext(context);
        // TODO
    }
}
