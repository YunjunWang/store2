package entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EntityScan
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(nullable = false, name="name")
    private String name;

    @Column(nullable = false, name="email")
    private String email;

    @Column(nullable = false, name="password")
    private String password;

    // Users table being owned by addresses, therefore User entity is owned by Address entity by Address's field user
    @OneToMany(mappedBy = "user")
    @Builder.Default // to be able to use Builder pattern when add/remove address
    private List<Address> addresses = new ArrayList<>(); // builder pattern will ignore this line of not annotate with @Builder.default

    public void addAddress(Address address) {
        this.addresses.add(address);
        address.setUser(this);
    }

    public void removeAddress(Address address) {
        this.addresses.remove(address);
        address.setUser(null);
    }

    // We set the users table is the owner of the Many-To-Many relationship here by using @JoinTable,
    // both User and Tag can own this Many-To-Many relationship, we have to choose one of them
    @ManyToMany
    @JoinTable(
            name = "user_tags",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="tag_id")
    )
    @Builder.Default
    private Set<Tag> tags = new HashSet<>();

    public void addTag(String tagName) {
        Tag tag = new Tag(tagName);
        this.tags.add(tag);
        tag.getUsers().add(this);
    }

    public void removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.getUsers().remove(this);
    }

    @OneToOne(mappedBy = "user")
    private Profile profile;

    public void addProfile(Profile profile) {
        this.profile = profile;
        profile.setUser(this);
    }

    public void removeProfile(Profile profile) {
        this.profile = null;
        profile.setUser(null);
    }
}
