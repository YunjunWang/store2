package com.yunjun.store2.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.*;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
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

    // The users table being owned by addresses, therefore, User entity is owned by Address entity by Address's field user
    @OneToMany(mappedBy = "user")
    @Builder.Default
    @ToString.Exclude // to be able to use the Builder pattern when add/remove address
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
    @ToString.Exclude
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

    @ManyToMany
    @JoinTable(
            name = "wishlist",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    @Builder.Default
    @ToString.Exclude
    private Set<Product> wishlist = new HashSet<>();

    public void addWishlist(Product product) {
        this.wishlist.add(product);
    }

    public void removeWishlist(Product product) {
        this.wishlist.remove(product);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        User user = (User) o;
        return getId() != null && Objects.equals(getId(), user.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
