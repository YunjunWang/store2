package com.yunjun.store2.repositories;

import com.yunjun.store2.entities.Profile;
import org.springframework.data.repository.CrudRepository;

public interface ProfileRepository extends CrudRepository<Profile, Long> {
  }