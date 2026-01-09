package com.project.back_end.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.project.back_end.models.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    /**
     * Find a patient by their email.
     * @param email Patient email
     * @return Patient entity if found, otherwise null
     */
    Patient findByEmail(String email);

    /**
     * Find a patient by email or phone number.
     * Useful for checking uniqueness or flexible lookups.
     * @param email Patient email
     * @param phone Patient phone
     * @return Patient entity if found, otherwise null
     */
    Patient findByEmailOrPhone(String email, String phone);
}
