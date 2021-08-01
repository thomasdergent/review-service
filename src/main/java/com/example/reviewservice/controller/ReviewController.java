package com.example.reviewservice.controller;

import com.example.reviewservice.model.Review;
import com.example.reviewservice.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

@RestController
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @PostConstruct
    public void fillDB(){
        if(reviewRepository.count()==0){
            reviewRepository.save(new Review(003, "687468435454",5));
            reviewRepository.save(new Review(002, "687468435454",2));
            reviewRepository.save(new Review(001, "687468434567",4));
        }

        System.out.println("Reviews test: " + reviewRepository.findReviewsByISBN("687468435454").size());
    }

    @GetMapping("/reviews/user/{userId}")
    public List<Review> getReviewsByUserId(@PathVariable Integer userId){
        return reviewRepository.findReviewsByUserId(userId);
    }

    @GetMapping("/reviews/{ISBN}")
    public List<Review> getReviewsByISBN(@PathVariable String ISBN){
        return reviewRepository.findReviewsByISBN(ISBN);
    }

    @GetMapping("/reviews/user/{userId}/book/{ISBN}")
    public Review getReviewByUserIdAndISBN(@PathVariable Integer userId, @PathVariable String ISBN){
        return reviewRepository.findReviewByUserIdAndAndISBN(userId, ISBN);
    }

    @PostMapping("/reviews")
    public Review addReview(@RequestBody Review review){

        reviewRepository.save(review);

        return review;
    }

    @PutMapping("/reviews")
    public Review updateReview(@RequestBody Review updatedReview){
        Review retrievedReview = reviewRepository.findReviewByUserIdAndAndISBN(updatedReview.getUserId(),updatedReview.getISBN());

        retrievedReview.setISBN(updatedReview.getISBN());
        retrievedReview.setScoreNumber(updatedReview.getScoreNumber());

        reviewRepository.save(retrievedReview);

        return retrievedReview;
    }

    @DeleteMapping("/reviews/user/{userId}/book/{ISBN}")
    public ResponseEntity deleteReview(@PathVariable Integer userId, @PathVariable String ISBN){
        Review review = reviewRepository.findReviewByUserIdAndAndISBN(userId, ISBN);
        if(review!=null){
            reviewRepository.delete(review);
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.notFound().build();
        }
    }
}
