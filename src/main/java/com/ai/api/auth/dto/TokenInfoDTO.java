package com.ai.api.auth.dto;

import com.ai.api.auth.domain.AdminRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenInfoDTO {
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Long userId;
    private String username;
    private AdminRole role;

}
