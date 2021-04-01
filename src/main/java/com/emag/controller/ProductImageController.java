package com.emag.controller;

import com.emag.model.dto.productimagedto.DeleteImagesDTO;
import com.emag.model.dto.produtcdto.ProductWithImagesDTO;
import com.emag.service.ProductImageService;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@RestController
public class ProductImageController extends AbstractController{

    @Autowired
    private ProductImageService productImageService;
    @Autowired
    private SessionManager sessionManager;

    @PostMapping("/products/{id}/images")
    public ProductWithImagesDTO uploadProductImages(@PathVariable int id, @RequestPart MultipartFile[] files, HttpSession session) throws IOException {
        sessionManager.adminVerification(session);
        return productImageService.uploadProductImages(id, files);
    }

    @DeleteMapping("/images")
    public List<Integer> removeProductImages(@RequestBody DeleteImagesDTO deleteImageDTO, HttpSession session) throws IOException {
        sessionManager.adminVerification(session);
        return productImageService.removeProductImages(deleteImageDTO);
    }

    @GetMapping(value = "/images/{imageId}", produces = "image/*")
    public byte[] getProductImage(@PathVariable int imageId) throws IOException {
        return productImageService.getProductImage(imageId);
    }
}
