package com.yunjun.store2.repositories;

import com.yunjun.store2.entities.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Byte> {
}