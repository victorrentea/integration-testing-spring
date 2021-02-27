package victor.testing.spring.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    private String name;

    private ProductCategory category;

    private String upc;

    @ManyToOne
    private Supplier supplier;

    @ManyToOne
    private User createdBy;

    public Product(String name, String upc, ProductCategory category) {
        this.name = name;
        this.upc = upc;
        this.category = category;
    }

    public Product(String name) {
        this.name = name;
    }

    public Product setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public Product() {}

}
