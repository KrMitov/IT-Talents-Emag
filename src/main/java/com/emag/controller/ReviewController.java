package com.emag.controller;

import com.emag.model.dto.reviewdto.RequestReviewDTO;
import com.emag.model.dto.reviewdto.ReviewDTO;
import com.emag.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
public class ReviewController extends AbstractController{

    @Autowired
    private SessionManager sessionManager;
    @Autowired
    private ReviewService reviewService;

    @PostMapping("/reviews")
    public ReviewDTO addReview(@Valid @RequestBody RequestReviewDTO requestReviewDTO, HttpSession session){
        return reviewService.addReview(requestReviewDTO, sessionManager.getLoggedUser(session).getId());
    }

    @PutMapping("/reviews")
    public ReviewDTO editReview(@Valid @RequestBody RequestReviewDTO requestReviewDTO, HttpSession session){
        return reviewService.editReview(requestReviewDTO, sessionManager.getLoggedUser(session).getId());
    }

    @PostMapping("/reviews/{id}/like")
    public ReviewDTO likeReview(@PathVariable int id, HttpSession session){
        return reviewService.likeReview(id, sessionManager.getLoggedUser(session).getId());
    }
}
