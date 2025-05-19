package com.projetominiERP.miniERP.services;

import com.projetominiERP.miniERP.models.User;
import com.projetominiERP.miniERP.models.dto.UserCreateDTO;
import com.projetominiERP.miniERP.models.dto.UserUpdateDTO;
import com.projetominiERP.miniERP.models.enums.ProfileEnum;
import com.projetominiERP.miniERP.repositories.UserRepository;
import com.projetominiERP.miniERP.security.UserSpringSecurity;
import com.projetominiERP.miniERP.services.exceptions.AuthorizationException;
import com.projetominiERP.miniERP.services.exceptions.DataBindingViolationException;
import com.projetominiERP.miniERP.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    public User findById(Long id) { //search user by id
        UserSpringSecurity userSpringSecurity = authenticated(); //authenticate user
        if (!Objects.nonNull(userSpringSecurity)
        || !userSpringSecurity.hasRole(ProfileEnum.ADMIN) && !id.equals(userSpringSecurity.getId()))
            throw new AuthorizationException("Access denied!"); //to ensure that the user is authenticated and
                                                                //has the right to access the resource
        Optional<User> user = this.userRepository.findById(id); //search user by id
        return user.orElseThrow(() -> new ObjectNotFoundException(
                "User is not found! Id: " + id + ", Typo: " + User.class.getName()));
    }

    @Transactional
    public User create (User obj) { //create user
        obj.setId(null);
        obj.setPassword(this.bCryptPasswordEncoder.encode(obj.getPassword())); //the password is encrypted
        obj.setProfiles(Stream.of(ProfileEnum.USER.getCode()).collect(Collectors.toSet()));  //the user created is USER
        obj = this.userRepository.save(obj); //save the user
        return obj;
    }

    @Transactional
    public User update(User obj) { //update user
        User newObj = findById(obj.getId()); //to ensure that the user exist
        newObj.setPassword(obj.getPassword()); //update pawssword
        newObj.setPassword(this.bCryptPasswordEncoder.encode(obj.getPassword())); //to encrypt the password
        return this.userRepository.save(newObj); //save the user updated
    }

    public void delete(Long id) { //to delete the user
        findById(id); //search user by id
        try {
            this.userRepository.deleteById(id); //to delete id
        } catch (Exception e) { //if it don't has a relationship with other entities
            throw new DataBindingViolationException("Cannot delete because this user has an entities relationship!");
            //error to delete the user
        }
    }

    public static UserSpringSecurity authenticated(){
        try {
            return (UserSpringSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            return null;
        }
    }

    public User fromDTO(@Valid UserCreateDTO obj) {
        User user = new User();
        user.setEmail(obj.getEmail());
        user.setPassword(obj.getPassword());
        return user;
    }

    public User fromDTO(@Valid UserUpdateDTO obj) {
        User user = new User();
        user.setId(obj.getId());
        user.setPassword(obj.getPassword());
        return user;
    }
}
