package com.fitness.api.auth.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
public class SubmitReviewDto {
    private UUID trainerId;
    private Integer rating;
    private String comment;
}