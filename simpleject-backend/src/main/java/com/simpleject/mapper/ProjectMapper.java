package com.simpleject.mapper;

import com.simpleject.dto.request.ProjectRequest;
import com.simpleject.dto.response.ProjectDetailResponse;
import com.simpleject.dto.response.ProjectMemberResponse;
import com.simpleject.dto.response.ProjectResponse;
import com.simpleject.dto.response.UserResponse;
import com.simpleject.entity.Project;
import com.simpleject.entity.ProjectMember;
import com.simpleject.entity.User;
import com.simpleject.repository.jpa.ProjectMemberRepository;
import com.simpleject.repository.jpa.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import static com.simpleject.util.UuidGenerator.generate;

@Component
@RequiredArgsConstructor
public class ProjectMapper {

    private final ProjectMemberRepository projectMemberRepository;
    private final UserRepository userRepository;

    public Project toEntity(ProjectRequest request, Long id) {
        return Project.builder()
                .ownerId(id)
                .publicId(generate())
                .name(request.getName())
                .description(request.getDescription())
                .visibility(request.getVisibility())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();
    }

    public ProjectResponse toResponse(Project project) {
        return ProjectResponse.builder()
                .publicId(project.getPublicId())
                .name(project.getName())
                .description(project.getDescription())
                .visibility(project.getVisibility())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }

    public ProjectDetailResponse toDetailedResponse(Project project) {
        List<ProjectMember> members = projectMemberRepository.findByProjectId(project.getId());
//        int taskCount = taskRepository.countByProjectId(project.getId());

        return ProjectDetailResponse.builder()
                .publicId(project.getPublicId())
                .name(project.getName())
                .description(project.getDescription())
                .visibility(project.getVisibility())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .owner(UserResponse.builder()
                        .publicId(project.getPublicId())
                        .name(userRepository.findById(project.getOwnerId())
                                .map(User::getUsername)
                                .orElse(""))
                        .build())
                .members(members.stream().map(member -> ProjectMemberResponse.builder()
                        .publicId(member.getUser().getPublicId())
                        .name(member.getUser().getUsername())
                        .role(member.getRole())
                        .build()).collect(Collectors.toList()))
//                .taskCount(taskCount)
//                .progress(calculateProgress(project.getId()))
                .daysRemaining(calculateDaysRemaining(project.getEndDate()))
                .build();
    }

//    private int calculateProgress(Long projectId) {
//        int totalTasks = taskRepository.countByProjectId(projectId);
//        if (totalTasks == 0) return 0;
//
//        int completedTasks = taskRepository.countByProjectIdAndStatus(projectId, TaskStatus.COMPLETED);
//        return (completedTasks * 100) / totalTasks;
//    }

    private int calculateDaysRemaining(LocalDateTime endDate) {
        if (endDate == null) return -1;
        return (int) ChronoUnit.DAYS.between(LocalDateTime.now(), endDate);
    }


}
