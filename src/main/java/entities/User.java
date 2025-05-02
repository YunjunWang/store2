package entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "user") // table address owns user by user_id
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
}
