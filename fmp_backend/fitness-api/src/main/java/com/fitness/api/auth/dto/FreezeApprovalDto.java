package com.fitness.api.auth.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
public class FreezeApprovalDto {
    private UUID logId;
    private boolean approved; // true = Chấp nhận, false = Từ chối đơn
}