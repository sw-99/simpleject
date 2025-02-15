package com.simpleject.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {

    @NotBlank
    @Size(min = 4, max = 20)
    private String username;

    @NotBlank
//    @Email(message = "올바른 이메일 형식이어야 합니다.")
    private String email;

    @NotBlank
//    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
//    @Pattern(
//            regexp = "^(?=.*[!@#$%^&*(),.?\":{}|<>]).*$",
//            message = "비밀번호는 최소 하나 이상의 특수문자를 포함해야 합니다."
//    )
    private String password;
}
