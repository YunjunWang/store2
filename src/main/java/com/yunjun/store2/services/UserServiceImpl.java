package com.yunjun.store2.services;

import com.yunjun.store2.dtos.UserDto;
import com.yunjun.store2.entities.Address;
import com.yunjun.store2.entities.Profile;
import com.yunjun.store2.entities.User;
import com.yunjun.store2.mappers.RegisterUserRequest;
import com.yunjun.store2.mappers.UserMapper;
import com.yunjun.store2.repositories.AddressRepository;
import com.yunjun.store2.repositories.ProfileRepository;
import com.yunjun.store2.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
//    private final EntityManager entityManager;
//    private final ProfileRepository profileRepository;
//    private final AddressRepository addressRepository;

    @Autowired
    private final UserMapper userMapper;

//    /**
//     * User @Transactional to make the transaction scope longer
//     * in the persistent context
//     */
//    @Transactional
//    public void showRelatedState() {
//        User user = User.builder()
//                .name("John")
//                .email("john@example.com")
//                .password("test")
//                .build();
//        if (entityManager.contains(user)) {
//            System.out.println("persistent");
//        } else {
//            System.out.println("transitional / detached");
//        }
//
//        userRepository.save(user);
//
//        if (entityManager.contains(user)) {
//            System.out.println("persistent");
//        } else {
//            System.out.println("transitional / detached");
//        }
//    }
//
//    /**
//     * The @Transactional makes the transaction scope longer for this whole method.
//     * If not, when the first query completes,
//     * Hibernate won't know which profile to get the user for
//     * as the profile object is out of persistent context, therefore,
//     * it is not tracked by Hibernate.
//
//     * The user query by userService will still load the profile inside the user object
//     * as the user is not the owner of the relationship, the Eager loading fetching strategy
//     * is applied by default.
//     */
//    @Transactional
//    public void showRelatedEntities() {
//        var user = userRepository.findById(1L).orElseThrow();
//        System.out.println(user.getEmail());
//
//        var profile = profileRepository.findById(1L).orElseThrow();
//        System.out.println(profile.getBio());
//        System.out.println(profile.getUser().getEmail());
//
//        var address1 = addressRepository.findById(1L).orElseThrow();
//        System.out.println(address1.getCity());
//    }
//
//    @Transactional
//    public void showPersistentState() {
//        User user = User.builder()
//                .name("persist")
//                .email("persist@example.com")
//                .password("persist")
//                .build();
//        Address address = Address.builder()
//                .street("123 Main St")
//                .city("San Francisco")
//                .state("California")
//                .zipcode("94107")
//                .build();
//        Profile profile = Profile.builder()
//                .bio("I am a developer")
//                .phoneNumber("123-456-7890")
//                .user(user)
//                .dateOfBirth(java.time.LocalDate.of(1990, 1, 1))
//                .loyaltyPoints(1000)
//                .build();
//        user.addAddress(address);
//
//        userRepository.save(user);
//        profileRepository.save(profile);
//    }
//
//    public void showDeleteState() {
//        User user = userRepository.findById(22L).orElseThrow();
//        userRepository.delete(user);
//    }
//
//    @Transactional
//    public void showDeleteChildEntityState() {
//        User user = userRepository.findById(21L).orElseThrow();
//        Address address = user.getAddresses().getFirst();
//        user.removeAddress(address);
//        userRepository.save(user);
//    }
//
//    /**
//     * needed @Transactional when we are using @ToString including the lazy loading attributes
//     *
//     */
//    public void fetchUser() {
//        var user = userRepository.findByEmail("john1@example.com").orElseThrow();
////        System.out.println(user.getEmail());
//        System.out.println(user);
//    }
//
//    @Transactional
//    public void fetchUsersWithAddresses() {
//        var users = userRepository.findAllWithAddresses();
//        users.forEach(u -> {
//            System.out.println(u);
//            u.getAddresses().forEach(System.out::println);
//        });
//    }
//
//    @Transactional
//    public void fetchUsersSummaryWithLoyaltyPointsGreaterThan() {
////        var users = userRepository.findUsersSummaryWithLoyaltyPointsGreaterThan(2);
////        users.forEach(System.out::println);
//        var users = userRepository.findLoyalUsers(2);
//        users.forEach(u -> {
//            System.out.println(u.getId() + ": " + u.getEmail());
//        });
//    }

    /**
     * @return
     */
    @Override
    public List<UserDto> getAllUsers(String sortBy) {
        return userRepository
                .findAll(Sort.by(sortBy))
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    /**
     * @param id
     * @return
     */
    @Override
    public UserDto getUserById(Long id) {
        var user = userRepository.findById(id).orElse(null);
        return userMapper.toDto(user);
    }

    /**
     * @param request
     * @return
     */
    @Override
    public UserDto createUser(RegisterUserRequest request) {
        User user = userMapper.toEntity(request);
        try {
            user = userRepository.save(user);
            return userMapper.toDto(user);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }

    }
}
