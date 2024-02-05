package tech.csm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tech.csm.entity.Sale;

@Repository
public interface SaleRepo extends JpaRepository<Sale, Integer> {

}
