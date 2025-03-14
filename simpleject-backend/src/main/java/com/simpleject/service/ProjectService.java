package com.simpleject.service;

import com.simpleject.dto.request.ProjectRequest;
import com.simpleject.dto.response.ProjectDetailResponse;
import com.simpleject.dto.response.ProjectResponse;
import com.simpleject.entity.Project;
import com.simpleject.entity.ProjectMember;
import com.simpleject.entity.User;
import com.simpleject.enums.Permission;
import com.simpleject.enums.Role;
import com.simpleject.enums.Visibility;
import com.simpleject.exception.BadRequestException;
import com.simpleject.exception.NotFoundException;
import com.simpleject.exception.UnauthorizedException;
import com.simpleject.mapper.ProjectMapper;
import com.simpleject.repository.jpa.ProjectMemberRepository;
import com.simpleject.repository.jpa.ProjectRepository;
import com.simpleject.repository.jpa.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final UserRepository userRepository;
    private final ProjectMapper projectMapper;

    public List<ProjectResponse> getProjects(Long id) {

        List<ProjectMember> projectMembers = projectMemberRepository.findByUserId(id);

        if (projectMembers .isEmpty()) {
            throw new NotFoundException("해당 사용자가 소유한 프로젝트가 없습니다. ID: " + id);
        }

        return projectMembers.stream()
                .map(pm -> projectMapper.toResponse(pm.getProject()))
                .collect(Collectors.toList());
    }

    public ProjectDetailResponse getProject(String publicId, Long userId) {
        Project project = projectRepository.findByPublicId(publicId)
                .orElseThrow(() -> new NotFoundException("프로젝트를 찾을 수 없습니다. ID: " + publicId));

        // 프로젝트가 PUBLIC이면 누구나 조회 가능 → 사용자 검증 필요 없음
        if (project.getVisibility() == Visibility.PUBLIC) {
            return projectMapper.toDetailedResponse(project);
        }

        // 프로젝트가 PRIVATE/INTERNAL이면 소유자 또는 멤버인지 확인
        if (!project.getOwnerId().equals(userId) &&
                !projectMemberRepository.existsByProjectIdAndUserId(project.getId(), userId)) {
            throw new UnauthorizedException("프로젝트에 접근할 수 없습니다.");
        }

        return projectMapper.toDetailedResponse(project);
    }

    public ProjectResponse createProject(ProjectRequest request, Long id) {

        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new BadRequestException("프로젝트 이름을 입력하세요.");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다. ID: " + id));

        Project project = projectMapper.toEntity(request, id);
        project = projectRepository.saveAndFlush(project); // 즉시 저장하여 ID 할당

        ProjectMember projectMember = ProjectMember.builder()
                .project(project)
                .user(user)
                .role(Role.OWNER)
                .permissions(Permission.ADMIN)
                .build();

        projectMemberRepository.save(projectMember);

        return projectMapper.toResponse(project);
    }

    public ProjectResponse updateProject(String publicId, Long userId, Map<String, Object> updates) {
        Project project = projectRepository.findByPublicId(publicId)
                .orElseThrow(() -> new NotFoundException("프로젝트를 찾을 수 없습니다. ID: " + publicId));

        if (!project.getOwnerId().equals(userId)) {
            throw new UnauthorizedException("프로젝트를 수정할 권한이 없습니다. 프로젝트 ID: " + publicId);
        }

        updates.forEach((key, value) -> {
            if (value == null) return;
            switch (key) {
                case "name" -> {
                    if (value instanceof String) project.setName((String) value);
                    else throw new BadRequestException("name 값은 문자열이어야 합니다.");
                }
                case "description" -> {
                    if (value instanceof String) project.setDescription((String) value);
                    else throw new BadRequestException("description 값은 문자열이어야 합니다.");
                }
                case "visibility" -> {
                    if (value instanceof String visibilityString) {
                        if (Visibility.isValid(visibilityString)) {
                            project.setVisibility(Visibility.valueOf(visibilityString.toUpperCase()));
                        } else {
                            throw new BadRequestException("유효하지 않은 visibility 값: " + visibilityString);
                        }
                    }
                }
                case "startDate", "endDate" -> {
                    if (value instanceof String dateString) {
                        try {
                            LocalDateTime parsedDate = LocalDateTime.parse(dateString);
                            if (key.equals("startDate")) {
                                project.setStartDate(parsedDate);
                            } else {
                                project.setEndDate(parsedDate);
                            }
                        } catch (DateTimeParseException e) {
                            throw new BadRequestException("잘못된 날짜 형식: " + dateString);
                        }
                    }
                }
                default -> throw new BadRequestException("알 수 없는 필드: " + key);
            }
        });

        projectRepository.save(project);
        return projectMapper.toResponse(project);
    }

    @Transactional
    public void deleteProject(String publicId, Long userId) {
        Project project = projectRepository.findByPublicId(publicId)
                .orElseThrow(() -> new NotFoundException("프로젝트를 찾을 수 없습니다. ID: " + publicId));

        if (!project.getOwnerId().equals(userId)) {
            throw new UnauthorizedException("프로젝트를 삭제할 권한이 없습니다. 프로젝트 ID: " + publicId);
        }

        projectMemberRepository.deleteByProjectId(project.getId());
        projectRepository.deleteById(project.getId());
    }

    @Transactional
    public void deleteProjects(List<String> publicIds, Long userId) {
        List<Project> projects = projectRepository.findByPublicIdIn(publicIds);

        if (projects.isEmpty()) {
            throw new NotFoundException("삭제할 프로젝트를 찾을 수 없습니다.");
        }

        for (Project project : projects) {
            if (!project.getOwnerId().equals(userId)) {
                throw new UnauthorizedException("삭제 권한이 없는 프로젝트가 포함되어 있습니다.");
            }
        }

        List<Long> projectIds = projects.stream().map(Project::getId).toList();
        projectMemberRepository.deleteByProjectIdIn(projectIds);
        projectRepository.deleteByIdIn(projectIds);
    }

}


