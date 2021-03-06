package com.pbl.yourside.controllers;

import com.pbl.yourside.entities.Report;
import com.pbl.yourside.entities.Role;
import com.pbl.yourside.entities.RoleName;
import com.pbl.yourside.entities.User;
import com.pbl.yourside.security.reponse.JwtResponse;
import com.pbl.yourside.security.reponse.ResponseMessage;
import com.pbl.yourside.security.request.LoginForm;
import com.pbl.yourside.security.request.SignUpForm;
import com.pbl.yourside.repositories.RoleRepository;
import com.pbl.yourside.repositories.UserRepository;
import com.pbl.yourside.security.sevices.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/auth")
public class AuthRESTController {

    AuthenticationManager authenticationManager;

    UserRepository userRepository;

    RoleRepository roleRepository;

    PasswordEncoder passwordEncoder;

    JwtProvider jwtProvider;

    @Autowired
    public AuthRESTController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateJwtToken(authentication);
        UserDetails userDetails = (UserDetails) ((org.springframework.security.core.Authentication) authentication).getPrincipal();

        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(), userDetails.getAuthorities()));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpForm signUpRequest) {

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<>(new ResponseMessage("Fail -> Username is already taken."), HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setActive(true);
        user.setLastName(signUpRequest.getLastName());
        user.setFirstName(signUpRequest.getFirstName());
        String role = signUpRequest.getRole();

        switch (role) {
            case "admin":
                Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                        .orElseThrow(() -> new RuntimeException("Fail -> Cause: Admin Role not found."));
                user.setRole(adminRole);
                break;
            case "student":
                Role studentRole = roleRepository.findByName(RoleName.ROLE_STUDENT)
                        .orElseThrow(() -> new RuntimeException("Fail -> Cause: Student Role not found."));
                user.setRole(studentRole);
                break;
            case "teacher":
                Role teacherRole = roleRepository.findByName(RoleName.ROLE_TEACHER)
                        .orElseThrow(() -> new RuntimeException("Fail -> Cause: Teacher Role not found."));
                user.setRole(teacherRole);
                break;
            case "school_admin":
                Role schoolAdmin = roleRepository.findByName(RoleName.ROLE_SCHOOL_ADMIN)
                        .orElseThrow(() -> new RuntimeException("Fail -> Cause: School Admin Role not found."));
                user.setRole(schoolAdmin);
                break;
        }

        userRepository.save(user);
        return new ResponseEntity<>(new ResponseMessage("User registered successfully."), HttpStatus.OK);
    }

}
