package com.example.demo.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.time.LocalDate;  // or import java.util.Date;

@Document(collection = "cars")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Car {
    @Id
    private String id;

    private String make;
    private String model;
    private int year;
    private String color;
    private BigDecimal price;
    private String imageUrl;
    private Integer mileage;
    private String fuelType;
    private boolean accidentFree;
    private LocalDate publishDate;
}