package com.projetominiERP.miniERP.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projetominiERP.miniERP.exceptions.GlobalExceptionHandler;
import com.projetominiERP.miniERP.models.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

//recebe o /login
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    private JWTUtil jwtUtil;

    //construtor
    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        setAuthenticationFailureHandler(new GlobalExceptionHandler());
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    //tentativa para validacao do email e a senha
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            User userCredentials = new ObjectMapper().readValue(request.getInputStream(), User.class);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userCredentials.getEmail(), userCredentials.getPassword(), new ArrayList<>());

            Authentication authentication = this.authenticationManager.authenticate(authToken);
            return authentication;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override //se a autenticacao foi um sucessor, rodará isso
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response, FilterChain chain,
                                            Authentication authentication) throws IOException, ServletException {
        UserSpringSecurity userSpringSecurity = (UserSpringSecurity) authentication.getPrincipal();
        String email = userSpringSecurity.getEmail();
        String token = jwtUtil.generateToken(email);
        response.addHeader("Authorization", "Bearer " + token);
        response.addHeader("access-control-expose-headers", "Authorization");
    } //isso é para retornar para o user o token para ele ser usado na "rota"

}
