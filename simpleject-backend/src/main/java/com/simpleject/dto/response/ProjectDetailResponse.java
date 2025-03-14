package com.simpleject.dto.response;

import com.simpleject.enums.Visibility;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class ProjectDetailResponse {
    private String publicId;
    private String name;
    private String description;
    private Visibility visibility;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserResponse owner;
    private List<ProjectMemberResponse> members;
    private int taskCount;
    private int progress;
    private int daysRemaining;
}
