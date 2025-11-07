package com.akif.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PickupRequestDto implements Serializable {

    @Size(max = 1000, message = "Pickup notes cannot exceed 1000 characters")
    private String notes;
}
