package com.projetominiERP.miniERP.models.projection;

import java.util.Date;

public interface TripProjection {

    Long getId();

    String getDescription();

    Boolean getFinished();

    Date getStartDate();

    Date getEndDate();

    String getOrigin();

    String getDestination();

    Double getKm();

    Integer getTravelTime();

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