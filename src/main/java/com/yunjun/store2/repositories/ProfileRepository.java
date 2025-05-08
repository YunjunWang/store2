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
}