package com.emag.controller;

import com.emag.model.dto.AddToCartDTO;
import com.emag.model.dto.userdto.UserWithoutPasswordDTO;
import com.emag.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CartController {

    @Autowired
    UserService userService;

//    @PostMapping("/cart")
//    public UserWithoutPasswordDTO addProductToCArt(@RequestBody AddToCartDTO dto){
////           return userService.addProductToCart(dto.getProductId(),dto.getUserId());
//    }

}
