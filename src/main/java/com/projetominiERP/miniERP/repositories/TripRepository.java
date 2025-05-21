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

    @Query("""
    SELECT 
        t.id AS id,
        t.user.id AS userId,
        t.description AS description,
        t.origin AS origin,
        t.destination AS destination,
        t.startDate AS startDate,
        t.endDate AS endDate,
        t.finished AS finished,
        t.travelTime AS travelTime,
        t.km AS km,
        t.travelCost AS travelCost
    FROM Trip t
    WHERE 
        (:isAdmin = false AND t.user.id = :userId) OR 
        (:isAdmin = true AND t.user.id = :userId)      
""")
    List<TripProjection> findByUser_id(@Param("userId") Long userId,
                                       @Param("isAdmin") boolean isAdmin);
}