package com.project.back_end.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.project.back_end.models.Prescription;

import java.util.List;

@Repository
public interface PrescriptionRepository extends MongoRepository<Prescription, String> {
    /**
     * Find all prescriptions associated with a specific appointment.
     * @param appointmentId The ID of the appointment
     * @return List of prescriptions
     */
    List<Prescription> findByAppointmentId(Long appointmentId);
}
