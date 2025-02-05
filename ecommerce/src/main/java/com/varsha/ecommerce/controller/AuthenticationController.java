package com.varsha.ecommerce.controller;

import com.varsha.ecommerce.dto.AuthResponseDTO;
import com.varsha.ecommerce.dto.AuthenticationDTO;
import com.varsha.ecommerce.dto.SignUpDTO;
import com.varsha.ecommerce.dto.UserDTO;
import com.varsha.ecommerce.entity.User;
import com.varsha.ecommerce.repository.UserRepository;
import com.varsha.ecommerce.service.AuthService;
import com.varsha.ecommerce.service.CustomUserDetailsService;
import com.varsha.ecommerce.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private static final String HEADER_STRING = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthService authService;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponseDTO> createAuthToken(@RequestBody AuthenticationDTO dto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.username(), dto.password())
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponseDTO("Incorrect username and/or password", null, null));
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(dto.username());
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String jwt = jwtService.generateToken(userDetails);

        AuthResponseDTO response = new AuthResponseDTO("Login successful", user.getId(), jwt);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .body(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUpUser(@RequestBody SignUpDTO dto){
        if(authService.hasUserWithEmail(dto.getEmail())){
            return new ResponseEntity<>("User already exists", HttpStatus.NOT_ACCEPTABLE);
        }
        UserDTO userDTO = authService.createUser(dto);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

}
