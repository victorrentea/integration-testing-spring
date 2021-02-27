package victor.testing.spring.repo;


import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.domain.User;

@ActiveProfiles("db-h2")
@SpringBootTest
@Transactional
public abstract class RepoTestBase {
   @Autowired
   private UserRepo userRepo;
   protected User user;

   @BeforeEach
   public final void insertUser() {
      user = userRepo.save(new User());
   }
}
