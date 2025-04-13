package com.projetominiERP.miniERP.models.enuns;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@AllArgsConstructor
@Getter
public enum ProfileEnum {

    ADMIN(1,"ROLE_ADMIN"), //dois tipos de user
    USER(2, "ROLE_USER");

    private Integer code;
    private String description;

    public static ProfileEnum toEnum(Integer code) {

        if(Objects.isNull(code)){ //se é nulo, então é nulo
            return null;
        }
        for(ProfileEnum x : ProfileEnum.values()){ //para cada valor do enum, ele tem seu valor no x
            if (code.equals(x.getCode())){ // se o code que foi passado está no x (1 ou 2) então retorne ele
                return x;
            }
        }

        throw new IllegalArgumentException("Código inválido! " + code); //se não for nulo e não for 1 ou 2, é inválido

    }

}

