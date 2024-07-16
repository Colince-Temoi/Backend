package tech.get_tt_right.SpringDataJpa.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity

//@NamedQuery(
//        name = "Product.findByPrice",
//        query = "SELECT p FROM Product p WHERE p.price = ?1"
//)

//Defining multiple named JPQL queries
@NamedQueries(
        {
                @NamedQuery(
                        name = "Product.findAllOrderByNameDesc",
                        query = "Select p from Product p ORDER By p.name DESC "
                ),
                @NamedQuery(
                        name = "Product.findByPrice",
                        query = "select p from  Product p where  p.price=:price"
                )
        }
)

//@NamedNativeQuery(
//        name = "Product.findByDescription",
//        query = "SELECT * FROM products p WHERE p.description = ?1",
//        resultClass = Product.class
//
//)

@NamedNativeQueries(
        {
                @NamedNativeQuery(
                        name = "Product.findByDescription",
                        query = "SELECT * FROM products p WHERE p.description = ?1",
                        resultClass = Product.class
                ),
                @NamedNativeQuery(
                        name = "Product.findAllOrderByNameAsc",
                        query = "SELECT * FROM products p ORDER BY p.name ASC",
                        resultClass = Product.class
                )
        }

)

@Table(
        name = "products",
        schema = "ecommerce",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "sku_unique",
                        columnNames = "stock_keeping_unit"
                )
        }
)
public class Product {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "product_generator"
    )
    @SequenceGenerator(
            name = "product_generator",
            sequenceName = "product_sequence_name",
            allocationSize = 1

    )
    private Long id;
    @Column(name = "stock_keeping_unit", nullable = false)
    private String sku;
    @Column(nullable = false)
    private String name;
    private String description;
    private BigDecimal price;
    private boolean active;
    private String imageUrl;
    @CreationTimestamp
    private LocalDateTime dateCreated;
    @UpdateTimestamp
    private LocalDateTime lastUpdated;
}
