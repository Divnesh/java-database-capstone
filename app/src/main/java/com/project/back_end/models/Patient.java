package com.project.back_end.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "patients")
public class Patient {

    // 1. Primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 2. Patient full name
    @NotNull(message = "Patient name is mandatory")
    @Size(min = 3, max = 100, message = "Patient name must be between 3 and 100 characters")
    @Column(nullable = false)
    private String name;

    // 3. Email address
    @NotNull(message = "Email is mandatory")
    @Email(message = "Email must be a valid email address")
    @Column(nullable = false, unique = true)
    private String email;

    // 4. Password
    @NotNull(message = "Password is mandatory")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    @Column(nullable = false)
    private String password;

    // 5. Phone number
    @NotNull(message = "Phone number is mandatory")
    @Pattern(regexp = "^[0-9]{10}$", message = "phone number must be exactly 10 digits")
    @Column(nullable = false)
    private String phone;

    // 6. Address
    @NotNull(message = "Address is mandatory")
    @Size(max = 255, message = "Address must not exceed 255 characters")
    @Column(nullable = false, length = 255)
    private String address;

    // No-argument constructor required by JPA
    public Patient() {
    }

    // Optional parameterized constructor
    public Patient(String name, String email, String password,
                   String phone, String address) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
    }

    // 7. Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Consider hashing before persisting
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
