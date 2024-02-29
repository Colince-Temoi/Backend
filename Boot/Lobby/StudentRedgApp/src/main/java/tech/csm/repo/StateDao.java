package tech.csm.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tech.csm.domain.State;

@Repository
public interface StateDao extends JpaRepository<State, Integer> {

}
