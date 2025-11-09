package com.ai.api.auth.controller;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TestController {

    // 인증된 사용자만 접근 가능
    @GetMapping("/user/test")
    public ResponseEntity<Map<String, Object>> userTest(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "USER 권한으로 접근 성공");
        response.put("user", authentication.getName());
        response.put("authorities", authentication.getAuthorities());
        return ResponseEntity.ok(response);
    }

    // ADMIN 권한만 접근 가능
    @GetMapping("/admin/test")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> adminTest(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "ADMIN 권한으로 접근 성공");
        response.put("user", authentication.getName());
        response.put("authorities", authentication.getAuthorities());
        return ResponseEntity.ok(response);
    }

    // 공개 API (인증 불필요)
    @GetMapping("/public/test")
    public ResponseEntity<Map<String, String>> publicTest() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "누구나 접근 가능한 공개 API");
        return ResponseEntity.ok(response);
    }
}
