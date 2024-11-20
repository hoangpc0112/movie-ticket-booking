package com.example.theater.repositories;

import com.example.theater.entities.Food;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository <Food, Long> {
}
