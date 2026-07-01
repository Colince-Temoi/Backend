package com.get_tt_right.profile.repository;

import com.get_tt_right.profile.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, String> {

    Optional<Profile> findByMobileNumberAndActiveSw(String mobileNumber, boolean active);

}
