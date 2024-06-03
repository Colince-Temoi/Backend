package tech.csm.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tech.csm.model.Season;

@Repository
public interface SeasonRepo extends JpaRepository<Season, Integer> {

}
