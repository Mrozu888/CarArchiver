package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "cars")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Make is required")
    @Size(max = 50, message = "Make cannot exceed 50 characters")
    @Column(nullable = false, length = 50)
    private String make;

    @NotBlank(message = "Model is required")
    @Size(max = 50, message = "Model cannot exceed 50 characters")
    @Column(nullable = false, length = 50)
    private String model;

    @Min(value = 1886, message = "Year must be after 1886 (first car invented)")
    @Max(value = 2100, message = "Year must be before 2100")
    @Column(nullable = false)
    private int year;

    @Size(max = 30, message = "Color cannot exceed 30 characters")
    @Column(length = 30)
    private String color;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Price must have up to 10 integer and 2 fraction digits")
    @Column(precision = 12, scale = 2)  // Increased precision for larger values
    private BigDecimal price;

    @Size(max = 1024, message = "Image URL cannot exceed 1024 characters")
    @Column(name = "image_url", length = 512)
    private String imageUrl;

    @Min(value = 0, message = "Mileage cannot be negative")
    @Column
    private Integer mileage;

    @Size(max = 30, message = "Fuel type cannot exceed 30 characters")
    @Column(name = "fuel_type", length = 30)
    private String fuelType;

    @Column(name = "accident_free")
    private boolean accidentFree;

    @PastOrPresent(message = "Publish date cannot be in the future")
    @Column(name = "publish_date")
    private LocalDate publishDate;

}