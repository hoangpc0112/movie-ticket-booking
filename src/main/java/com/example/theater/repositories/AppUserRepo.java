package com.example.theater.repositories;

import com.example.theater.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepo extends JpaRepository<AppUser, Integer> {
    public AppUser findByUsername(String username);
}
