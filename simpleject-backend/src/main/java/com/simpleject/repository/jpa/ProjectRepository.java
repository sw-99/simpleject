package com.simpleject.repository.jpa;

import com.simpleject.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByPublicId(String publicId);
    List<Project> findByPublicIdIn(List<String> publicIds);
    void deleteByIdIn(List<Long> ids);




}
