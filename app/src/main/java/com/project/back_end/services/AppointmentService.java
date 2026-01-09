package com.project.back_end.services;

import com.project.back_end.DTO.AppointmentDTO;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import com.project.back_end.models.Appointment;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final Service service; // Your shared service for validation
    private final TokenService tokenService;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    // ================= CONSTRUCTOR INJECTION =================
    public AppointmentService(AppointmentRepository appointmentRepository,
                              Service service,
                              TokenService tokenService,
                              PatientRepository patientRepository,
                              DoctorRepository doctorRepository) {
        this.appointmentRepository = appointmentRepository;
        this.service = service;
        this.tokenService = tokenService;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    // ================= BOOK APPOINTMENT =================
    @Transactional
    public int bookAppointment(Appointment appointment) {
        try {
            appointmentRepository.save(appointment);
            return 1; // Success
        } catch (Exception e) {
            e.printStackTrace();
            return 0; // Failure
        }
    }

    // ================= UPDATE APPOINTMENT =================
    @Transactional
    public ResponseEntity<Map<String, String>> updateAppointment(Appointment updatedAppointment) {
        Map<String, String> response = new HashMap<>();

        try {
            Optional<Appointment> optionalAppointment = appointmentRepository.findById(updatedAppointment.getId());
            if (optionalAppointment.isEmpty()) {
                response.put("message", "No appointment available with id: " + updatedAppointment.getId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            Appointment existingAppointment = optionalAppointment.get();

            // Validate patient owns the appointment
            if (!existingAppointment.getPatient().getId().equals(updatedAppointment.getPatient().getId())) {
                response.put("message", "Patient Id mismatch");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Validate doctor availability (example logic)
            boolean isAvailable = service.validateAppointment(updatedAppointment) == 1;

            if (!isAvailable) {
                response.put("message", "Appointment already booked for given time or Doctor not available");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }

            // Update appointment details
            existingAppointment.setAppointmentTime(updatedAppointment.getAppointmentTime());
            existingAppointment.setDoctor(updatedAppointment.getDoctor());
            existingAppointment.setStatus(updatedAppointment.getStatus());
            appointmentRepository.save(existingAppointment);

            response.put("message", "Appointment Updated Successfully");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response.put("message", "Internal Server Error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // ================= CANCEL APPOINTMENT =================
    @Transactional
    public ResponseEntity<Map<String, String>> cancelAppointment(Long appointmentId, String token) {
        Map<String, String> response = new HashMap<>();

        Optional<Appointment> optionalAppointment = appointmentRepository.findById(appointmentId);
        if (optionalAppointment.isEmpty()) {
            response.put("message", "No appointment for given id: " + appointmentId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        String extractedToken = tokenService.extractEmail(token);
        Patient patient = patientRepository.findByEmail(extractedToken);

        if (!Objects.equals(patient.getId(), optionalAppointment.get().getPatient().getId())) {
            response.put("message", "Patient Id mismatch");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        Appointment existingAppointment = optionalAppointment.get();
        appointmentRepository.delete(existingAppointment);
        response.put("message", "Appointment Deleted Successfully");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // ================= GET APPOINTMENTS =================
    @Transactional(readOnly = true)
    public Map<String, Object> getAppointments(String patientName, LocalDate dateTime, String token ) {
        Map<String, Object> map = new HashMap<>();
        String extractedEmail = tokenService.extractEmail(token);

        long doctorId = doctorRepository.findByEmail(extractedEmail).getId();
        LocalDateTime startOfDay = dateTime.atStartOfDay();
        LocalDateTime endOfDay = dateTime.atTime(LocalTime.MAX);
        List<Appointment> appointments;
        System.out.println("doctorId: "+ doctorId);
        System.out.println("startOfDay: "+ startOfDay);
        System.out.println("startOfDay: "+ endOfDay);
        System.out.println("patientName: "+ patientName.equals("null") );

        if (patientName.equals("null")) {
            appointments = appointmentRepository
                    .findByDoctorIdAndAppointmentTimeBetween(doctorId, startOfDay, endOfDay);
        } else {
            appointments = appointmentRepository
                    .findByDoctorIdAndPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(
                            doctorId, patientName, startOfDay, endOfDay);
        }

        System.out.println("appointments: "+ appointments.size());
        List<AppointmentDTO> appointmentDTOs = appointments.stream()
                .map(app -> new AppointmentDTO(
                        app.getId(),
                        app.getDoctor().getId(), // Simplified doctor info
                        app.getDoctor().getName(),
                        app.getPatient().getId(),
                        app.getPatient().getName(),
                        app.getPatient().getEmail(),
                        app.getPatient().getPhone(),
                        app.getPatient().getAddress(),
                        app.getAppointmentTime(),
                        app.getStatus()))
                .collect(Collectors.toList());

        map.put("appointments", appointmentDTOs);
        return map;
    }

    // ================= CHANGE STATUS =================
    @Transactional
    public void changeStatus(Long appointmentId, int status) {
        try {
            appointmentRepository.updateStatus(status, appointmentId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
