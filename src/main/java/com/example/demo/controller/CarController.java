package com.example.demo.controller;

import com.example.demo.model.Car;
import com.example.demo.repository.CarRepository;
import com.example.demo.service.CurrencyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/cars")
public class CarController {

    private final CarRepository carRepository;
    private final CurrencyService currencyService;

    public CarController(CarRepository carRepository, CurrencyService currencyService) {
        this.carRepository = carRepository;
        this.currencyService = currencyService;
    }

    @GetMapping
    public String getAllCars(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        Page<Car> carPage = carRepository.findAll(PageRequest.of(page, size));

        model.addAttribute("cars", carPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", carPage.getTotalPages());
        model.addAttribute("totalElements", carPage.getTotalElements());
        model.addAttribute("pageSize", size);
        model.addAttribute("exchangeRate", currencyService.getEuroToPlnRate());

        return "car-list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        Car car = new Car();
        car.setPublishDate(LocalDate.now()); // Set default to current date
        model.addAttribute("car", car);
        return "add-car";
    }

    @PostMapping("/add")
    public String addCar(@ModelAttribute Car car,
                         @RequestParam(required = false, defaultValue = "false") boolean accidentFree,
                         RedirectAttributes redirectAttributes) {
        car.setAccidentFree(accidentFree);
        if (car.getPublishDate() == null) {
            car.setPublishDate(LocalDate.now()); // Set default if not provided
        }
        carRepository.save(car);
        redirectAttributes.addFlashAttribute("successMessage", "Car added successfully!");
        return "redirect:/cars";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid car Id:" + id));
        model.addAttribute("car", car);
        return "edit-car";
    }

    @PostMapping("/update")
    public String updateCar(@ModelAttribute Car car,
                            @RequestParam(required = false, defaultValue = "false") boolean accidentFree,
                            RedirectAttributes redirectAttributes) {
        car.setAccidentFree(accidentFree);
        carRepository.save(car);
        redirectAttributes.addFlashAttribute("successMessage", "Car updated successfully!");
        return "redirect:/cars";
    }

    @GetMapping("/delete/{id}")
    public String deleteCar(@PathVariable String id, RedirectAttributes redirectAttributes) {
        carRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Car deleted successfully!");
        return "redirect:/cars";
    }

    @GetMapping("/details/{id}")
    public String showCarDetails(@PathVariable String id, Model model) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid car Id:" + id));
        model.addAttribute("car", car);
        return "car-details"; // This must match your Thymeleaf template name
    }

    @GetMapping("/search")
    public String searchCars(
            @RequestParam(required = false) String make,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        Page<Car> carPage;
        Pageable pageable;

        // Create pageable with sorting
        if (sort != null && !sort.isEmpty()) {
            switch (sort) {
                case "price-asc":
                    pageable = PageRequest.of(page, size, Sort.by("price").ascending());
                    break;
                case "price-desc":
                    pageable = PageRequest.of(page, size, Sort.by("price").descending());
                    break;
                case "year-asc":
                    pageable = PageRequest.of(page, size, Sort.by("year").ascending());
                    break;
                case "year-desc":
                    pageable = PageRequest.of(page, size, Sort.by("year").descending());
                    break;
                case "mileage-asc":
                    pageable = PageRequest.of(page, size, Sort.by("mileage").ascending());
                    break;
                case "mileage-desc":
                    pageable = PageRequest.of(page, size, Sort.by("mileage").descending());
                    break;
                default:
                    pageable = PageRequest.of(page, size);
            }
        } else {
            pageable = PageRequest.of(page, size);
        }

        // Filter by make if provided
        if (make != null && !make.isEmpty()) {
            carPage = carRepository.findByMakeContainingIgnoreCase(make, pageable);
        } else {
            carPage = carRepository.findAll(pageable);
        }

        model.addAttribute("cars", carPage.getContent());
        model.addAttribute("searchMake", make);
        model.addAttribute("sort", sort);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", carPage.getTotalPages());
        model.addAttribute("pageSize", size);

        return "car-search";
    }
}