package tech.csm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tech.csm.domain.Crop;

public interface CropRepo extends JpaRepository<Crop, Integer> {

}
