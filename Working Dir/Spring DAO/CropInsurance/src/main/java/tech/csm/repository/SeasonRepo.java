package tech.csm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tech.csm.domain.Season;

public interface SeasonRepo extends JpaRepository<Season, Integer> {

}
