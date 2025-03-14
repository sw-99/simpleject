package com.simpleject.dto.response;

import com.simpleject.enums.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProjectMemberResponse {
    private String publicId;
    private String name;
    private Role role;
}
