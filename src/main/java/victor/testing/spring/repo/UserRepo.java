package victor.testing.spring.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.testing.spring.domain.User;

public interface UserRepo extends JpaRepository<User, Long> {
}
