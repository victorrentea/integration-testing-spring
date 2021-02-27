package victor.testing.spring.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode.Exclude;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class User {
   @Id
   @GeneratedValue
   @Setter(AccessLevel.NONE)
   private Long id;
   private String username;
}
