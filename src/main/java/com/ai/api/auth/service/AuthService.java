package com.ai.api.auth.service;

import com.ai.api.auth.repository.UserRepository;
import com.ai.api.auth.domain.AdminRole;
import com.ai.api.auth.domain.User;
import com.ai.api.auth.dto.LoginDTO;
import com.ai.api.auth.dto.RefreshTokenDTO;
import com.ai.api.auth.dto.SignUpDTO;
import com.ai.api.auth.dto.TokenInfoDTO;
import com.ai.api.auth.dto.UserInfoDTO;
import com.ai.common.exception.DuplicateEntityException;
import com.ai.common.exception.EntityNotFoundException;
import com.ai.common.exception.InvalidTokenException;
import com.ai.common.exception.UnauthorizedException;
import com.ai.common.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 회원가입
    public UserInfoDTO signUp(SignUpDTO request) {
        // 아이디 중복 체크
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateEntityException("이미 존재하는 아이디입니다.");
        }

        // 사용자 생성
        User user = User.builder()
            .username(request.getUsername())
            .password(passwordEncoder.encode(request.getPassword()))
            .name(request.getName())
            .role(request.getRole() != null ? request.getRole() : AdminRole.ROLE_USER)
            .build();

        User savedUser = userRepository.save(user);

        return UserInfoDTO.builder()
            .id(savedUser.getId())
            .username(savedUser.getUsername())
            .role(savedUser.getRole())
            .build();
    }

    // 로그인
    public TokenInfoDTO login(LoginDTO request) {
        // 사용자 조회
        User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new UnauthorizedException("아이디 또는 비밀번호가 올바르지 않습니다."));

        // 비밀번호 검증
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        // Authentication 객체 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            user.getUsername(),
            null,
            user.getAuthorities()
        );

        // 토큰 생성
        String accessToken = jwtUtil.createAccessToken(authentication);
        String refreshToken = jwtUtil.createRefreshToken(authentication);

        // Refresh Token 저장
        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        return TokenInfoDTO.builder()
            .grantType("Bearer")
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .userId(user.getId())
            .username(user.getUsername())
            .role(user.getRole())
            .build();
    }

    // Access Token 재발급
    public TokenInfoDTO refreshAccessToken(RefreshTokenDTO request) {
        // Refresh Token 유효성 검증
        if (!jwtUtil.validateToken(request.getRefreshToken())) {
            throw new InvalidTokenException("유효하지 않은 Refresh Token입니다.");
        }

        // Refresh Token으로 사용자 조회
        User user = userRepository.findByRefreshToken(request.getRefreshToken())
            .orElseThrow(() -> new EntityNotFoundException("Refresh Token을 찾을 수 없습니다."));

        // 새로운 Access Token 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            user.getUsername(),
            null,
            user.getAuthorities()
        );

        String newAccessToken = jwtUtil.createAccessToken(authentication);

        return TokenInfoDTO.builder()
            .grantType("Bearer")
            .accessToken(newAccessToken)
            .refreshToken(request.getRefreshToken())
            .userId(user.getId())
            .username(user.getUsername())
            .role(user.getRole())
            .build();
    }

    // 로그아웃
    public void logoutByUsername(String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        user.setRefreshToken(null);
        userRepository.save(user);
    }

    public void logoutByRefreshToken(String refreshToken) {
        // Refresh Token으로 사용자 조회 (없으면 저장)
        userRepository.findByRefreshToken(refreshToken)
            .ifPresent(user -> {
                user.setRefreshToken(null);
                userRepository.save(user);
            });
    }

    // 현재 사용자 정보 조회
    public UserInfoDTO getCurrentUser(String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        return UserInfoDTO.builder()
            .id(user.getId())
            .username(user.getUsername())
            .role(user.getRole())
            .build();
    }
}