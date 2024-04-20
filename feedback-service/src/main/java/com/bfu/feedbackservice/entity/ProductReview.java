package com.bfu.feedbackservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("product_Review")
public class ProductReview {
    @Id
    private UUID id;

    private String productId;

    private int rating;

    private String review;

    @CreatedDate
    private Date createdDate;

    private String username;

    private String userId;
}
