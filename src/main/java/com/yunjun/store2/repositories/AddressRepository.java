package com.yunjun.store2.repositories;

import com.yunjun.store2.users.Address;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<Address, Long> {
}