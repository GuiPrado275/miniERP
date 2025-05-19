package com.projetominiERP.miniERP.models.projection;

import java.util.Date;

public interface TripProjection {

    public Long getId();

    public String getDescription();

    public boolean getFinished();

    public Date getStartDate();

    public Date getEndDate();

    public String getOrigin();

    public String getDestination();

    public Double getKmValue();

    public Double getKm();

    public Double getTravelTime();

    public Double getTravelCost();
}
