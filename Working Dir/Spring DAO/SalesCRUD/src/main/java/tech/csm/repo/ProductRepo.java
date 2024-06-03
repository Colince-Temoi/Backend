package tech.csm.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tech.csm.model.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, Integer> {

}
