package com.ai.api.auth.controller;

import com.ai.api.auth.service.AuthService;
import com.ai.api.auth.dto.LoginDTO;
import com.ai.api.auth.dto.MessageDTO;
import com.ai.api.auth.dto.RefreshTokenDTO;
import com.ai.api.auth.dto.SignUpDTO;
import com.ai.api.auth.dto.TokenInfoDTO;
import com.ai.api.auth.dto.UserInfoDTO;
import com.ai.common.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final CookieUtil cookieUtil;

    @Value("${jwt.cookie.access-token.name}")
    private String accessTokenCookieName;

    @Value("${jwt.cookie.access-token.max-age}")
    private int accessTokenMaxAge;

    @Value("${jwt.cookie.refresh-token.name}")
    private String refreshTokenCookieName;

    @Value("${jwt.cookie.refresh-token.max-age}")
    private int refreshTokenMaxAge;

    @Value("${jwt.cookie.http-only}")
    private boolean httpOnly;

    @Value("${jwt.cookie.secure}")
    private boolean secure;

    @Value("${jwt.cookie.same-site}")
    private String sameSite;

    @Value("${jwt.cookie.domain}")
    private String domain;

    @PostMapping("/signup")
    public ResponseEntity<MessageDTO> signUp(@Valid @RequestBody SignUpDTO signUpDTO) {
        try{
            UserInfoDTO userInfo = authService.signUp(signUpDTO);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                MessageDTO.builder()
                    .message("회원가입 완료")
                    .status(HttpStatus.CREATED.value())
                    .data(userInfo)
                    .build()
            );
        } catch (Exception e) {
            log.error("회원가입 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                MessageDTO.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST.value())
                    .build()
            );
        }
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<MessageDTO> login(
        @Valid @RequestBody LoginDTO request,
        HttpServletResponse response
    ) {
        try {
            TokenInfoDTO tokenInfo = authService.login(request);

            cookieUtil.addCookie(response, accessTokenCookieName,
                tokenInfo.getAccessToken(), accessTokenMaxAge);
            cookieUtil.addCookie(response, refreshTokenCookieName,
                tokenInfo.getRefreshToken(), refreshTokenMaxAge);

            UserInfoDTO userInfo = UserInfoDTO.builder()
                .id(tokenInfo.getUserId())
                .username(tokenInfo.getUsername())
                .role(tokenInfo.getRole())
                .build();

            return ResponseEntity.ok(
                MessageDTO.builder()
                    .message("로그인이 완료되었습니다.")
                    .status(HttpStatus.OK.value())
                    .data(userInfo)
                    .build()
            );
        } catch (Exception e) {
            log.error("로그인 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                MessageDTO.builder()
                    .message("아이디 또는 비밀번호가 올바르지 않습니다.")
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .build()
            );
        }
    }

    // Access Token 재발급
    @PostMapping("/refresh")
    public ResponseEntity<MessageDTO> refreshToken(
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        try {
            // 쿠키에서 Refresh Token 추출
            String refreshToken = cookieUtil.getCookie(request, refreshTokenCookieName);

            if (refreshToken == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    MessageDTO.builder()
                        .message("Refresh Token이 없습니다.")
                        .status(HttpStatus.UNAUTHORIZED.value())
                        .build()
                );
            }

            RefreshTokenDTO refreshTokenDTO = RefreshTokenDTO.builder()
                .refreshToken(refreshToken)
                .build();

            TokenInfoDTO tokenInfo = authService.refreshAccessToken(refreshTokenDTO);

            // 새로운 Access Token 쿠키 설정
            cookieUtil.addCookie(response, accessTokenCookieName,
                tokenInfo.getAccessToken(), accessTokenMaxAge);

            return ResponseEntity.ok(
                MessageDTO.builder()
                    .message("토큰이 재발급되었습니다.")
                    .status(HttpStatus.OK.value())
                    .build()
            );
        } catch (Exception e) {
            log.error("토큰 재발급 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                MessageDTO.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .build()
            );
        }
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<MessageDTO> logout(
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        try {
            // 쿠키에서 Refresh Token 추출
            String refreshToken = cookieUtil.getCookie(request, refreshTokenCookieName);

            if (refreshToken != null) {
                // Refresh Token을 기반으로 로그아웃 처리
                authService.logoutByRefreshToken(refreshToken);
            }

            // 쿠키 삭제
            cookieUtil.deleteCookie(response, accessTokenCookieName);
            cookieUtil.deleteCookie(response, refreshTokenCookieName);

            return ResponseEntity.ok(
                MessageDTO.builder()
                    .message("로그아웃이 완료되었습니다.")
                    .status(HttpStatus.OK.value())
                    .build()
            );
        } catch (Exception e) {
            log.error("로그아웃 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                MessageDTO.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST.value())
                    .build()
            );
        }
    }

    // 현재 사용자 정보 조회
    @GetMapping("/me")
    public ResponseEntity<MessageDTO> getCurrentUser(Authentication authentication) {
        try {
            if (authentication == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    MessageDTO.builder()
                        .message("인증 정보가 없습니다.")
                        .status(HttpStatus.UNAUTHORIZED.value())
                        .build()
                );
            }

            UserInfoDTO userInfo = authService.getCurrentUser(authentication.getName());

            return ResponseEntity.ok(
                MessageDTO.builder()
                    .message("사용자 정보 조회 성공")
                    .status(HttpStatus.OK.value())
                    .data(userInfo)
                    .build()
            );
        } catch (Exception e) {
            log.error("사용자 정보 조회 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                MessageDTO.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST.value())
                    .build()
            );
        }
    }

}
