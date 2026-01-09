package com.project.back_end.DTO;

public class Login {

    // ================= FIELDS =================
    private String email;
    private String password;

    // ================= CONSTRUCTORS =================
    public Login() {
    }

    public Login(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // ================= GETTERS & SETTERS =================
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
