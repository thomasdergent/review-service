package com.example.reviewservice.repository;

import com.example.reviewservice.model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {
    List<Review> findReviewsByUserId(Integer userid);
    List<Review> findReviewsByISBN(String ISBN);
    Review findReviewByUserIdAndAndISBN(Integer userid, String ISBN);
}
