package com.yunjun.store2.repositories;

import com.yunjun.store2.entities.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {
  }