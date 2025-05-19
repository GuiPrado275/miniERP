package com.projetominiERP.miniERP.repositories;

import com.projetominiERP.miniERP.models.Trip;
import com.projetominiERP.miniERP.models.projection.TripProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

    List<TripProjection> findByUser_id(Long id);

}
