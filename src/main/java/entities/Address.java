package entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}
