package com.sushi.service;

import com.sushi.dto.AuthenticationRequest;
import com.sushi.dto.AuthenticationResponse;
import com.sushi.dto.RegisterRequest;
import com.sushi.entity.User;
import com.sushi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = new User();

        user.setFirstName(request.getFirstname());
        user.setLastName(request.getLastname());
        user.setEmail(request.getEmail());
        user.setPwd(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        var savedUser = repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var AuthenticationResponse = new AuthenticationResponse();
        AuthenticationResponse.setAccessToken(jwtToken);
        return AuthenticationResponse;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var AuthenticationResponse = new AuthenticationResponse();
        AuthenticationResponse.setAccessToken(jwtToken);
        return AuthenticationResponse;
    }
}