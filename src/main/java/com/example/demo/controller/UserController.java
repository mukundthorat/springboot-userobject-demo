package com.example.demo.controller;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

//    @PostMapping("/createUser")
//    public ResponseEntity<User> createUser(@RequestBody User user){
//        User createdUser=userService.saveUser(user);
//        return ResponseEntity.ok(createdUser);
//    }


    //get User that is authenticated,so instead of calling through userService we call it through
    //Security COntext holder
    @GetMapping("/me")
    public ResponseEntity<User> authenticatedUser(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        User currentUser=(User) authentication.getPrincipal();

        return ResponseEntity.ok(currentUser);
    }

    //All users
    @GetMapping("/all")
    public ResponseEntity<List<User>> allUsers(){
        List <User> users=userService.allUsers();

        return ResponseEntity.ok(users);
    }


    @PutMapping("/update")
    public ResponseEntity<User> updateAuthenticatedUser(
            @RequestBody User userPayLoad
                ){
        Authentication authentication=
                SecurityContextHolder.getContext().getAuthentication();
        User currentUser=(User) authentication.getPrincipal();

        User updatedUser=userService.updateUser(currentUser.getId(),userPayLoad);
        return ResponseEntity.ok(updatedUser);
    }
    @DeleteMapping("/delete")
    public  ResponseEntity<String> deleteAuthenticatedUser(){
        Authentication authentication=
                SecurityContextHolder.getContext().getAuthentication();
        User currentUser=(User) authentication.getPrincipal();

        userService.deleteUser(currentUser.getId());
        return ResponseEntity.ok("User deleted Successfully");
        }
}
