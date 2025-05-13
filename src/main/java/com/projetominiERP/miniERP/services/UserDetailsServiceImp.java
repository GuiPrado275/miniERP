package com.projetominiERP.miniERP.services;

import com.projetominiERP.miniERP.models.User;
import com.projetominiERP.miniERP.repositories.UserRepository;
import com.projetominiERP.miniERP.security.UserSpringSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = this.userRepository.findByEmail(email);
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("User not found: " + email);
        }
        return new UserSpringSecurity(user.getId(), user.getEmail(), user.getPassword(), user.getProfiles());
        //This go to UserDetaisService that transforms the ID,username, password and profiles in authorities
    }

}