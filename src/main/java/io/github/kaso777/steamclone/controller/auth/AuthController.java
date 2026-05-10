package io.github.kaso777.steamclone.controller.auth;

import io.github.kaso777.steamclone.dto.auth.LoginRequest;
import io.github.kaso777.steamclone.dto.auth.RegisterRequest;
import io.github.kaso777.steamclone.dto.UserResponseDTO;
import io.github.kaso777.steamclone.exception.InvalidCredentialsException;
import io.github.kaso777.steamclone.repository.UserRepository;
import io.github.kaso777.steamclone.service.UserService;
import io.github.kaso777.steamclone.service.security.JwtUtil;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserRepository userRepository,
            UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtUtil.generateToken(userDetails);

            String userId = userRepository.findByUsername(loginRequest.getUsername())
                    .map(user -> user.getId().toString())
                    .orElse(null);

            Map<String, String> response = new HashMap<>();
            response.put("jwt", jwt);
            response.put("userId", userId);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new InvalidCredentialsException();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        UserResponseDTO registeredUser = userService.registerUser(registerRequest);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }
}
