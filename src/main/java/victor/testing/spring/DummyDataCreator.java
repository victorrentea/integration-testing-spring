package victor.testing.spring;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import victor.testing.spring.domain.User;
import victor.testing.spring.repo.UserRepo;

@Slf4j
@Component
@Profile("insertDummyData")
@RequiredArgsConstructor
public class DummyDataCreator implements CommandLineRunner {
   private final UserRepo userRepo;

   @Override
   public void run(String... args) throws Exception {
      userRepo.save(new User());

      log.info("Inserted dummy data");

   }
}
