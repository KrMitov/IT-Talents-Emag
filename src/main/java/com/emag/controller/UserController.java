package com.emag.controller;

import com.emag.model.dto.LoginRequestUserDTO;
import com.emag.model.dto.RegisterRequestUserDTO;
import com.emag.model.dto.RegisterResponseUserDTO;
import com.emag.model.dto.UserWithoutPasswordDTO;
import com.emag.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
public class UserController extends AbstractController{

    @Autowired
    private UserService userService;

    @PostMapping("/users")
    public RegisterResponseUserDTO register(@RequestBody RegisterRequestUserDTO dto){
        return userService.register(dto);
    }

    @PostMapping("/users/login")
    public UserWithoutPasswordDTO login(@RequestBody LoginRequestUserDTO dto){
    return userService.login(dto);
    }

    @GetMapping("/users/{id}")
    public UserWithoutPasswordDTO getUserById(@PathVariable int id){
       return userService.findById(id);
    }

}
