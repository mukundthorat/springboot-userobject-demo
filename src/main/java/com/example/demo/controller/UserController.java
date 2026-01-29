package com.example.demo.controller;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/users/v1")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping("/createUser")
    public ResponseEntity<User> createUser(@RequestBody User user){
        User createdUser=userService.saveUser(user);
        return ResponseEntity.ok(createdUser);
    }
    @GetMapping("/getUser/{username}")
    public ResponseEntity<User> getUserByUserName(@PathVariable("username") String username){
        final User userByUsername= userService.getUserByUserName(username);
        return ResponseEntity.ok(userByUsername);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @RequestBody User userPayLoad
                ){
        User updatedUser=userService.updateUser(id,userPayLoad);
        return ResponseEntity.ok(updatedUser);
    }
    @DeleteMapping("/delete/{id}")
    public  ResponseEntity<String> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted Successfully");
    }
}
