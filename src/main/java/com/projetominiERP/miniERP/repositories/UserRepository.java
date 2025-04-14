package com.projetominiERP.miniERP.repositories;

import com.projetominiERP.miniERP.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> { //para busca o id, email...

    @Transactional(readOnly = true)
    User findByEmail(String email); //para buscar o usuário pelo email

}
