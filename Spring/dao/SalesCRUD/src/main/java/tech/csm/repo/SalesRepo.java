package tech.csm.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tech.csm.model.Sales;

@Repository
public interface SalesRepo extends JpaRepository<Sales, Integer> {

}
