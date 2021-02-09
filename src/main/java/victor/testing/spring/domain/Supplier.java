package victor.testing.spring.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;
    @Getter @Setter
    private String name;

    @Getter @Setter
    private boolean active;

    public Supplier() {}

    public Supplier(String name) {
        this.name = name;
    }

}
