package com.project.back_end.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.project.back_end.services.Service;

@Controller
public class DashboardController {

    // ================= AUTOWIRE SHARED SERVICE =================
    @Autowired
    private Service service;

    // ================= ADMIN DASHBOARD =================
    @GetMapping("/adminDashboard/{token}")
    public ModelAndView adminDashboard(@PathVariable("token") String token) {
        // Validate token for admin role
        String validationError = service.validateToken(token, "admin");

        if (validationError == null || validationError.isEmpty()) {
            // Token valid → forward to admin dashboard view
            return new ModelAndView("admin/adminDashboard");
        } else {
            // Token invalid → redirect to root/login page
            return new ModelAndView("redirect:/");
        }
    }

    // ================= DOCTOR DASHBOARD =================
    @GetMapping("/doctorDashboard/{token}")
    public ModelAndView doctorDashboard(@PathVariable("token") String token) {
        // Validate token for doctor role
        String validationError = service.validateToken(token, "doctor");

        if (validationError == null || validationError.isEmpty()) {
            // Token valid → forward to doctor dashboard view
            return new ModelAndView("doctor/doctorDashboard");
        } else {
            // Token invalid → redirect to root/login page
            return new ModelAndView("redirect:/");
        }
    }
}