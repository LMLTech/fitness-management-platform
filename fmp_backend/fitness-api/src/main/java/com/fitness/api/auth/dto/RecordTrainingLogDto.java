package com.fitness.api.auth.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class RecordTrainingLogDto {
    private UUID memberId;
    private String objectives;
    private List<LogItemDto> logs;

    @Getter
    @Setter
    public static class LogItemDto {
        private String exerciseName;
        private Integer sets;
        private Integer reps;
        private Double weight;
        private String notes;
    }
}