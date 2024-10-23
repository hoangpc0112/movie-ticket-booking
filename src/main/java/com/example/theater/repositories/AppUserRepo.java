package com.example.theater.repositories;

import com.example.theater.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepo extends JpaRepository < AppUser, Long > {
    AppUser findByUsername ( String username );

    AppUser findByEmail ( String email );

    boolean existsByEmail ( String email );
}
