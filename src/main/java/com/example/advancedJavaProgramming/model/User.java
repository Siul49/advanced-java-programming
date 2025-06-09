package com.example.advancedJavaProgramming.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "account")
@Getter @Setter
public class User {
    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    private String accountId;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    @Indexed(unique = true)
    private String name;

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    private String nickname;

    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @Indexed(unique = true)
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    private String email;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$",
            message = "영문, 숫자, 특수문자를 포함한 8~20자로 입력해주세요")
    private String password;
}
