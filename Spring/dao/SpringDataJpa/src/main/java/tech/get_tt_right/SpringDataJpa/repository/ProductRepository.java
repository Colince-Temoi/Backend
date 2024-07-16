package tech.get_tt_right.SpringDataJpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tech.get_tt_right.SpringDataJpa.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Returns the found product by using  its name as a search criteria.
     * If no product is found, this method returns null.
     */
    Product findByName(String name);

    /* *
     * Returns the found product by using its id as a search criteria.
     * If no product is found, this method returns an empty optional.
     * */
    Optional<Product> findById(Long id);

    /**
     * Returns the found list of products entries whose name or description is given as method parameters.
     * If no product entries are found, this method returns an empty list.
     */
    List<Product> findByNameOrDescription(String name, String description);

    /**
     * Returns the found list of products entries whose name and description is given as method parameters.
     * If no product entries are found, this method returns an empty list.
     */
    List<Product> findByNameAndDescription(String name, String description);

    /**
     * Returns the distinct product entry whose name is given as method parameter.
     * If no product entry is found, this method returns null.
     */
    Product findDistinctByName(String name);

    /**
     * Returns the product whose price is greater than the given price as method parameter.
     */
    List<Product> findByPriceGreaterThan(BigDecimal price);

    /**
     * Returns the product whose price is less than the given price as method parameter.
     */
    List<Product> findByPriceLessThan(BigDecimal price);

    /**
     * Return the filtered product records that match a given name text as a search criteria.
     * *
     */
    List<Product> findByNameContaining(String name);

    /**
     * Return the filtered product records based on SQL Like clause.
     * *
     */
    List<Product> findByNameLike(String name);

    /**
     * Return the filtered product records whose price is between the given price range. Start price and end price are given as method parameters.
     * *
     */
    List<Product> findByPriceBetween(BigDecimal startPrice, BigDecimal endPrice);

    /**
     * Return the filtered product records whose date created between start date and end date. Start date and end date are given as method parameters.
     * *
     */
    List<Product> findByDateCreatedBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Return the filtered product records based on multiple names as search criteria.
     * *
     */
    List<Product> findByNameIn(List<String> names);

    /**
     * Return the first 2 product records by order by name in ASC order.
     * *
     */
    List<Product> findFirst2ByOrderByNameAsc();

    /**
     * Return the top 3 product records by order by price in DESC order.
     * *
     */
    List<Product> findTop3ByOrderByPriceDesc();

    /**
     * Define JPQL query using @Query annotation with index or positional parameters. Find by name or description.
     * * *
     */
    @Query("SELECT p FROM Product p WHERE p.name = ?1 OR p.description = ?2")
    Product getProductByNameOrDescription(String name, String description);

    /**
     * Define JPQL query using @Query annotation with named parameters. Find by name or description.
     * * *
     */
    @Query("SELECT p FROM Product p WHERE p.name = :name OR p.description = :description")
    Product getProductByNameOrDescriptionNamedParams(@Param("name") String name, @Param("description") String description);

    /**
     * Define native query using @Query annotation with index or positional parameters. Find by name or description.
     * * * *
     */
    @Query(
            value = "SELECT * FROM products WHERE name = ?1 OR description = ?2",
            nativeQuery = true
    )
    Product getProductByNameOrDescriptionNativeQuery(String name, String description);

    /**
     * Define native query using @Query annotation with named parameters. Find by name or description.
     * * * *
     */
    @Query(
            value = "SELECT * FROM products p WHERE p.name = :name OR p.description = :description",
            nativeQuery = true
    )
    Product getProductByNameOrDescriptionNamedParamsNativeQuery(@Param("name") String name,
                                                                @Param("description") String description);

    /**
     * Define named JPQL query. Find by price.
     */
    Product findByPrice(BigDecimal price);

    /**
     * Define named JPQL query. findAllOrderByDesc.
     */
    List<Product> findAllOrderByNameDesc();

    /**
     * Define named native query. Find by description.
     */
    @Query(nativeQuery = true)
    Product findByDescription(String description);

    /**
     * Define named native query. Find all order by name in ASC order.
     */
    @Query(nativeQuery = true)
    List<Product> findAllOrderByNameAsc();
}