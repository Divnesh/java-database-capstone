package com.project.back_end.services;

import com.project.back_end.DTO.AppointmentDTO;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.PatientRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final TokenService tokenService;

    // 2. Constructor injection for dependencies
    public PatientService(PatientRepository patientRepository,
                          AppointmentRepository appointmentRepository,
                          TokenService tokenService) {
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;
    }

    // 3. Create a new patient
    public int createPatient(Patient patient) {
        try {
            patientRepository.save(patient);
            return 1; // success
        } catch (Exception e) {
            e.printStackTrace();
            return 0; // failure
        }
    }

    // 4. Get all appointments for a patient as DTOs
    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, Object>> getPatientAppointments(Long patientId) {
        Map<String, Object> map = new HashMap<>();

        try {
            List<Appointment> appointments = appointmentRepository.findByPatientId(patientId);
            List<AppointmentDTO> appointmentDTOs = appointments.stream()
                    .map(AppointmentDTO::new) // Convert to DTO
                    .collect(Collectors.toList());

            map.put("appointments", appointmentDTOs);
            return ResponseEntity.status(HttpStatus.OK).body(map);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("error", "Internal Server Error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
        }
    }

    // 5. Filter appointments by condition (past/future)
    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, Object>> filterByCondition(Long patientId, String condition) {
        Map<String, Object> map = new HashMap<>();

        int status = condition.equalsIgnoreCase("past") ? 1 : 0;
        try {
            List<Appointment> appointments = appointmentRepository
                    .findByPatient_IdAndStatusOrderByAppointmentTimeAsc(patientId, status);
            List<AppointmentDTO> appointmentDTOs = appointments.stream()
                    .map(AppointmentDTO::new)
                    .collect(Collectors.toList());

            map.put("appointments", appointmentDTOs);
            return ResponseEntity.status(HttpStatus.OK).body(map);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("error", "Internal Server Error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
        }
    }

    // 6. Filter appointments by doctor's name
    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, Object>> filterByDoctor(Long patientId, String doctorName) {
        Map<String, Object> map = new HashMap<>();

        try {
            List<Appointment> appointments = appointmentRepository
                    .filterByDoctorNameAndPatientId(doctorName, patientId);
            List<AppointmentDTO> appointmentDTOs = appointments.stream()
                    .map(AppointmentDTO::new)
                    .collect(Collectors.toList());

            map.put("appointments", appointmentDTOs);
            return ResponseEntity.status(HttpStatus.OK).body(map);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("error", "Internal Server Error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
        }
    }

    // 7. Filter by doctor and condition
    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, Object>> filterByDoctorAndCondition(Long patientId, String doctorName, String condition) {
        Map<String, Object> map = new HashMap<>();
        int status = condition.equalsIgnoreCase("past") ? 1 : 0;

        try {
            List<Appointment> appointments = appointmentRepository
                    .filterByDoctorNameAndPatientIdAndStatus(doctorName, patientId, status);
            List<AppointmentDTO> appointmentDTOs =  appointments.stream()
                    .map(AppointmentDTO::new)
                    .collect(Collectors.toList());

            map.put("appointments", appointmentDTOs);
            return ResponseEntity.status(HttpStatus.OK).body(map);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("error", "Internal Server Error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
        }
    }

    // 8. Get patient details by token
    public ResponseEntity<Map<String,Object>> getPatientDetails(String token) {
        Map<String, Object> map = null;
        try {
            map = new HashMap<>();
            String email = tokenService.extractEmail(token);
            Patient patient = patientRepository.findByEmail(email);
            map.put("patient", patient);
            return ResponseEntity.status(HttpStatus.OK).body(map);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("error", "Internal Server Error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
        }
    }
}
