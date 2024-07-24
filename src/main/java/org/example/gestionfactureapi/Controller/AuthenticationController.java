package org.example.gestionfactureapi.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.gestionfactureapi.Entity.AuthenticationResponse;
import org.example.gestionfactureapi.Entity.User;
import org.example.gestionfactureapi.Service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    private final AuthenticationService authService;

    public AuthenticationController(AuthenticationService authService) {
        this.authService = authService;
    }


    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody User request) {
        System.out.println(request);
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody User request
    ) {
        System.out.println(request);
        AuthenticationResponse respone =authService.authenticate(request);
        if(respone.getMessage().equals("User login was successful")){
            return ResponseEntity.ok(respone);
        }else {
            return ResponseEntity.internalServerError().body("Email or password error");
        }

    }

    @PostMapping("/refresh_token")
    public ResponseEntity refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        return authService.refreshToken(request, response);
    }
}