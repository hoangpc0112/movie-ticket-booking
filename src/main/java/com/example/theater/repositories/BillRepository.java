package com.example.theater.repositories;

import com.example.theater.entities.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository <Bill, Long> {
}