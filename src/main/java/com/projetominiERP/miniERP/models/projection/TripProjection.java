package com.projetominiERP.miniERP.models.projection;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.time.LocalDate;
import java.time.LocalTime;

@JsonPropertyOrder({
        "id",
        "user_id",
        "description",
        "origin",
        "destination",
        "startDate",
        "endDate",
        "finished",
        "travelTime",
        "km",
        "travelCost"
})
public interface TripProjection {

    Long getId();

    @JsonProperty("user_id")
    Long getUserId();

    String getDescription();

    Boolean getFinished();

    LocalDate getStartDate();

    LocalDate getEndDate();

    String getOrigin();

    String getDestination();

    Double getKm();

    LocalTime getTravelTime();

    Double getTravelCost();

    //to calculate the cost of travel
    default Double getKmValue() {
        Double travelCost = getTravelCost();
        Double km = getKm();
        return (travelCost != null && travelCost != 0 && km != null)
                ? km / travelCost
                : null;
    }
}