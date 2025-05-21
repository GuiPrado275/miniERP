package com.projetominiERP.miniERP.services;

import com.projetominiERP.miniERP.models.Trip;
import com.projetominiERP.miniERP.models.User;
import com.projetominiERP.miniERP.models.enums.ProfileEnum;
import com.projetominiERP.miniERP.models.projection.TripProjection;
import com.projetominiERP.miniERP.repositories.TripRepository;
import com.projetominiERP.miniERP.security.UserSpringSecurity;
import com.projetominiERP.miniERP.services.exceptions.AuthorizationException;
import com.projetominiERP.miniERP.services.exceptions.DataBindingViolationException;
import com.projetominiERP.miniERP.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class TripService {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private UserService userService;

    public Trip findById(Long id){ // if the trip exists show it, else return:
        Trip trip = this.tripRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(
                "Trip not found! Id: " + id + ", Type: " + Trip.class.getName()));

        UserSpringSecurity userSpringSecurity = UserService.authenticated();//to authenticate if user is not logged
        if(Objects.isNull(userSpringSecurity) || !userSpringSecurity.hasRole(ProfileEnum.ADMIN)
                && !userHasTrip(userSpringSecurity, trip)){
            throw new AuthorizationException("Access denied");//to authenticate if user is not logged or not adm,
        }                                                     //or not be the owner of trip
        return trip;
    }

    public List<TripProjection> findAllByUser(Long userId) {
        UserSpringSecurity userSpringSecurity = UserService.authenticated();
        if (userSpringSecurity == null) {
            throw new AuthorizationException("Access denied");
        }

        boolean isAdmin = userSpringSecurity.hasRole(ProfileEnum.ADMIN);

        // a user only can find your taks
        if (!isAdmin) {
            if (userId != null && !userId.equals(userSpringSecurity.getId())) {
                throw new AuthorizationException("You can only view your own trips");
            }
            userId = userSpringSecurity.getId(); //force fetch own id
        }

        //admin must pass a valid user
        if (isAdmin && userId == null) {
            throw new IllegalArgumentException("User ID is mandatory for admin");
        }

        return tripRepository.findByUser_id(userId, isAdmin);
    }

    @Transactional //util for inputs in databases, in create or modification of database
    public Trip create(Trip obj){
        UserSpringSecurity userSpringSecurity = UserService.authenticated(); //to authenticate if user is not logged
        if(Objects.isNull(userSpringSecurity)) {
            throw new AuthorizationException("Access denied");
        }
    User user = userService.findById(userSpringSecurity.getId()); //to ensure that a bad user can't use the
        obj.setId(null);                                           //id in trip creation
        obj.setUser(user);
        obj = this.tripRepository.save(obj);
        return obj;
    }

    @Transactional
    public Trip update(Trip obj){
        Trip newObj = findById(obj.getId()); //to ensure that the trip exist
        newObj.setDescription(obj.getDescription());
        newObj.setStartDate(obj.getStartDate());
        newObj.setEndDate(obj.getEndDate());
        newObj.setOrigin(obj.getOrigin());
        newObj.setDestination(obj.getDestination());
        newObj.setKm(obj.getKm());
        newObj.setTravelTime(obj.getTravelTime());
        newObj.setTravelCost(obj.getTravelCost());

        return this.tripRepository.save(newObj);
    }

    public void delete(Long id){
        findById(id);
        try{
            tripRepository.deleteById(id); //delete trip
        }
        catch (Exception e) {
            throw new DataBindingViolationException("Trip not found! Id: " + id);
        } //if trip has a tasks, the script will not be deleted
    }

    private boolean userHasTrip(UserSpringSecurity userSpringSecurity, Trip trip) { //verify if user have a trip
        return trip.getUser().getId().equals(userSpringSecurity.getId());
    }
}
