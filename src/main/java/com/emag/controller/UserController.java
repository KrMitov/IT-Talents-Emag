package com.emag.controller;

import com.emag.model.dto.*;
import com.emag.model.dto.registerDTO.RegisterRequestUserDTO;
import com.emag.model.dto.registerDTO.RegisterResponseUserDTO;
import com.emag.model.dto.userDTO.UserWithoutPasswordDTO;
import com.emag.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpSession;
import java.text.ParseException;

@RestController
public class UserController extends AbstractController{

    @Autowired
    private UserService userService;
    @Autowired
    private SessionManager sessionManager;

    @PostMapping("/users")
    public RegisterResponseUserDTO register(@RequestBody RegisterRequestUserDTO dto){
        return userService.register(dto);
    }

    @PostMapping("/users/login")
    public UserWithoutPasswordDTO login(@RequestBody LoginRequestUserDTO dto, HttpSession session){
        UserWithoutPasswordDTO response = userService.login(dto);
        sessionManager.loginUser(session,response.getId());
        return response;
    }

    @GetMapping("/users/{id}")
    public UserWithoutPasswordDTO getUserById(@PathVariable int id,HttpSession session) throws AuthenticationException {
        sessionManager.getLoggedUser(session);
        return userService.findById(id);
    }

    @PostMapping("/users/logout")
    public String logout(HttpSession session){
        sessionManager.logoutUser(session);
        return "you logged out";
    }

    @PutMapping("/users/{id}")
    public UserWithoutPasswordDTO editUser(@PathVariable int id, @RequestBody EditProfileRequestDTO dto,HttpSession session) throws ParseException {
          sessionManager.getLoggedUser(session);
          return userService.editUser(id,dto);
    }


}
