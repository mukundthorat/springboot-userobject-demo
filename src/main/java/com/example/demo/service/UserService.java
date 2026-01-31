package com.example.demo.service;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    //Create ,but first check if same username is already present or not
    public User saveUser(User user){
        if(userRepository.findByUsername(user.getUsername()).isPresent() ){
            throw new RuntimeException("Username already exists");
        }
        if(userRepository.findUserByEmail(user.getEmail()).isPresent()){
            throw new RuntimeException("Email already exists");
        }
        return userRepository.save(user);
    }
    //get user by username
    public User getUserByUserName(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(()-> new RuntimeException("User not found"));
    }
    //update
    public User updateUser(Long id,User userPayLoad) {
        //fetch existing user
        User existingUser=userRepository.findById(id)
                .orElseThrow( ()-> new RuntimeException("User not found") );
        //modify fieds
        existingUser.setUsername(userPayLoad.getUsername());
        existingUser.setEmail(userPayLoad.getEmail());
        existingUser.setFirstName(userPayLoad.getFirstName());
        existingUser.setLastName(userPayLoad.getLastName());

        if(userPayLoad.getPassword() !=null && !userPayLoad.getPassword().isBlank()){
            existingUser.setPassword(userPayLoad.getPassword());
        }

        existingUser.setPhoneNumber(userPayLoad.getPhoneNumber());

        existingUser.setRole(userPayLoad.getRole());

        existingUser.setStatus(userPayLoad.getStatus());
        //now save updated entity
        return userRepository.save(existingUser);
    }
    //delete
    public void deleteUser(Long id) {
        //fetch first
        User exstingUser=userRepository.findById(id)
                .orElseThrow( ()->new RuntimeException("user not found") );
        //delete
        userRepository.delete(exstingUser);
    }

    //allusers
    public List<User> allUsers(){
        List<User> users=new ArrayList<>();

        userRepository.findAll().forEach(users::add);

        return users;
    }
}
