package com.emag.controller;

import com.emag.model.dto.reviewdto.RequestReviewDTO;
import com.emag.model.dto.reviewdto.ReviewDTO;
import com.emag.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class ReviewController extends AbstractController{

    @Autowired
    private SessionManager sessionManager;
    @Autowired
    private ReviewService reviewService;

    @PostMapping("/review")
    public ReviewDTO addReview(@RequestBody RequestReviewDTO requestReviewDTO, HttpSession session){
        return reviewService.addReview(requestReviewDTO, sessionManager.getLoggedUser(session).getId());
    }
    @PutMapping("/review")
    public ReviewDTO editReview(@RequestBody RequestReviewDTO requestReviewDTO, HttpSession session){
        return reviewService.editReview(requestReviewDTO, sessionManager.getLoggedUser(session).getId());
    }
}
