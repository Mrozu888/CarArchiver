package com.example.demo.repository;

import com.example.demo.model.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
    Page<Car> findByMakeContainingIgnoreCase(String make, Pageable pageable);
}