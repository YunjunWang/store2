package com.yunjun.store2.products;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Repository
public class ProductCriteriaRepositoryImpl implements ProductCriteriaRepository{

    @PersistenceContext
    private final EntityManager entityManager;
    /**
     * Using CriteriaQuery to create dynamic queries by Java code
     *
     * Disadvantages:
     * Code is verbose and not reusable.
     *
     * @param name
     * @param minPrice
     * @param maxPrice
     * @return
     */
//    @EntityGraph(attributePaths = {"category"})
    @Override
    public List<Product> getProductsWithCriteria(String name, BigDecimal minPrice, BigDecimal maxPrice) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> cq = cb.createQuery(Product.class);
        Root<Product> root = cq.from(Product.class);

        List<Predicate> predicates = new ArrayList<>();
        if (name != null) {
            // name like %<name_value>%
            predicates.add(cb.like(root.get("name"), "%" + name + "%"));
        }

        if (minPrice != null) {
           // price >= minPrice
            predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
        }

        if (maxPrice != null) {
            // price <= maxPrice
            predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
        }

        cq.select(root).where(predicates.toArray(new Predicate[predicates.size()]));

        return entityManager.createQuery(cq).getResultList();
    }

    /**
     * @param category
     * @return
     */
    @Override
    public List<Product> getProductsWithCriteriaByCategory(Category category) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> cq = cb.createQuery(Product.class);
        Root<Product> root = cq.from(Product.class);

        if (category != null) {
            cq.select(root).where(cb.equal(root.get("category"), category));
            return entityManager.createQuery(cq).getResultList();
        }
        return List.of(); // what to do when category == null? throw exceptions?
    }
}
