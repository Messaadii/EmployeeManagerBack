package com.elkhobna.employeemanager.controllers;

import com.elkhobna.employeemanager.dtos.LoginResponse;
import com.elkhobna.employeemanager.entities.User;
import com.elkhobna.employeemanager.services.AuthenticationService;
import com.elkhobna.employeemanager.services.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/rest/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<User> register(@RequestBody User user) {
        User registeredUser = authenticationService.signup(user);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/log-in")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody User user) {
        User authenticatedUser = authenticationService.authenticate(user);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse().setToken(jwtToken).setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }
}