package tech.csm.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tech.csm.model.Crop;

@Repository
public interface CropRepo extends JpaRepository<Crop, Integer> {
	List<Crop> findBySeasonSeasonId(Integer seasonId);
}
