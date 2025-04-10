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
            @RequestParam(required = false) String make,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        Pageable pageable = createPageable(page, size, sort);
        Page<Car> carPage = getFilteredCars(make, pageable);

        model.addAttribute("cars", carPage.getContent());
        model.addAttribute("searchMake", make);
        model.addAttribute("sort", sort);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", carPage.getTotalPages());
        model.addAttribute("pageSize", size);
        model.addAttribute("exchangeRate", currencyService.getEuroToPlnRate());

        return "car-search";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        Car car = new Car();
        car.setPublishDate(LocalDate.now());
        model.addAttribute("car", car);
        return "add-car";
    }

    @PostMapping("/add")
    public String addCar(@ModelAttribute Car car,
                         @RequestParam(required = false, defaultValue = "false") boolean accidentFree,
                         RedirectAttributes redirectAttributes) {
        car.setAccidentFree(accidentFree);
        if (car.getPublishDate() == null) {
            car.setPublishDate(LocalDate.now());
        }
        carRepository.save(car);
        redirectAttributes.addFlashAttribute("successMessage", "Car added successfully!");
        return "redirect:/cars";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid car Id: " + id));
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
    public String deleteCar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        carRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Car deleted successfully!");
        return "redirect:/cars";
    }

    @GetMapping("/details/{id}")
    public String showCarDetails(@PathVariable Long id, Model model) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid car Id: " + id));
        model.addAttribute("car", car);
        return "car-details";
    }

    @GetMapping("/search")
    public String searchCars(
            @RequestParam(required = false) String make,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        Pageable pageable = createPageable(page, size, sort);
        Page<Car> carPage = getFilteredCars(make, pageable);

        model.addAttribute("cars", carPage.getContent());
        model.addAttribute("searchMake", make);
        model.addAttribute("sort", sort);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", carPage.getTotalPages());
        model.addAttribute("pageSize", size);
        model.addAttribute("exchangeRate", currencyService.getEuroToPlnRate());

        return "car-search";
    }

    private Pageable createPageable(int page, int size, String sort) {
        if (sort == null || sort.isEmpty()) {
            return PageRequest.of(page, size);
        }

        return switch (sort) {
            case "price-asc" -> PageRequest.of(page, size, Sort.by("price").ascending());
            case "price-desc" -> PageRequest.of(page, size, Sort.by("price").descending());
            case "year-asc" -> PageRequest.of(page, size, Sort.by("year").ascending());
            case "year-desc" -> PageRequest.of(page, size, Sort.by("year").descending());
            case "mileage-asc" -> PageRequest.of(page, size, Sort.by("mileage").ascending());
            case "mileage-desc" -> PageRequest.of(page, size, Sort.by("mileage").descending());
            default -> PageRequest.of(page, size);
        };
    }

    private Page<Car> getFilteredCars(String make, Pageable pageable) {
        if (make != null && !make.isEmpty()) {
            return carRepository.findByMakeContainingIgnoreCase(make, pageable);
        }
        return carRepository.findAll(pageable);
    }
}