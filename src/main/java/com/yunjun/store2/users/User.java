package com.yunjun.store2.users;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
//@ToString
@Entity
@Table(name="users")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, name="name")
    private String name;

    @Column(nullable = false, name="email")
    private String email;

    @Column(nullable = false, name="password")
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    // The users table being owned by addresses, therefore, User entity is owned by Address entity by Address's field user
    // Use CascadeType.PERSIST to make sure the address is persisted when the user is persisted in the database
    // Use CascadeType.REMOVE to make sure the address is removed when the user is removed from the database
    // Use orphanRemoval to make sure the address is removed when the user is removed from the database
    // If we don't use @Builder.Default, the addresses field will be null when we use the Builder pattern to create a new User object
    // and the addresses field will be empty when we use the constructor to create a new User object
    // because the addresses field is not annotated with @Builder.Default
    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    @Builder.Default
    private List<Address> addresses = new ArrayList<>(); // builder pattern will ignore this line of not annotate with @Builder.default


    public void addAddress(Address address) {
        this.addresses.add(address);
        address.setUser(this);
    }

    public void removeAddress(Address address) {
        this.addresses.remove(address);
        address.setUser(null);
    }

    // We set the "users" table is the owner of the Many-To-Many relationship here by using @JoinTable;
    // Both of the User and Tag can own this Many-To-Many relationship, we have to choose one of them.
    // Not used in the second part of the course.
    /*@ManyToMany
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
    }*/

    /*
      Can't apply Lazy loading fetching strategy on the non-owner entity
      of the relationship

      Since User is only for user accounts,
      there's no need to load the profile information in the user account;
      the best here is to remove the code, only have a one-directional relationship
      in the owner entity.
     */
//    @OneToOne(mappedBy = "user"/*, fetch = FetchType.LAZY*/, cascade = {CascadeType.REMOVE})
//    private Profile profile;
//
//    public void addProfile(Profile profile) {
//        this.profile = profile;
//        profile.setUser(this);
//    }
//
//    public void removeProfile(Profile profile) {
//        this.profile = null;
//        profile.setUser(null);
//    }

    /*@ManyToMany
    @JoinTable(
            name = "wishlist",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    @Builder.Default
    private Set<Product> wishlist = new HashSet<>();

    public void addWishlist(Product product) {
        this.wishlist.add(product);
    }

    public void removeWishlist(Product product) {
        this.wishlist.remove(product);
    }*/

/*    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        User user = (User) o;
        return getId() != null && Objects.equals(getId(), user.getId());
    }*/
/*
    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }*/

    /**
     * avoid queries for lazy loading attributes
     * which will cause lazy loading initialization exception
     *
     * @return
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "email = " + email + ")";
    }
}
