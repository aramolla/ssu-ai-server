package com.ai.common.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CookieUtil {

    @Value("${jwt.cookie.http-only}")
    private boolean httpOnly;

    @Value("${jwt.cookie.secure}")
    private boolean secure;

    @Value("${jwt.cookie.same-site}")
    private String sameSite;

    @Value("${jwt.cookie.domain}")
    private String domain;

    public void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
//        cookie.setHttpOnly(httpOnly);
        cookie.setSecure(secure);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);

        // localhost가 아닌 경우에만 domain 설정
        if (!"localhost".equals(domain)) {
            cookie.setDomain(domain);
        }

        response.addCookie(cookie);

        // SameSite 속성은 Set-Cookie 헤더에 직접 추가
        String setCookieHeader = String.format(
            "%s=%s; Path=/; Max-Age=%d; HttpOnly=%b; Secure=%b; SameSite=%s",
            name, value, maxAge, httpOnly, secure, sameSite
        );
        response.addHeader("Set-Cookie", setCookieHeader);

        log.debug("쿠키 추가: name={}, maxAge={}", name, maxAge);
    }

    public void deleteCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setHttpOnly(httpOnly);
        cookie.setSecure(secure);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        if (!"localhost".equals(domain)) {
            cookie.setDomain(domain);
        }

        response.addCookie(cookie);

        log.debug("쿠키 삭제: name={}", name);
    }


    public String getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    log.debug("쿠키 추출: name={}", name);
                    return cookie.getValue();
                }
            }
        }
        log.debug("쿠키를 찾을 수 없음: name={}", name);
        return null;
    }
}