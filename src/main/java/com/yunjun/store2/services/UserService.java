package com.yunjun.store2.services;

import com.yunjun.store2.entities.User;
import com.yunjun.store2.repositories.AddressRepository;
import com.yunjun.store2.repositories.ProfileRepository;
import com.yunjun.store2.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;
    private EntityManager entityManager;
    private ProfileRepository profileRepository;
    private AddressRepository addressRepository;

    /**
     * User @Transactional to make the transaction scope longer
     * in the persistent context
     */
    @Transactional
    public void showRelatedState() {
        User user = User.builder()
                .name("John")
                .email("john@example.com")
                .password("test")
                .build();
        if (entityManager.contains(user)) {
            System.out.println("persistent");
        } else {
            System.out.println("transitional / detached");
        }

        userRepository.save(user);

        if (entityManager.contains(user)) {
            System.out.println("persistent");
        } else {
            System.out.println("transitional / detached");
        }
    }

    /**
     * The @Transactional makes the transaction scope longer for this whole method
     * if not, when the first query completes,
     * Hibernate won't know which profile to get the user for
     * as the profile object is out of persistent context therefore
     * it is not tracked by Hibernate.
     *
     * The user query by userService will still load the profile inside the user object
     * as user is not the owner of the relationship, the Eager loading fetching strategy
     * is applied by default.
     */
    @Transactional
    public void showRelatedEntities() {
        var user = userRepository.findById(1L).orElseThrow();
        System.out.println(user.getEmail());

        var profile = profileRepository.findById(1L).orElseThrow();
        System.out.println(profile.getBio());
        System.out.println(profile.getUser().getEmail());

        var address1 = addressRepository.findById(1L).orElseThrow();
        System.out.println(address1.getCity());
    }
}
