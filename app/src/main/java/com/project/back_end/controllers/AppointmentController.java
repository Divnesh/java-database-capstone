package com.project.back_end.controllers;

import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.Service;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController // 1. Defines this class as a REST controller
@RequestMapping("/appointments") // 1. Base path for all appointment-related endpoints
public class AppointmentController {

    private final AppointmentService appointmentService; // 2. Handles appointment-specific logic
    private final Service service; // 2. General shared service (e.g., token validation)

    // 2. Constructor injection for dependencies
    @Autowired
    public AppointmentController(AppointmentService appointmentService, Service service) {
        this.appointmentService = appointmentService;
        this.service = service;
    }

    // 3. Fetch appointments for a doctor by date and optional patient name
    @GetMapping("/{date}/{patientName}/{token}")
    public ResponseEntity<Map<String,Object>> getAppointments(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @PathVariable String patientName,
            @PathVariable String token) {

        Map<String, Object> map = new HashMap<>();
        ResponseEntity<Map<String,String>> tempMap= service.validateToken(token, "doctor");
        if (!tempMap.getBody().isEmpty()) {
            map.putAll(tempMap.getBody());
            return new ResponseEntity<>(map, tempMap.getStatusCode());
        }
        map = appointmentService.getAppointments(patientName, date, token);
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    // 4. Book a new appointment for a patient
    @PostMapping("/book/{token}")
    public ResponseEntity<Map<String, String>> bookAppointment(
            @RequestBody @Valid Appointment appointment,
            @PathVariable String token) {

         ResponseEntity<Map<String, String>> tempMap = service.validateToken(token, "patient");
        if (!tempMap.getBody().isEmpty()) {
            return tempMap;
        }

        Map<String, String> response = new HashMap<>();
        int out = service.validateAppointment(appointment);
        if (out == 1) {
            int res = appointmentService.bookAppointment(appointment);
            if (res == 1) {
                response.put("message", "Appointment Booked Successfully");
                return ResponseEntity.status(HttpStatus.CREATED).body(response); // 201 Created

            }
            response.put("message", "Internal Server Error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // 409 Conflict

        } else if (out == -1) {
            response.put("message", "Invalid doctor id");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        response.put("message", "Appointment already booked for given time or Doctor not available");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // 5. Update an existing appointment
    @PutMapping("/update/{token}")
    public ResponseEntity<Map<String, String>> updateAppointment(
            @RequestBody Appointment appointment,
            @PathVariable String token) {
        ResponseEntity<Map<String, String>> tempMap = service.validateToken(token, "patient");

        if (!tempMap.getBody().isEmpty()) {
            return tempMap;
        }
        return appointmentService.updateAppointment(appointment);
    }

    // 6. Cancel an appointment
    @DeleteMapping("/cancel/{appointmentId}/{token}")
    public ResponseEntity<Map<String, String>> cancelAppointment(
            @PathVariable Long appointmentId,
            @PathVariable String token) {
        ResponseEntity<Map<String, String>> tempMap = service.validateToken(token, "patient");
        if (!tempMap.getBody().isEmpty()) {
            return tempMap;
        }
        return appointmentService.cancelAppointment(appointmentId, token);
    }

}
