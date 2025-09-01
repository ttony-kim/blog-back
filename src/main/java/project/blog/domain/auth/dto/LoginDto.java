package project.blog.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginDto {

    @NotBlank(message = "Email을 입력해 주세요.")
    private String email;

    @NotBlank(message = "Password를 입력해 주세요.")
    private String password;

}
