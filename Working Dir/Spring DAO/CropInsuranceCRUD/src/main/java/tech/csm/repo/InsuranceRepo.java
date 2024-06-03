package tech.csm.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tech.csm.model.Insurance;

@Repository
public interface InsuranceRepo extends JpaRepository<Insurance, Integer> {
	boolean existsByCrop_CropIdAndAadhaarNo(Integer cropId, Integer aadhaarNo);
}
