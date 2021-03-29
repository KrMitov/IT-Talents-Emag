package com.emag.controller;

import com.emag.model.dto.produtcdto.LikedProductsForUserDTO;
import com.emag.model.dto.produtcdto.ProductsFromCartForUserDTO;
import com.emag.model.dto.produtcdto.UserOrdersDTO;
import com.emag.model.dto.registerdto.RegisterRequestUserDTO;
import com.emag.model.dto.registerdto.RegisterResponseUserDTO;
import com.emag.model.dto.userdto.EditProfileRequestDTO;
import com.emag.model.dto.userdto.LoginRequestUserDTO;
import com.emag.model.dto.userdto.UserWithoutPasswordDTO;
import com.emag.model.pojo.UserImage;
import com.emag.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpSession;
import java.io.IOException;
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
        sessionManager.getLoggedUser(session);
        sessionManager.logoutUser(session);
        return "you logged out";
    }

    @PutMapping("/users/{id}")
    public UserWithoutPasswordDTO editUser(@PathVariable int id, @RequestBody EditProfileRequestDTO dto, HttpSession session) throws ParseException {
          sessionManager.getLoggedUser(session);
          return userService.editUser(id,dto);
    }

    @GetMapping("/users/{id}/favourites")
    public LikedProductsForUserDTO getFavoriteProducts(@PathVariable int id,HttpSession session){
        sessionManager.getLoggedUser(session);
        return userService.getLikedProducts(id);
    }

    @GetMapping("/users/{id}/cart")
    public ProductsFromCartForUserDTO getProductsFromCart(@PathVariable int id,HttpSession session){
        sessionManager.getLoggedUser(session);
         return userService.getProductsFromCart(id);
    }
   @PutMapping("/users/{id}/image")
    public UserImage uploadImage(@RequestPart MultipartFile file, @PathVariable int id,HttpSession session) throws IOException {
       sessionManager.getLoggedUser(session);
        return userService.uploadImage(file,id);
   }
   @GetMapping(value = "/users/{id}/image",produces = "image/*")
    public byte[] downloadUserImage(@PathVariable int id,HttpSession session) throws IOException {
        sessionManager.getLoggedUser(session);
        return userService.downloadImage(id);
   }

   @GetMapping("/users/{id}/orders")
   public UserOrdersDTO getOrdersForUser(@PathVariable int id,HttpSession session){
        sessionManager.getLoggedUser(session);
         return userService.getOrders(id);
   }

}
