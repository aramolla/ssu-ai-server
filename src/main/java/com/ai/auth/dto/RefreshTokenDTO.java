package com.ai.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenDTO {

    @NotBlank(message = "Refresh Token은 필수입니다")
    private String refreshToken;

}
