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

    public String generateToken(String email) { // token that in future will be used to authenticate the user
        SecretKey key = getKeyBySecret();
        return Jwts.builder()
                .setSubject(email)
                .setExpiration(new Date(System.currentTimeMillis() + this.expiration))
                .signWith(key)
                .compact();
    }

    private SecretKey getKeyBySecret(){ //encryption key
        SecretKey key = Keys.hmacShaKeyFor(this.secret.getBytes());
        return key;
    }

    public boolean isValidToken(String token){ //to verify if the token is valid or expired
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

    public String getEmail(String token){ //to get the email from the token
        Claims claims = getClaims(token);
        if (Objects.nonNull(claims))
            return claims.getSubject();
        return null;
    }

    private Claims getClaims(String token){ //to generate claims from the token
        SecretKey key = getKeyBySecret();
        try {
            return Jwts.parserBuilder().setSigningKey(key). build().parseClaimsJws(token).getBody();
        } catch (Exception e){
            return null; //if the object is null or invalid, return this
        }
    }

}
