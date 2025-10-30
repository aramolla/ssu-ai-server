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
public class UserInfoDTO {
    private Long id;
    private String username;
    private String password;
    private AdminRole role;
}
