package com.ai.common.security;

import com.ai.common.util.CookieUtil;
import com.ai.common.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;

    @Value("${jwt.cookie.access-token.name}")
    private String accessTokenCookieName;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private static final List<String> EXCLUDE_PATHS = Arrays.asList(
        "/auth/login",
        "/auth/signup",
        "/auth/refresh"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestURI = request.getRequestURI();
        // /auth/로 시작하는 모든 경로는 필터를 건너뜁니다.
        return EXCLUDE_PATHS.stream()
            .anyMatch(path -> pathMatcher.match(path, requestURI));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        try {
            String token = cookieUtil.getCookie(request, accessTokenCookieName);

            if (!StringUtils.hasText(token)) { // 쿠키에 없으면 Authorization 헤더에서 추출
                token = resolveTokenFromHeader(request);
            }

            if (StringUtils.hasText(token) && jwtUtil.validateToken(token)) {
                Authentication authentication = jwtUtil.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication); // SecurityContext에 Authentication 객체 저장
                log.debug("Security Context에 '{}' 인증 정보를 저장했습니다.", authentication.getName());
            }
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT 토큰입니다: {}", e.getMessage());
            // 만료 시 401 에러를 응답하고 필터 체인을 즉시 중단
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("{\"message\": \"Access Token Expired\"}");
            return; // <-- 중요: 필터 체인 중단
        } catch (Exception e) {
            log.error("유효하지 않은 JWT 토큰입니다: {}", e.getMessage());
            // 그 외 모든 JWT 예외(서명 오류 등)도 401 응답
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("{\"message\": \"Invalid JWT Token\"}");
            return; // <-- 중요: 필터 체인 중단
        }
        filterChain.doFilter(request, response);
    }

    private String resolveTokenFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
