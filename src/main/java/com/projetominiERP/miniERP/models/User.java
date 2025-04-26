package com.projetominiERP.miniERP.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.projetominiERP.miniERP.models.enums.ProfileEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import javax.validation.constraints.Email;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = User.TABLE_NAME) //tabela do database
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {

    public static final String TABLE_NAME = "users";

    @Id
    @Column(name = "id", unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY) //id é aleatório
    private Long id;

    @Column(name = "email", length = 50, nullable = false, unique = true)
    @Size(min = 2, max = 50)
    @NotBlank
    @Email(message = "O e-mail deve ser válido!")
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) //apenas para post put e delete, não pode get
    @Column(name = "password", length = 60, nullable = false)
    @Size(min = 6, max = 60)
    private String password;

    @Column(name = "profile", nullable = false)
    @ElementCollection(fetch = FetchType.EAGER) //carregar as roles
    @CollectionTable(name = "user_profile")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Set<Integer> profiles = new HashSet<>(); //lista de valores unicos

    public Set<ProfileEnum> getProfiles() {
        return this.profiles.stream().map(x -> ProfileEnum.toEnum(x)).collect(Collectors.toSet());
        //transofrma o Set<interger> em uma stream manipulável
        //.map() aplica a função pra cada item (x)
        //(x -> Profil...) é a função que transforma o valor x (código numérico) em um valor da enum ProfileEnum
        //Exemplo: Se x for 1, a função vai chamar ProfileEnum.toEnum(1), que retorna ProfileEnum.ADMIN
        //collect() é uma operação terminal que transforma a Stream de volta em uma coleção.
        //Collectors.toSet() coleta os resultados da stream em um Set, ou seja,
        //um conjunto que não permite duplicação de elementos.
    }

    public void AddProfile(ProfileEnum profileEnum) {
        this.profiles.add(profileEnum.getCode());
    }
    //Ou seja, o código 1 ou 2 (dependendo de qual valor de ProfileEnum foi passado)
    //será adicionado ao conjunto profiles Set<Interger>, permitindo que o usuário tenha uma role
    //Basicamente adciona um perfil ao usuário e salva
}
