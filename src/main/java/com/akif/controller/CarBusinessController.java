package com.akif.controller;

import com.akif.dto.request.CarPriceUpdateRequestDto;
import com.akif.dto.request.CarStatusUpdateRequestDto;
import com.akif.dto.response.CarResponseDto;
import com.akif.service.ICarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cars/business")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Car Business", description = "Business operations for cars")
public class CarBusinessController {

    private final ICarService carService;

    @PostMapping(value = "/{id}/sell")
    @Operation(summary = "Sell car", description = "Mark a car as sold")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car sold successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CarResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Car not found"),
            @ApiResponse(responseCode = "400", description = "Car cannot be sold")
    })
    public ResponseEntity<CarResponseDto> sellCar(
            @Parameter(description = "Car ID", required = true) @PathVariable Long id) {
        CarResponseDto car = carService.sellCar(id);
        return ResponseEntity.ok(car);
    }

    @PostMapping(value = "/{id}/reserve")
    @Operation(summary = "Reserve car", description = "Reserve a car for a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car reserved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CarResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Car not found"),
            @ApiResponse(responseCode = "400", description = "Car cannot be reserved")
    })
    public ResponseEntity<CarResponseDto> reserveCar(
            @Parameter(description = "Car ID", required = true) @PathVariable Long id) {
        CarResponseDto car = carService.reserveCar(id);
        return ResponseEntity.ok(car);
    }

    @PostMapping(value = "/{id}/cancel-reservation")
    @Operation(summary = "Cancel reservation", description = "Cancel car reservation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservation cancelled successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CarResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Car not found"),
            @ApiResponse(responseCode = "400", description = "Car is not reserved")
    })
    public ResponseEntity<CarResponseDto> cancelReservation(
            @Parameter(description = "Car ID", required = true) @PathVariable Long id) {
        CarResponseDto car = carService.cancelReservation(id);
        return ResponseEntity.ok(car);
    }

    @PostMapping(value = "/{id}/maintenance")
    @Operation(summary = "Mark car as maintenance", description = "Mark a car as under maintenance")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car marked as maintenance successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CarResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Car not found"),
            @ApiResponse(responseCode = "400", description = "Invalid car ID")
    })
    public ResponseEntity<CarResponseDto> markAsMaintenance(
            @Parameter(description = "Car ID", required = true) @PathVariable Long id) {
        CarResponseDto car = carService.markAsMaintenance(id);
        return ResponseEntity.ok(car);
    }

    @PostMapping(value = "/{id}/available")
    @Operation(summary = "Mark car as available", description = "Mark a car as available for sale")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car marked as available successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CarResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Car not found"),
            @ApiResponse(responseCode = "400", description = "Invalid car ID")
    })
    public ResponseEntity<CarResponseDto> markAsAvailable(
            @Parameter(description = "Car ID", required = true) @PathVariable Long id) {
        CarResponseDto car = carService.markAsAvailable(id);
        return ResponseEntity.ok(car);
    }

    @PatchMapping(value = "/{id}/status")
    @Operation(summary = "Update car status", description = "Update car status with reason")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car status updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CarResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Car not found"),
            @ApiResponse(responseCode = "400", description = "Invalid status transition")
    })
    public ResponseEntity<CarResponseDto> updateCarStatus(
            @Parameter(description = "Car ID", required = true) @PathVariable Long id,
            @Parameter(description = "Status update data", required = true) @Valid @RequestBody CarStatusUpdateRequestDto statusUpdateRequest) {
        CarResponseDto car = carService.updateCarStatus(id, statusUpdateRequest);
        return ResponseEntity.ok(car);
    }

    @PatchMapping(value = "/{id}/price")
    @Operation(summary = "Update car price", description = "Update car price and currency")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car price updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CarResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Car not found"),
            @ApiResponse(responseCode = "400", description = "Invalid price data")
    })
    public ResponseEntity<CarResponseDto> updateCarPrice(
            @Parameter(description = "Car ID", required = true) @PathVariable Long id,
            @Parameter(description = "Price update data", required = true) @Valid @RequestBody CarPriceUpdateRequestDto priceUpdateRequest) {
        CarResponseDto car = carService.updateCarPrice(id, priceUpdateRequest);
        return ResponseEntity.ok(car);
    }

    @PostMapping(value = "/{id}/view")
    @Operation(summary = "Increment view count", description = "Increment view count for a car")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "View count incremented successfully"),
            @ApiResponse(responseCode = "404", description = "Car not found"),
            @ApiResponse(responseCode = "400", description = "Invalid car ID")
    })
    public ResponseEntity<Void> incrementViewCount(
            @Parameter(description = "Car ID", required = true) @PathVariable Long id) {
        carService.incrementViewCount(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/{id}/like")
    @Operation(summary = "Increment like count", description = "Increment like count for a car")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Like count incremented successfully"),
            @ApiResponse(responseCode = "404", description = "Car not found"),
            @ApiResponse(responseCode = "400", description = "Invalid car ID")
    })
    public ResponseEntity<Void> incrementLikeCount(
            @Parameter(description = "Car ID", required = true) @PathVariable Long id) {
        carService.incrementLikeCount(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{id}/like")
    @Operation(summary = "Decrement like count", description = "Decrement like count for a car")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Like count decremented successfully"),
            @ApiResponse(responseCode = "404", description = "Car not found"),
            @ApiResponse(responseCode = "400", description = "Invalid car ID")
    })
    public ResponseEntity<Void> decrementLikeCount(
            @Parameter(description = "Car ID", required = true) @PathVariable Long id) {
        carService.decrementLikeCount(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{id}/can-sell")
    @Operation(summary = "Check if car can be sold", description = "Check if car can be sold")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Check completed",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Car not found"),
            @ApiResponse(responseCode = "400", description = "Invalid car ID")
    })
    public ResponseEntity<Map<String, Boolean>> canCarBeSold(
            @Parameter(description = "Car ID", required = true) @PathVariable Long id) {
        log.info("GET /api/cars/business/{}/can-sell", id);
        boolean canBeSold = carService.canCarBeSold(id);
        return ResponseEntity.ok(Map.of("canBeSold", canBeSold));
    }

    @GetMapping(value = "/{id}/can-reserve")
    @Operation(summary = "Check if car can be reserved", description = "Check if car can be reserved")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Check completed",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Car not found"),
            @ApiResponse(responseCode = "400", description = "Invalid car ID")
    })
    public ResponseEntity<Map<String, Boolean>> canCarBeReserved(
            @Parameter(description = "Car ID", required = true) @PathVariable Long id) {
        log.info("GET /api/cars/business/{}/can-reserve", id);
        boolean canBeReserved = carService.canCarBeReserved(id);
        return ResponseEntity.ok(Map.of("canBeReserved", canBeReserved));
    }
}
