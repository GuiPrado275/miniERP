package com.projetominiERP.miniERP.controllers;

import com.projetominiERP.miniERP.models.Trip;
import com.projetominiERP.miniERP.models.projection.TripProjection;
import com.projetominiERP.miniERP.security.UserSpringSecurity;
import com.projetominiERP.miniERP.services.TripService;
import com.projetominiERP.miniERP.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/trip")
@Validated
public class TripController {

    @Autowired
    private TripService tripService;

    @GetMapping("/{id}")
    public ResponseEntity<Trip> findById(@PathVariable Long id) { //find trip by id
        Trip obj = tripService.findById(id);
        return ResponseEntity.ok().body(obj); //.ok() - If there is no error, continue
    }                                         //.body() - Is the "body" of response, local of data

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TripProjection>> getTripsByUser(
            @PathVariable Long userId) {

        List<TripProjection> trips = tripService.findAllByUser(userId);
        return ResponseEntity.ok(trips);
    }

    //a normal user
    @GetMapping("/user/me")
    public ResponseEntity<List<TripProjection>> getMyTrips() {
        UserSpringSecurity user = UserService.authenticated();
        return getTripsByUser(user.getId());
    }

    @PostMapping
    @Validated
    public ResponseEntity<Void> create(@Valid @RequestBody Trip obj){
        this.tripService.create(obj);                                              //to create trip
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}") //build /trip/{id} (trip id)
                .buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();                                //201 or void
    }

    @PutMapping("/{id}")
    @Validated
    public ResponseEntity<Void> update(@Valid @RequestBody Trip obj, @PathVariable Long id){
        obj.setId(id);
        this.tripService.update(obj);               //to update trip
        return ResponseEntity.noContent().build(); //noContend() because we are not returning any data, only updating
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        this.tripService.delete(id);                            //to delete trip
        return  ResponseEntity.noContent().build();
    }

}
