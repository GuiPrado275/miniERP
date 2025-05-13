package com.projetominiERP.miniERP.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Objects;

@Component
public class JWTUtil {

    @Value("${jwt.secret}") //do application properties
    private String secret;

    @Value("${jwt.expiration}") //do application properties
    private Long expiration;

    public String generateToken(String email) { // token que no futuro será expirado
        SecretKey key = getKeyBySecret();
        return Jwts.builder()
                .setSubject(email)
                .setExpiration(new Date(System.currentTimeMillis() + this.expiration)) //tempo de  expiração
                .signWith(key)
                .compact();
    }

    private SecretKey getKeyBySecret(){ //chave de encriptação
        SecretKey key = Keys.hmacShaKeyFor(this.secret.getBytes());
        return key;
    }

    public boolean isValidToken(String token){ //verifica se o token é válido, se tem um email e se não está expirado
        Claims claims = getClaims(token);
        if (Objects.nonNull(claims)){
            String email = claims.getSubject();
            Date expirationDate = claims.getExpiration();
            Date now = new Date(System.currentTimeMillis());
            if (Objects.nonNull(email) && Objects.nonNull(expirationDate) && now.before(expirationDate))
                return true;
        }
        return false;
    }

    public String getEmail(String token){ //pega o email do token
        Claims claims = getClaims(token);
        if (Objects.nonNull(claims))
            return claims.getSubject();
        return null;
    }

    private Claims getClaims(String token){ //Para gerar claims - transformar token em datas, desencriptar
        SecretKey key = getKeyBySecret();
        try {
            return Jwts.parserBuilder().setSigningKey(key). build().parseClaimsJws(token).getBody();
        } catch (Exception e){
            return null; //se o objeto for nulo ou inválido, retorna isso
        }
    }

}
