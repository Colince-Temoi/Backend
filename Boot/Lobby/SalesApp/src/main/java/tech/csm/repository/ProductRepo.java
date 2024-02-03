package tech.csm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tech.csm.entity.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, Integer> {

}
