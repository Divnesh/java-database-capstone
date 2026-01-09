package com.project.back_end.services;

import com.project.back_end.DTO.AppointmentDTO;
import com.project.back_end.models.Admin;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@org.springframework.stereotype.Service
public class Service {

    private final TokenService tokenService;
    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final DoctorService doctorService;
    private final PatientService patientService;

    // 2. Constructor injection
    public Service(TokenService tokenService,
                   AdminRepository adminRepository,
                   DoctorRepository doctorRepository,
                   PatientRepository patientRepository,
                   DoctorService doctorService,
                   PatientService patientService) {
        this.tokenService = tokenService;
        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    // 3. validateToken: checks if JWT token is valid for a specific role
    public ResponseEntity<Map<String, String>> validateToken(String token, String role) {
        Map<String, String> response = new HashMap<>();
        try {
            if (!tokenService.validateToken(token, role)) {
                response.put("error", "Invalid or expired token");
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } catch (Exception e) {
            response.put("error", "Internal Server Error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 4. validateAdmin: login validation for admin
    public ResponseEntity<Map<String, String>> validateAdmin(String username, String password) {
        Map<String, String> map = new HashMap<>();
        try {
            Admin admin = adminRepository.findByUsername(username);
            if (admin == null) {
                map.put("error", "invalid email id");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(map);
            }
            if (!admin.getPassword().equals(password)) {
                map.put("error", "Password does not match");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(map);
            }

            map.put("token", tokenService.generateToken(admin.getUsername()));
            return ResponseEntity.status(HttpStatus.OK).body(map);
        } catch (Exception e) {
            map.put("error", "Internal Server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
        }
    }

    // 5. filterDoctor: flexible filtering by name, specialty, and time (AM/PM)
    public Map<String, Object> filterDoctor(String name, String specialty, String time) {
        Map<String, Object> map = new HashMap<>();
        if (!name.equals("null") && !time.equals("null") && !specialty.equals("null")) {
            map = doctorService.filterDoctorsByNameSpecialtyAndTime(name, specialty, time);
        }
        else if (!name.equals("null") && !time.equals("null")) {
            map = doctorService.filterDoctorByNameAndTime(name, time);
        } else if (!name.equals("null") && !specialty.equals("null")) {
            map = doctorService.filterDoctorByNameAndSpecialty(name, specialty);
        } else if (!specialty.equals("null") && !time.equals("null")) {
            map = doctorService.filterDoctorByTimeAndSpecialty(specialty, time);
        } else if (!name.equals("null")) {
            map = doctorService.findDoctorByName(name);
        } else if (!specialty.equals("null")) {
            map = doctorService.filterDoctorBySpecility(specialty);
        } else if (!time.equals("null")) {
            map = doctorService.filterDoctorsByTime(time);
        } else {
            map.put("doctors", doctorService.getDoctors());
        }
        return map;
    }

    // 6. validateAppointment: check if doctor is available at requested time
    // Returns: 1 = valid, 0 = invalid, -1 = doctor not found
    public int validateAppointment(Appointment appointment) {
        Doctor doctor = doctorRepository.findById(appointment.getDoctor().getId()).orElse(null);
        if (doctor == null) return -1;

        LocalDate appointmentDate = appointment.getAppointmentDate();
        LocalTime appointmentTime = appointment.getAppointmentTimeOnly();
        List<String> availableTimes = doctorService.getDoctorAvailability(doctor.getId(), appointmentDate);

        for (String timeSlot : availableTimes) {
            String[] times = timeSlot.split("-");
            LocalTime startTime = LocalTime.parse(times[0]);

            if (appointmentTime.equals(startTime)) {
                return 1; // The appointment time matches the start time of an available slot
            }
        }
        return 0;
    }

    // 7. validatePatient: check if email/phone is unique
    public boolean validatePatient(String email, String phone) {
        Patient patient = patientRepository.findByEmailOrPhone(email, phone);
        return patient == null;
    }

    // 8. validatePatientLogin: checks login credentials and returns token
    public ResponseEntity<Map<String, String>> validatePatientLogin(String email, String password) {
        Map<String, String> map = new HashMap<>();

        try {
            Patient patient = patientRepository.findByEmail(email);
            if (patient == null) {
                map.put("error", "invalid email id");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(map);
            }
            if (!patient.getPassword().equals(password)) {
                map.put("error", "Password does not match");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(map);
            }

            map.put("token", tokenService.generateToken(email));
            return ResponseEntity.status(HttpStatus.OK).body(map);
        } catch (Exception e) {
            map.put("error", "Internal Server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
        }
    }

    // 9. Filter patient's appointment history
    public ResponseEntity<Map<String,Object>> filterPatient(String token, String condition, String doctorName) {
        try {
            String extractedEmail = tokenService.extractEmail(token);
            Long patientId = patientRepository.findByEmail(extractedEmail).getId();

            if(doctorName.equals("null") && !condition.equals("null")) {
                return patientService.filterByCondition(patientId, condition);
            } else if(condition.equals("null")&& !doctorName.equals("null")) {
                return patientService.filterByDoctor(patientId, doctorName);
            } else if(!condition.equals("null")) {
                return patientService.filterByDoctorAndCondition(patientId, doctorName, condition);
            } else {
                return patientService.getPatientAppointments(patientId);
            }
        } catch (Exception e) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "Internal Server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
        }
    }
}



