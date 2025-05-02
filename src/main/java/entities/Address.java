package entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table(name="addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(nullable = false, name="street")
    private String street;

    @Column(nullable = false, name="city")
    private String city;

    @Column(nullable = false, name="state")
    private String state;

    @Column(nullable = false, name="zipcode")
    private String zipcode;

    @ManyToOne
    @JoinColumn(name="user_id") // addresses table owns the relationship by column user_id,
                                // therefore Address entity owns the relationship
    @ToString.Exclude // avoid looping
    private User user;
}
