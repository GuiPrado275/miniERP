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

    public User findById(Long id) { //buscar o usuário pelo id
        UserSpringSecurity userSpringSecurity = authenticated(); //autenticar o usuário
        if (!Objects.nonNull(userSpringSecurity)
        || !userSpringSecurity.hasRole(ProfileEnum.ADMIN) && !id.equals(userSpringSecurity.getId()))
            throw new AuthorizationException("Access denied!"); //garantir quem vai acessar o id

        Optional<User> user = this.userRepository.findById(id); //buscar o ID
        return user.orElseThrow(() -> new ObjectNotFoundException(
                "User is not found! Id: " + id + ", Typo: " + User.class.getName()));
    }

    @Transactional
    public User create (User obj) { //criar o usuário
        obj.setId(null);
        obj.setPassword(this.bCryptPasswordEncoder.encode(obj.getPassword())); //a senha é criptografada
        obj.setProfiles(Stream.of(ProfileEnum.USER.getCode()).collect(Collectors.toSet()));  //o usuario criado é USER
        obj = this.userRepository.save(obj); //salvar o usuário
        return obj;
    }

    @Transactional
    public User update(User obj) { //atualizar o usuário
        User newObj = findById(obj.getId()); //garantir que o usuário existe
        newObj.setPassword(obj.getPassword()); //atualizar a senha
        newObj.setPassword(this.bCryptPasswordEncoder.encode(obj.getPassword())); //criptografar a senha
        return this.userRepository.save(newObj); //salvar o usuário atualizado
    }

    public void delete(Long id) { //deletar o usuário
        findById(id); //buscar o ID
        try {
            this.userRepository.deleteById(id); //deletar o ID
        } catch (Exception e) { //se tiver enitdades relacionadas, não deletar
            throw new DataBindingViolationException("Cannot delete because this user has an entities relationship!");
            //se não conseguir deletar, retorna erro
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
