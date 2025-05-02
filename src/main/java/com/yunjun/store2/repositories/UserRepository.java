package com.yunjun.store2.repositories;

import com.yunjun.store2.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long>{
}
