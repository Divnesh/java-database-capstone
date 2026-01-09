package com.project.back_end.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.project.back_end.models.Admin;

/**
 * Repository interface for Admin entity.
 * Provides CRUD operations and custom queries for Admin.
 */
@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    /**
     * Find an admin by their username.
     * @param username the username of the admin
     * @return the Admin entity if found, otherwise null
     */
    Admin findByUsername(String username);
}
