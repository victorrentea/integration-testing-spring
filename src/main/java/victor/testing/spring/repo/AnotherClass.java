package victor.testing.spring.repo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.domain.Product;

import javax.persistence.EntityManager;

@RequiredArgsConstructor
@Component
public class AnotherClass {
   private final EntityManager em;
   @Transactional(propagation = Propagation.REQUIRES_NEW)
   public void runsInSeparatedTransaction() {
      em.persist(new Product("Garbage"));

   }
}
