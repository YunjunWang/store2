package com.yunjun.store2.repositories;

import com.yunjun.store2.dtos.ProfileSummaryDto;
import com.yunjun.store2.dtos.UserSummaryDto;
import com.yunjun.store2.entities.Profile;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProfileRepository extends CrudRepository<Profile, Long> {

//  @EntityGraph(attributePaths = {"user"})
//  @Query("select p from Profile p where p.loyaltyPoints > :loyaltyPoint order by p.user.email")
  @Procedure("findProfilesGreaterThan2")
  List<ProfileSummaryDto> findProfilesGreaterThan(/*@Param("loyaltyPoint") */Long loyaltyPoint);


  @Query("select p from Profile p where p.loyaltyPoints > ?1")
  List<Profile> findByLoyaltyPointsGreaterThan(int loyaltyPoints);

  @Query("select p from Profile p where p.loyaltyPoints > ?1 order by p.user.email")
  @EntityGraph(attributePaths = {"user"})
  List<Profile> findByLoyaltyPointsGreaterThanOrderByUserEmail(int loyaltyPoints);

  @Query("select p from Profile p where p.loyaltyPoints > ?1 order by p.user.email")
  @EntityGraph(attributePaths = {"user"})
  List<Profile> findLoyalProfiles1(int loyaltyPoints);

  @Query("select p.id as id, p.user.email as email from Profile p where p.loyaltyPoints > ?1 order by p.user.email")
  @EntityGraph(attributePaths = {"user"})
  List<UserSummaryDto> findLoyalProfiles2(int loyaltyPoints);
}