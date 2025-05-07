package com.yunjun.store2.repositories;

import com.yunjun.store2.entities.Product;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Long> {

  /**
   * Find products whose prices are in a given range and sort by name
   * Alt + Enter > Extract JPQL Query and Configure...
   *
   * @param min
   * @param max
   * @return
   */
  List<Product> findByPriceBetweenOrderByNameAsc(BigDecimal min, BigDecimal max);

  /**
   * By SQL: dependent on a specific database engine,
   * not portable across different database engines
   *
   *
   * @param min
   * @param max
   * @return
   */
  @Query(value = "select * from products p where p.price between :min and :max order by p.name", nativeQuery = true)
  List<Product> findProductsBySql(@Param("min") BigDecimal min, @Param("max") BigDecimal max);

  /**
   * By JPQL: database-agnostic,
   * portable across different database engines
   *
   * join, leftjoin etc.
   * @param min
   * @param max
   * @return
   */
  @Query("select p from Product p join p.category where p.price between :min and :max")
  List<Product> findByProducts(@Param("min") BigDecimal min, @Param("max") BigDecimal max);

  /**
   * aggregate functions: count, max, min ...
   * @param min
   * @param max
   * @return
   */
  @Query("select p from Product p where p.price between :min and :max")
  long countProducts(@Param("min")BigDecimal min, @Param("max")BigDecimal max);

  /**
   * Always use @Modifying for update scenarios
   * and use @Transactional for any method calling this method
   *
   * @param categoryId
   * @param price
   */
  @Modifying
  @Query("update Product p set p.price = :price where p.category.id = :categoryId")
  void updatePriceByCategory(Byte categoryId, BigDecimal price);
}