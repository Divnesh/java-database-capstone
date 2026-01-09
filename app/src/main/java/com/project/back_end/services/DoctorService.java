package com.project.back_end.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.back_end.models.Doctor;
import com.project.back_end.models.Appointment;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.services.TokenService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final TokenService tokenService;

    // ================= CONSTRUCTOR INJECTION =================
    public DoctorService(DoctorRepository doctorRepository,
                         AppointmentRepository appointmentRepository,
                         TokenService tokenService) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;
    }

    // ================= GET DOCTOR AVAILABILITY =================
    @Transactional(readOnly = true)
    public List<String> getDoctorAvailability(Long doctorId, LocalDate date) {
        Optional<Doctor> optionalDoctor = doctorRepository.findById(doctorId);
        if (optionalDoctor.isEmpty()) return Collections.emptyList();

        Doctor doctor = optionalDoctor.get();
        List<String> allSlots = doctor.getAvailableTimes(); // assume Set<LocalTime>
        // Convert LocalDate to LocalDateTime (start and end of the day)
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        List<Appointment> bookedAppointments = appointmentRepository
                .findByDoctorIdAndAppointmentTimeBetween(
                        doctorId,
                        startOfDay,
                        endOfDay
                );

        Set<LocalTime> bookedSlots = bookedAppointments.stream()
                .map(appt -> appt.getAppointmentTime().toLocalTime())
                .collect(Collectors.toSet());

        return allSlots.stream()
                .filter(slot -> !bookedSlots.contains(slot))
                .sorted()
                .collect(Collectors.toList());
    }

    // ================= SAVE DOCTOR =================
    @Transactional
    public int saveDoctor(Doctor doctor) {
        try {
            if (doctorRepository.findByEmail(doctor.getEmail()) != null) {
                return -1; // Conflict
            }
            doctorRepository.save(doctor);
            return 1; // Success
        } catch (Exception e) {
            e.printStackTrace();
            return 0; // Error
        }
    }

    // ================= UPDATE DOCTOR =================
    @Transactional
    public int updateDoctor(Doctor doctor) {
        Optional<Doctor> existing = doctorRepository.findById(doctor.getId());
        if (existing.isEmpty()) {
            return -1; // Doctor not found
        }
        doctorRepository.save(doctor);
        return 1;
    }

    // ================= GET ALL DOCTORS =================
    @Transactional(readOnly = true)
    public List<Doctor> getDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();

        doctors.forEach(doc -> doc.getAvailableTimes().size());
        return doctors;
    }

    // ================= DELETE DOCTOR =================
    @Transactional
    public int deleteDoctor(Long doctorId) {
        Optional<Doctor> doctor = doctorRepository.findById(doctorId);
        if (doctor.isEmpty()) return -1;

        // Delete associated appointments first
        appointmentRepository.deleteAllByDoctorId(doctorId);
        doctorRepository.deleteById(doctorId);
        return 1;
    }

    // ================= VALIDATE DOCTOR LOGIN =================
    public ResponseEntity<Map<String, String>> validateDoctor(String email, String password) {
        Map<String, String> map = new HashMap<>();

        Doctor doctor = doctorRepository.findByEmail(email);
        if (doctor == null) {
            map.put("error", "invalid email id");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(map);
        }

        if (!doctor.getPassword().equals(password)) {
            map.put("error", "Password is invalid");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(map);
        }

        // Generate JWT token
        map.put("token", tokenService.generateToken(doctor.getEmail()));
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    // ================= FIND DOCTOR BY NAME =================
    @Transactional(readOnly = true)
    public Map<String, Object> findDoctorByName(String name) {
        Map<String, Object> map = new HashMap<>();

        List<Doctor> doctorList = doctorRepository.findByNameLike(name);
        doctorList.forEach(doc -> doc.getAvailableTimes().size());
        map.put("doctors", doctorList);
        return map;
    }

    // ================= FILTER DOCTORS =================
    @Transactional(readOnly = true)
    public Map<String, Object>  filterDoctorsByNameSpecialtyAndTime(String name, String specialty, String timePeriod) {
        Map<String, Object> map = new HashMap<>();

        List<Doctor> filtered = doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);
        List<Doctor> filteredDoctors = filterDoctorByTime(filtered,timePeriod);
        map.put("doctors", filteredDoctors);
        return map;
    }

    @Transactional(readOnly = true)
    public  List<Doctor> filterDoctorByTime(List<Doctor> doctors, String timePeriod) {
        List<Doctor> result = new ArrayList<>();

        for (Doctor doctor : doctors) {
            boolean hasSlot = doctor.getAvailableTimes().stream()
                    .map(timeStr -> {
                        try {
                            return LocalTime.parse(timeStr); // convert string to LocalTime
                        } catch (Exception e) {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .anyMatch(slot -> (timePeriod.equalsIgnoreCase("AM") && slot.getHour() < 12) ||
                            (timePeriod.equalsIgnoreCase("PM") && slot.getHour() >= 12));

            if (hasSlot) {
                result.add(doctor);
            }
        }

        return result;
    }

    @Transactional(readOnly = true)
    public  Map<String, Object> filterDoctorByNameAndTime(String name, String timePeriod) {
        Map<String, Object> map = new HashMap<>();

        List<Doctor> doctors = doctorRepository.findByNameLike(name);

        List<Doctor> filteredDoctors = filterDoctorByTime(doctors,timePeriod);
        map.put("doctors", filteredDoctors);
        return map;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> filterDoctorByNameAndSpecialty(String name, String specialty) {
        Map<String, Object> map = new HashMap<>();

        List<Doctor> doctorList = doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);
        doctorList.forEach(doc -> doc.getAvailableTimes().size());
        map.put("doctors", doctorList);
        return map;
    }

    @Transactional(readOnly = true)
    public Map<String, Object>filterDoctorByTimeAndSpecialty(String timePeriod, String specialty) {
        Map<String, Object> map = new HashMap<>();

        List<Doctor> doctors = doctorRepository.findBySpecialtyIgnoreCase(specialty);
        List<Doctor> filteredDoctors = filterDoctorByTime(doctors,timePeriod);
        map.put("doctors", filteredDoctors);
        return map;
    }

    @Transactional
    public Map<String, Object> filterDoctorBySpecility(String specilty) {
        Map<String, Object> map = new HashMap<>();

        List<Doctor> doctorList = doctorRepository.findBySpecialtyIgnoreCase(specilty);
        doctorList.forEach(doc -> doc.getAvailableTimes().size());
        map.put("doctors", doctorList);
        return map;
    }

    @Transactional
    public Map<String, Object> filterDoctorsByTime(String amOrPm) {
        Map<String, Object> map = new HashMap<>();

        List<Doctor> doctors = doctorRepository.findAll();
        List<Doctor> filteredDoctors = filterDoctorByTime(doctors,amOrPm);
        map.put("doctors", filteredDoctors);
        return map;
    }
}
