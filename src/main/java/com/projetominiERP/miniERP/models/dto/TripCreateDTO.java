package com.projetominiERP.miniERP.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TripCreateDTO {

    @Size(min = 1, max = 255, message = "Description must be between 1 and 255 characters.")
    @NotBlank(message = "Description cannot be blank.")
    private String description;

    @NotNull(message = "Start date is required.")
    private Date startDate;

    @NotNull(message = "End date is required.")
    private Date endDate;

    @Size(min = 1, max = 50, message = "Origin must be between 1 and 20 characters.")
    @NotBlank(message = "Origin cannot be blank.")
    private String origin;

    @Size(min = 1, max = 50, message = "Destination must be between 1 and 20 characters.")
    @NotBlank(message = "Destination cannot be blank.")
    private String destination;

    @NotNull(message = "KM is required.")
    private Double km;

    @NotNull(message = "Travel time is required.")
    private Double travelTime;

    @NotNull(message = "Travel cost is required.")
    private Double travelCost;

}
