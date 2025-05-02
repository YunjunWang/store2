package entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table(name="tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(nullable = false, name = "name")
    private String name;

    // owned by User entity by the field tags since User entity owns the relationship using @JoinTable
    @ManyToMany(mappedBy = "tags")
    @Builder.Default
    @ToString.Exclude
    private Set<User> users = new HashSet<>();
}
