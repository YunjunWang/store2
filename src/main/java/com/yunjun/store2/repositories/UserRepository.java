package com.yunjun.store2.repositories;

import com.yunjun.store2.entities.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long>{

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
    @EntityGraph(attributePaths = {"tags","addresses"})
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
}
