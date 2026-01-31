package com.example.demo.service;

import com.example.demo.dtos.LoginUserDto;
import com.example.demo.dtos.RegisterUserDto;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private  final AuthenticationManager authenticationManager;



    public User signup(RegisterUserDto input){
        User user=new User();
        user.setUsername(input.getUsername());
        user.setEmail(input.getEmail());
        user.setFirstName(input.getFirstName());
        user.setLastName(input.getLastName());
        user.setPhoneNumber(input.getPhoneNumber());
        user.setRole(input.getRole());
        user.setStatus(input.getStatus());
        user.setPassword(passwordEncoder.encode(input.getPassword()));

        return userRepository.save(user);

    }
    public User authenticate(LoginUserDto input){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );
        return userRepository.findUserByEmail(input.getEmail())
                .orElseThrow();
    }
}
