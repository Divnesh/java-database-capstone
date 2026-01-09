package com.project.back_end.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.project.back_end.models.Doctor;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    /**
     * Find a doctor by their email.
     * @param email Doctor's email
     * @return Doctor entity if found, otherwise null
     */
    Doctor findByEmail(String email);

    /**
     * Find doctors whose name contains the given string (case-sensitive).
     * @param name Partial name to search for
     * @return List of matching doctors
     */
    List<Doctor> findByNameLike(String name);

    /**
     * Find doctors by name containing a string (case-insensitive)
     * and specialty (case-insensitive exact match).
     * @param name Partial name
     * @param specialty Specialty
     * @return List of matching doctors
     */
    List<Doctor> findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(String name, String specialty);

    /**
     * Find doctors by specialty (case-insensitive).
     * @param specialty Specialty
     * @return List of matching doctors
     */
    List<Doctor> findBySpecialtyIgnoreCase(String specialty);
}
