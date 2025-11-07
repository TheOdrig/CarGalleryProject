package com.akif.controller;

import com.akif.dto.request.PickupRequestDto;
import com.akif.dto.request.RentalRequestDto;
import com.akif.dto.request.ReturnRequestDto;
import com.akif.dto.response.RentalResponseDto;
import com.akif.service.IRentalService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rentals")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Rental Management", description = "Operations for managing car rentals")
public class RentalController {

    private final IRentalService rentalService;

    @PostMapping("/request")
    @Operation(summary = "Request a rental", description = "Create a new rental request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Rental request created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RentalResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid rental data"),
            @ApiResponse(responseCode = "404", description = "Car not found"),
            @ApiResponse(responseCode = "409", description = "Date overlap or car not available")
    })
    public ResponseEntity<RentalResponseDto> requestRental(
            @Parameter(description = "Rental request data", required = true)
            @Valid @RequestBody RentalRequestDto request,
            Authentication authentication) {

        String username = authentication.getName();
        log.info("POST /api/rentals/request - User: {}", username);

        RentalResponseDto rental = rentalService.requestRental(request, username);

        log.info("Rental request created successfully. RentalId: {}", rental.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(rental);
    }


    @PostMapping("/{id}/confirm")
    @Operation(summary = "Confirm rental", description = "Admin confirms a rental request and authorizes payment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rental confirmed successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RentalResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid rental state"),
            @ApiResponse(responseCode = "404", description = "Rental not found"),
            @ApiResponse(responseCode = "402", description = "Payment failed")
    })
    public ResponseEntity<RentalResponseDto> confirmRental(
            @Parameter(description = "Rental ID", required = true)
            @PathVariable Long id) {

        log.info("POST /api/rentals/{}/confirm", id);

        RentalResponseDto rental = rentalService.confirmRental(id);

        log.info("Rental confirmed successfully. RentalId: {}", id);
        return ResponseEntity.ok(rental);
    }


    @PostMapping("/{id}/pickup")
    @Operation(summary = "Pickup rental", description = "Admin processes car pickup and captures payment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pickup processed successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RentalResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid rental state"),
            @ApiResponse(responseCode = "404", description = "Rental not found")
    })
    public ResponseEntity<RentalResponseDto> pickupRental(
            @Parameter(description = "Rental ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Pickup notes")
            @RequestBody(required = false) PickupRequestDto request) {

        log.info("POST /api/rentals/{}/pickup", id);

        String notes = (request != null) ? request.getNotes() : null;
        RentalResponseDto rental = rentalService.pickupRental(id, notes);

        log.info("Pickup processed successfully. RentalId: {}", id);
        return ResponseEntity.ok(rental);
    }


    @PostMapping("/{id}/return")
    @Operation(summary = "Return rental", description = "Admin processes car return")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return processed successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RentalResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid rental state"),
            @ApiResponse(responseCode = "404", description = "Rental not found")
    })
    public ResponseEntity<RentalResponseDto> returnRental(
            @Parameter(description = "Rental ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Return notes")
            @RequestBody(required = false) ReturnRequestDto request) {

        log.info("POST /api/rentals/{}/return", id);

        String notes = (request != null) ? request.getNotes() : null;
        RentalResponseDto rental = rentalService.returnRental(id, notes);

        log.info("Return processed successfully. RentalId: {}", id);
        return ResponseEntity.ok(rental);
    }


    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancel rental", description = "Cancel a rental (user can cancel own, admin can cancel any)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rental cancelled successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RentalResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid rental state"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Rental not found")
    })
    public ResponseEntity<RentalResponseDto> cancelRental(
            @Parameter(description = "Rental ID", required = true)
            @PathVariable Long id,
            Authentication authentication) {

        String username = authentication.getName();
        log.info("POST /api/rentals/{}/cancel - User: {}", id, username);

        RentalResponseDto rental = rentalService.cancelRental(id, username);

        log.info("Rental cancelled successfully. RentalId: {}", id);
        return ResponseEntity.ok(rental);
    }


    @GetMapping("/me")
    @Operation(summary = "Get my rentals", description = "Get current user's rental list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rentals retrieved successfully",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Page<RentalResponseDto>> getMyRentals(
            @Parameter(description = "Pagination information")
            @PageableDefault(size = 20) Pageable pageable,
            Authentication authentication) {

        String username = authentication.getName();
        log.debug("GET /api/rentals/me - User: {}", username);

        Page<RentalResponseDto> rentals = rentalService.getMyRentals(username, pageable);

        log.info("Retrieved {} rentals for user: {}", rentals.getTotalElements(), username);
        return ResponseEntity.ok(rentals);
    }


    @GetMapping("/admin")
    @Operation(summary = "Get all rentals (Admin)", description = "Get all rentals (admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rentals retrieved successfully",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<Page<RentalResponseDto>> getAllRentals(
            @Parameter(description = "Pagination information")
            @PageableDefault(size = 20) Pageable pageable) {

        log.debug("GET /api/rentals/admin");

        Page<RentalResponseDto> rentals = rentalService.getAllRentals(pageable);

        log.info("Retrieved {} rentals", rentals.getTotalElements());
        return ResponseEntity.ok(rentals);
    }


    @GetMapping("/{id}")
    @Operation(summary = "Get rental by ID", description = "Get rental details (user can view own, admin can view any)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rental found successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RentalResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Rental not found")
    })
    public ResponseEntity<RentalResponseDto> getRentalById(
            @Parameter(description = "Rental ID", required = true)
            @PathVariable Long id,
            Authentication authentication) {

        String username = authentication.getName();
        log.debug("GET /api/rentals/{} - User: {}", id, username);

        RentalResponseDto rental = rentalService.getRentalById(id, username);

        log.info("Retrieved rental: {}", id);
        return ResponseEntity.ok(rental);
    }
}
