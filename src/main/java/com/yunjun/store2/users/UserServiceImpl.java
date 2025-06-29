package com.yunjun.store2.users;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final EntityManager entityManager;
    private final ProfileRepository profileRepository;
    private final AddressRepository addressRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * User @Transactional to make the transaction scope longer
     * in the persistent context
     */
    /*@Transactional
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
    }*/

    /**
     * The @Transactional makes the transaction scope longer for this whole method.
     * If not, when the first query completes,
     * Hibernate won't know which profile to get the user for
     * as the profile object is out of persistent context, therefore,
     * it is not tracked by Hibernate.

     * The user query by userService will still load the profile inside the user object
     * as the user is not the owner of the relationship, the Eager loading fetching strategy
     * is applied by default.
     */
    /*@Transactional
    public void showRelatedEntities() {
        var user = userRepository.findById(1L).orElseThrow();
        System.out.println(user.getEmail());

        var profile = profileRepository.findById(1L).orElseThrow();
        System.out.println(profile.getBio());
        System.out.println(profile.getUser().getEmail());

        var address1 = addressRepository.findById(1L).orElseThrow();
        System.out.println(address1.getCity());
    }

    @Transactional
    public void showPersistentState() {
        User user = User.builder()
                .name("persist")
                .email("persist@example.com")
                .password("persist")
                .build();
        Address address = Address.builder()
                .street("123 Main St")
                .city("San Francisco")
                .state("California")
                .zipcode("94107")
                .build();
        Profile profile = Profile.builder()
                .bio("I am a developer")
                .phoneNumber("123-456-7890")
                .user(user)
                .dateOfBirth(java.time.LocalDate.of(1990, 1, 1))
                .loyaltyPoints(1000)
                .build();
        user.addAddress(address);

        userRepository.save(user);
        profileRepository.save(profile);
    }

    public void showDeleteState() {
        User user = userRepository.findById(22L).orElseThrow();
        userRepository.delete(user);
    }

    @Transactional
    public void showDeleteChildEntityState() {
        User user = userRepository.findById(21L).orElseThrow();
        Address address = user.getAddresses().getFirst();
        user.removeAddress(address);
        userRepository.save(user);
    }*/

    /**
     * needed @Transactional when we are using @ToString including the lazy loading attributes
     *
     */
    /*public void fetchUser() {
        var user = userRepository.findByEmail("john1@example.com").orElseThrow();
//        System.out.println(user.getEmail());
        System.out.println(user);
    }

    @Transactional
    public void fetchUsersWithAddresses() {
        var users = userRepository.getAllWithAddresses();
        users.forEach(u -> {
            System.out.println(u);
            u.getAddresses().forEach(System.out::println);
        });
    }

    @Transactional
    public void fetchUsersSummaryWithLoyaltyPointsGreaterThan() {
//        var users = userRepository.findUsersSummaryWithLoyaltyPointsGreaterThan(2);
//        users.forEach(System.out::println);
        var users = userRepository.getLoyalUsers(2);
        users.forEach(u -> {
            System.out.println(u.getId() + ": " + u.getEmail());
        });
    }
*/

    /**
     * @return
     */
    @Override
    public List<UserDto> getAllUsers(String sortBy) {
        if (!Set.of("name", "email").contains(sortBy))
            sortBy = "name";
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
        var user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        return userMapper.toDto(user);
    }

    /**
     * @param request
     * @return
     */
    @Override
    public UserDto registerUser(RegisterUserRequest request) throws UserAlreadyExistsException{
        // We can never decode it when the user login, we'll hash it again to compare with the database
        // password should be encoded in the API layer to avoid inherited security vulnerabilities.
        request.setPassword(passwordEncoder.encode(request.getPassword()));

        if (userRepository.existsUserByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException();
        }
        var user = userMapper.update(request);
        user.setRole(Role.USER);
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    /**
     * @param id
     * @param request
     * @return
     */
    @Override
    public UserDto updateUser(Long id, UpdateUserRequest request) {
       var user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
       userMapper.update(user, request);
       userRepository.save(user);
       return userMapper.toDto(user);
    }

    /**
     * @param id
     */
    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * @param request
     * @param id
     * @return
     */
    @Override
    public UserDto changePassword(ChangePasswordRequest request, Long id) throws IllegalAccessException, IllegalArgumentException {
        var user = userRepository.findById(id).orElseThrow(() -> new IllegalAccessException("User not found"));
        var oldPwd = passwordEncoder.encode(request.getOldPassword());
        if (!user.getPassword().equals(oldPwd) || !user.getEmail().equals(request.getEmail())) {
            throw new IllegalAccessException("Invalid password or username not found");
        }
        if (!request.getNewPassword().equals(request.getConfirmedPassword())) {
            throw new IllegalArgumentException("New password and old password must be the same");
        }

        user.setPassword(request.getNewPassword());
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    /**
     * @param email
     * @return
     */
    @Override
    public UserDto getUserByEmail(String email) {
        var user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        return userMapper.toDto(user);
    }
}
