package com.example.reviewservice;

import com.example.reviewservice.model.Review;
import com.example.reviewservice.repository.ReviewRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ReviewControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewRepository reviewRepository;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void givenReview_whenGetReviewByUserIdAndISBN_thenReturnJsonReview() throws Exception {
        Review reviewUser1Book1 = new Review(001,"ISBN1",1);

        given(reviewRepository.findReviewByUserIdAndAndISBN(001,"ISBN1")).willReturn(reviewUser1Book1);

        mockMvc.perform(get("/reviews/user/{userId}/book/{ISBN}",001,"ISBN1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId",is(001)))
                .andExpect(jsonPath("$.isbn",is("ISBN1")))
                .andExpect(jsonPath("$.scoreNumber",is(1)));
    }

    @Test
    public void givenReview_whenGetReviewsByISBN_thenReturnJsonReviews() throws Exception {
        Review reviewUser1Book1 = new Review(001,"ISBN1",1);
        Review reviewUser2Book1 = new Review(002,"ISBN1",2);

        List<Review> reviewList = new ArrayList<>();
        reviewList.add(reviewUser1Book1);
        reviewList.add(reviewUser2Book1);

        given(reviewRepository.findReviewsByISBN("ISBN1")).willReturn(reviewList);

        mockMvc.perform(get("/reviews/{ISBN}","ISBN1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].userId",is(001)))
                .andExpect(jsonPath("$[0].isbn",is("ISBN1")))
                .andExpect(jsonPath("$[0].scoreNumber",is(1)))
                .andExpect(jsonPath("$[1].userId",is(002)))
                .andExpect(jsonPath("$[1].isbn",is("ISBN1")))
                .andExpect(jsonPath("$[1].scoreNumber",is(2)));
    }

    @Test
    public void givenReview_whenGetReviewsByUserId_thenReturnJsonReviews() throws Exception {
        Review reviewUser1Book1 = new Review(001,"ISBN1",1);
        Review reviewUser1Book2 = new Review(001,"ISBN2",2);

        List<Review> reviewList = new ArrayList<>();
        reviewList.add(reviewUser1Book1);
        reviewList.add(reviewUser1Book2);

        given(reviewRepository.findReviewsByUserId(001)).willReturn(reviewList);

        mockMvc.perform(get("/reviews/user/{userId}",001))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].userId",is(001)))
                .andExpect(jsonPath("$[0].isbn",is("ISBN1")))
                .andExpect(jsonPath("$[0].scoreNumber",is(1)))
                .andExpect(jsonPath("$[1].userId",is(001)))
                .andExpect(jsonPath("$[1].isbn",is("ISBN2")))
                .andExpect(jsonPath("$[1].scoreNumber",is(2)));
    }

    @Test
    public void whenPostReview_thenReturnJsonReview() throws Exception{
        Review reviewUser3Book1 = new Review(003,"ISBN1",1);

        mockMvc.perform(post("/reviews")
                .content(mapper.writeValueAsString(reviewUser3Book1))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId",is(003)))
                .andExpect(jsonPath("$.isbn",is("ISBN1")))
                .andExpect(jsonPath("$.scoreNumber",is(1)));
    }

    @Test
    public void givenReview_whenPutReview_thenReturnJsonReview() throws Exception{
        Review reviewUser1Book1 = new Review(001,"ISBN1",1);

        given(reviewRepository.findReviewByUserIdAndAndISBN(001,"ISBN1")).willReturn(reviewUser1Book1);

        Review updatedReview = new Review(001,"ISBN1",2);

        mockMvc.perform(put("/reviews")
                .content(mapper.writeValueAsString(updatedReview))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId",is(001)))
                .andExpect(jsonPath("$.isbn",is("ISBN1")))
                .andExpect(jsonPath("$.scoreNumber",is(2)));
    }

    @Test
    public void givenReview_whenDeleteReview_thenStatusOk() throws Exception{
        Review reviewToBeDeleted = new Review(999,"ISBN9",1);

        given(reviewRepository.findReviewByUserIdAndAndISBN(999,"ISBN9")).willReturn(reviewToBeDeleted);

        mockMvc.perform(delete("/reviews/user/{userId}/book/{ISBN}",999,"ISBN9")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void givenNoReview_whenDeleteReview_thenStatusNotFound() throws Exception{
        given(reviewRepository.findReviewByUserIdAndAndISBN(888,"ISBN8")).willReturn(null);

        mockMvc.perform(delete("/reviews/user/{userId}/book/{ISBN}",888,"ISBN8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
