package com.simpleject.controller;

import com.simpleject.dto.request.ProjectRequest;
import com.simpleject.dto.response.ProjectDetailResponse;
import com.simpleject.dto.response.ProjectResponse;
import com.simpleject.security.CustomUserDetails;
import com.simpleject.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getProjects(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long id = userDetails.getUserId();
        List<ProjectResponse> response = projectService.getProjects(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{publicId}")
    public ResponseEntity<ProjectDetailResponse> getProject(@PathVariable String publicId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        ProjectDetailResponse response = projectService.getProject(publicId, userDetails.getUserId());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(@Valid @RequestBody ProjectRequest request, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long id = userDetails.getUserId();
        ProjectResponse response = projectService.createProject(request, id);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{publicId}")
    public ResponseEntity<ProjectResponse> updateProject(@PathVariable String publicId,
                                                         @RequestBody Map<String, Object> updates,
                                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        ProjectResponse response = projectService.updateProject(publicId, userDetails.getUserId(), updates);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{publicId}")
    public ResponseEntity<?> deleteProject(@PathVariable String publicId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        projectService.deleteProject(publicId, userDetails.getUserId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteProjects(@RequestBody List<String> publicIds,
                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        projectService.deleteProjects(publicIds, userDetails.getUserId());
        return ResponseEntity.noContent().build();
    }

}
