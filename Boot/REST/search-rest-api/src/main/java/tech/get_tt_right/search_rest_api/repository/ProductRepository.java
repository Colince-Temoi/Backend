package tech.get_tt_right.search_rest_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tech.get_tt_right.search_rest_api.entity.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // JPQL Query to search for products by name or description containing the query string passed as a parameter
    /*
    * WHERE p.name LIKE CONCAT('%', :query, '%'): This condition filters products where the name field contains the string passed as the query parameter.
    * The LIKE operator is used for pattern matching, and CONCAT('%', :query, '%') is used to create a pattern that matches any product name containing the query string anywhere within it.
    * The % symbol is a wildcard that matches any sequence of characters.
    * */
    @Query("SELECT p FROM Product p WHERE " +
            "p.name LIKE CONCAT('%',:query, '%')" +
            "Or p.description LIKE CONCAT('%', :query, '%')")
    List<Product> searchProducts(String query);

    // Native SQL Query to search for products by name or description containing the query string passed as a parameter
    @Query(value = "SELECT * FROM products p WHERE " +
            "p.name LIKE CONCAT('%',:query, '%')" +
            "Or p.description LIKE CONCAT('%', :query, '%')", nativeQuery = true)
    List<Product> searchProductsSQL(String query);
}
