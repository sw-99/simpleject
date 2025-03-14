package com.simpleject.dto.request;

import com.simpleject.enums.Visibility;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.security.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
public class ProjectRequest {

    @NotBlank(message = "프로젝트 이름을 입력하세요")
    private String name;
    private String description;
    private Visibility visibility;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
