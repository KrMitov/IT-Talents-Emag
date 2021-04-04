package com.emag.controller;

import com.emag.model.dto.produtcdto.LikedProductsForUserDTO;
import com.emag.model.dto.produtcdto.ProductsFromCartForUserDTO;
import com.emag.model.dto.produtcdto.UserOrdersDTO;
import com.emag.model.dto.registerdto.RegisterRequestUserDTO;
import com.emag.model.dto.registerdto.RegisterResponseUserDTO;
import com.emag.model.dto.userdto.*;
import com.emag.model.pojo.UserImage;
import com.emag.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;

@RestController
public class UserController extends AbstractController{

    @Autowired
    private UserService userService;
    @Autowired
    private SessionManager sessionManager;

    @PostMapping("/users")
    public RegisterResponseUserDTO register(@RequestBody @Valid RegisterRequestUserDTO dto, HttpSession session){
        sessionManager.loggedInVerification(session);
        return userService.register(dto);
    }

    @PostMapping("/users/login")
    public UserWithoutPasswordDTO login(@RequestBody @Valid LoginRequestUserDTO dto, HttpSession session){
        sessionManager.loggedInVerification(session);
        UserWithoutPasswordDTO response = userService.login(dto);
        sessionManager.loginUser(session,response.getId());
        return response;
    }

    @GetMapping("/users/{id}")
    public UserWithoutPasswordDTO getUserById(@PathVariable int id,HttpSession session) {
        sessionManager.userVerification(session, id);
        return userService.findById(id);
    }

    @PostMapping("/users/logout")
    public LogoutDTO logout(HttpSession session){
        sessionManager.getLoggedUser(session);
        sessionManager.logoutUser(session);
        return new LogoutDTO("You have successfully logged out.");
    }

    @PutMapping("/users/{id}")
    public UserWithoutPasswordDTO editUser(@PathVariable int id, @RequestBody @Valid EditProfileRequestDTO dto, HttpSession session){
        sessionManager.userVerification(session, id);
        return userService.editUser(id,dto);
    }

    @GetMapping("/users/{id}/favourites")
    public LikedProductsForUserDTO getFavoriteProducts(@PathVariable int id,HttpSession session){
        sessionManager.userVerification(session, id);
        return userService.getLikedProducts(id);
    }

    @GetMapping("/users/{id}/cart")
    public ProductsFromCartForUserDTO getProductsFromCart(@PathVariable int id,HttpSession session){
        sessionManager.userVerification(session, id);
        return userService.getProductsFromCart(id);
    }

    @PostMapping("/users/{id}/image")
    public UserImage uploadImage(@RequestPart MultipartFile file, @PathVariable int id,HttpSession session) throws IOException {
        sessionManager.userVerification(session, id);
        return userService.uploadImage(file,id);
    }

    @GetMapping(value = "/users/{id}/image",produces = "image/*")
    public byte[] downloadUserImage(@PathVariable int id,HttpSession session) throws IOException {
        sessionManager.userVerification(session, id);
        return userService.downloadImage(id);
    }

    @GetMapping("/users/{id}/orders")
    public UserOrdersDTO getOrdersForUser(@PathVariable int id,HttpSession session){
        sessionManager.userVerification(session, id);
        return userService.getOrders(id);
    }

    @GetMapping("/users/{id}/reviews")
    public UserReviewsDTO getReviewsByUser(@PathVariable int id, HttpSession session){
        sessionManager.userVerification(session, id);
        return userService.getReviews(id);
    }
}
