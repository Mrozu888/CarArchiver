package com.example.demo.repository;

import com.example.demo.model.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface CarRepository extends MongoRepository<Car, String> {

    // For regular search without pagination (keep this for backward compatibility)
    List<Car> findByMakeContainingIgnoreCase(String make);

    // For search with pagination and sorting
    @Query("{ 'make': { $regex: ?0, $options: 'i' } }")
    Page<Car> findByMakeContainingIgnoreCase(String make, Pageable pageable);

    // Alternative approach using query methods
    Page<Car> findByMakeIgnoreCaseContaining(String make, Pageable pageable);
}