package com.projetominiERP.miniERP.repositories;

import com.projetominiERP.miniERP.models.Trip;
import com.projetominiERP.miniERP.models.projection.TripProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

    @Query("SELECT t.id AS id, " +
            "t.description AS description, " +
            "t.finished AS finished, " +
            "t.startDate AS startDate, " +
            "t.endDate AS endDate, " +
            "t.origin AS origin, " +
            "t.destination AS destination, " +
            "t.km AS km, " +
            "t.travelTime AS travelTime, " +
            "t.travelCost AS travelCost " +
            "FROM Trip t WHERE t.user.id = :id")
    List<TripProjection> findByUser_id(@Param("id") Long id);
}
