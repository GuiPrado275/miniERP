package com.projetominiERP.miniERP.repositories;

import com.projetominiERP.miniERP.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> { //to search for the user by id

    @Transactional(readOnly = true)
    User findByEmail(String email); //to search for the user by email

}
