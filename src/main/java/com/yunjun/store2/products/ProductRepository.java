package com.yunjun.store2.repositories;

import com.yunjun.store2.dtos.ProductSummaryDto;
import com.yunjun.store2.dtos.ProductSummaryProjection;
import com.yunjun.store2.entities.Category;
import com.yunjun.store2.entities.Product;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductCriteriaRepository, JpaSpecificationExecutor<Product> {

  /**
   * Find products whose prices are in a given range and sort by name
   * Alt + Enter > Extract JPQL Query and Configure...
   *
   * @param min
   * @param max
   * @return
   */
  List<Product> getByPriceBetweenOrderByNameAsc(BigDecimal min, BigDecimal max);

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
  List<Product> getProductsBySql(@Param("min") BigDecimal min, @Param("max") BigDecimal max);

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
  List<Product> getProductsBetweenPrices(@Param("min") BigDecimal min, @Param("max") BigDecimal max);

  /**
   * Create a stored procedure in the database for complicated queries
   * Then use @Procedure annotation to call the stored procedure
   *
   * @param min
   * @param max
   * @return
   */
  @Procedure("getProductsBetweenPrices")
  List<Product> getProductsByPrices(BigDecimal min, BigDecimal max);
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

  /**
   * Default without using projections for partial data,
   * it will search all columns of the Product entity and
   * an eager loading of Category entity
   *
   * @param category
   */
  @Query("select p from Product p where p.category = :category")
  List<Product> getProductsByCategory(@Param("category") Category category);

  /**
   * The package name "dtos" can be "projections" too.
   *
   * Fetch partial data with Projections
   * using @Query annotation
   * and return a list of ProductSummaryProjection objects
   * ProductSummaryProjection is an Interface which is much more convenient than using a class
   *
   * @param category
   * @return
   */
  @Query("select p.id, p.name from Product p where p.category = :category")
  List<ProductSummaryProjection> getProductsP(@Param("category") Category category);

  /**
   * Fetch partial data with Projections
   * using @Query annotation
   * and return a list of ProductSummaryDto objects
   * ProductSummaryDto is a DTO class which requires the full path of the class in the query
   *
   * Only use a DTO class when you need to have more logic for the data
   * @param category
   * @return
   */
  @Query("select new com.yunjun.store2.dtos.ProductSummaryDto(p.id, p.name) from Product p where p.category = :category")
  List<ProductSummaryDto> getProductsD(@Param("category") Category category);

  @EntityGraph(attributePaths = {"category"}) // equivalent as "join fetch p.category", make multi sql queries to be single
  @Query("select p from Product p")
  List<Product> getAllProductsWithCategory();

  @EntityGraph(attributePaths = {"category"}) // make multi sql queries to be single
  List<Product> getProductsByCategoryId(Byte categoryId);
}