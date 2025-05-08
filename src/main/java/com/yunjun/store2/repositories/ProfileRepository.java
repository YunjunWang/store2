package com.yunjun.store2.repositories;

import com.yunjun.store2.dtos.ProfileSummaryDto;
import com.yunjun.store2.entities.Profile;
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


  @Query("select p from Profile p where p.loyaltyPoints > :loyaltyPoints")
  List<Profile> findByLoyaltyPointsGreaterThan(@Param("loyaltyPoints") int loyaltyPoints);

//  @Query("select p from Profile p where p.loyaltyPoints > :loyaltyPoints order by p.user.email")
  @EntityGraph(attributePaths = {"user"})
  List<Profile> findByLoyaltyPointsGreaterThanOrderByUserEmail(@Param("loyaltyPoints") int loyaltyPoints);

  @Query("select p from Profile p where p.loyaltyPoints > :loyaltyPoints order by p.user.email")
  @EntityGraph(attributePaths = {"user"})
  List<Profile> findLoyalProfiles(@Param("loyaltyPoints") int loyaltyPoints);

  /**
   * use 'as field_name' to tell the query what the field name is in the result set
   * otherwise, the field name will be random names in the result set,
   * which is not what we want
   *
   * @param loyaltyPoints
   * @return
   */
  @Query("select p.id as id, p.user.email as email from Profile p where p.loyaltyPoints > :loyaltyPoints order by p.user.email")
  @EntityGraph(attributePaths = {"user"})
  List<ProfileSummaryDto> findLoyalProfileSummaries(@Param("loyaltyPoints") int loyaltyPoints);
}