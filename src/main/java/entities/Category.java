package entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@ToString
@Entity
@Table(name="categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private byte id;

    @Column(name="name")
    private String name;

    @OneToMany(mappedBy = "category")
    @ToString.Exclude
    private Set<Product> products = new HashSet<>();

    public Category(String name) {
        this.name = name;
    }
}
