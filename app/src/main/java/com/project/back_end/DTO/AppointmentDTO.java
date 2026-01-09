package com.project.back_end.DTO;

import com.project.back_end.models.Appointment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AppointmentDTO {

    // ================= FIELDS =================
    private Long id;                 // Appointment unique ID
    private Long doctorId;           // Doctor's ID
    private String doctorName;       // Doctor's name
    private Long patientId;          // Patient's ID
    private String patientName;      // Patient's name
    private String patientEmail;     // Patient's email
    private String patientPhone;     // Patient's phone
    private String patientAddress;   // Patient's address
    private LocalDateTime appointmentTime; // Full appointment datetime
    private int status;              // Appointment status (0=Scheduled, 1=Completed, etc.)

    // ================= DERIVED FIELDS =================
    private LocalDate appointmentDate;     // Date part of appointmentTime
    private LocalTime appointmentTimeOnly; // Time part of appointmentTime
    private LocalDateTime endTime;         // appointmentTime + 1 hour

    // ================= CONSTRUCTOR =================
    public AppointmentDTO(Long id, Long doctorId, String doctorName,
                          Long patientId, String patientName, String patientEmail,
                          String patientPhone, String patientAddress,
                          LocalDateTime appointmentTime, int status) {
        this.id = id;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.patientId = patientId;
        this.patientName = patientName;
        this.patientEmail = patientEmail;
        this.patientPhone = patientPhone;
        this.patientAddress = patientAddress;
        this.appointmentTime = appointmentTime;
        this.status = status;

        // Derived fields
        if (appointmentTime != null) {
            this.appointmentDate = appointmentTime.toLocalDate();
            this.appointmentTimeOnly = appointmentTime.toLocalTime();
            this.endTime = appointmentTime.plusHours(1);
        }
    }

    public AppointmentDTO(Appointment appointment) {
        this.id = appointment.getId();
        this.doctorId = appointment.getDoctor().getId();
        this.doctorName = appointment.getDoctor().getName();
        this.patientId = appointment.getPatient().getId();
        this.patientName = appointment.getPatient().getName();
        this.patientEmail = appointment.getPatient().getEmail();
        this.patientPhone = appointment.getPatient().getPhone();
        this.patientAddress = appointment.getPatient().getAddress();
        this.appointmentTime = appointment.getAppointmentTime();
        this.status = appointment.getStatus();
        this.appointmentDate = appointmentTime.toLocalDate();
        this.appointmentTimeOnly = appointmentTime.toLocalTime();
        this.endTime = appointmentTime.plusHours(1);
    }

    // ================= GETTERS =================
    public Long getId() { return id; }
    public Long getDoctorId() { return doctorId; }
    public String getDoctorName() { return doctorName; }
    public Long getPatientId() { return patientId; }
    public String getPatientName() { return patientName; }
    public String getPatientEmail() { return patientEmail; }
    public String getPatientPhone() { return patientPhone; }
    public String getPatientAddress() { return patientAddress; }
    public LocalDateTime getAppointmentTime() { return appointmentTime; }
    public int getStatus() { return status; }

    // ================= DERIVED GETTERS =================
    public LocalDate getAppointmentDate() { return appointmentDate; }
    public LocalTime getAppointmentTimeOnly() { return appointmentTimeOnly; }
    public LocalDateTime getEndTime() { return endTime; }
}
