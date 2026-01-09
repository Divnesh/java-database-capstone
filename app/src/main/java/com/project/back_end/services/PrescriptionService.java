package com.project.back_end.services;

import com.project.back_end.models.Prescription;
import com.project.back_end.repo.PrescriptionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service // 1. Marks this as a Spring-managed service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;

    // 2. Constructor injection for dependencies
    public PrescriptionService(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    /**
     * 3. savePrescription Method
     * Saves a new prescription if one doesn't already exist for the same appointment.
     */
    public ResponseEntity<Map<String, String>> savePrescription(Prescription prescription) {
        Map<String, String> response = new HashMap<>();

        try {
            // Check if a prescription already exists for this appointment
            if (!prescriptionRepository.findByAppointmentId(prescription.getAppointmentId()).isEmpty()) {
                response.put("message", "Prescription already exists for this appointment.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Save the new prescription
            prescriptionRepository.save(prescription);
            response.put("message", "Prescription saved successfully.");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "Error saving prescription.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 4. getPrescription Method
     * Retrieves the prescription for a given appointment.
     */
    public ResponseEntity<Map<String, Object>> getPrescription(Long appointmentId) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Fetch prescription by appointment ID
            Optional<Prescription> prescription = prescriptionRepository.findByAppointmentId(appointmentId)
                    .stream().findFirst();

            if (prescription.isPresent()) {
                response.put("prescription", prescription.get());
                return ResponseEntity.ok(response);
            } else {
                response.put("message", "No prescription found for this appointment.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "Error fetching prescription.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
