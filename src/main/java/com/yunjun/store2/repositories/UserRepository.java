package com.yunjun.store2.repositories;

import com.yunjun.store2.dtos.UserSummaryDto;
import com.yunjun.store2.entities.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Use JpaRepository instead of CrudRepository to be able to return a List of User objects
 */
public interface UserRepository extends JpaRepository<User, Long>{

    /**
     * @EntityGraph annotation can be used to fetch multiple entities at once
     * that only allow this query to fetch the specified entities as if Eager loading was used.
     *
     * attributePaths = "addresses"
     * attributePaths = {"tag", "addresses"}
     * attributePaths = {"tag", "addresses.country"} for nested entity if address has country
     *
     * @param email
     * @return
     */
    @EntityGraph(attributePaths = {/*"tags",*/"addresses"})
    @Query("select u from User u where u.email = :email")
    Optional<User> findByEmail(String email);


    /**
     * @EntityGraph annotation can be used to fetch multiple entities at once
     * to avoid the N+1 problem
     *
     * @return
     */
    @EntityGraph(attributePaths = {"addresses"})
    @Query("select u from User u")
    List<User> findAllWithAddresses();

    @Procedure("findUsersWithLoyaltyPointGreaterThan")
    List<UserSummaryDto> findUsersSummaryWithLoyaltyPointsGreaterThan(Integer loyaltyPoint);

    @Query("select u.id as id, u.email as email from User u left join Profile p on p.id = u.id where p.loyaltyPoints > :loyaltyPoints order by u.email")
//    @EntityGraph(attributePaths = {"profile"})
    List<UserSummaryDto> findLoyalUsers(@Param("loyaltyPoints") int loyaltyPoints);

    void deleteById(Long id);

    boolean existsUserByEmail(String email);
}
