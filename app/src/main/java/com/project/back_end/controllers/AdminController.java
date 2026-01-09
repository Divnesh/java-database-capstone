package com.project.back_end.controllers;

import com.project.back_end.models.Admin;
import com.project.back_end.services.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController // 1. Marks this class as a REST controller returning JSON responses
@RequestMapping("${api.path}admin") // 1. Base path for all endpoints in this controller
public class AdminController {

    private final Service service; // 2. Service dependency

    // 2. Constructor injection for cleaner code and testability
    @Autowired
    public AdminController(Service service) {
        this.service = service;
    }

    // 3. Admin login endpoint
    @PostMapping
    public  ResponseEntity<Map<String, String>>  adminLogin(@RequestBody Admin admin) {
        // Delegates authentication to the service layer
        return service.validateAdmin(admin.getUsername(), admin.getPassword());
    }

    @GetMapping("/dashboard/{token}")
    public String adminDashboard(@PathVariable String token)
    {
        Map<String, String> map=service.validateToken(token,"admin").getBody();
        if(map==null)
        {
            return "admin/adminDashboard";
        }
        return "redirect:http://localhost:8080/";
    }
}
