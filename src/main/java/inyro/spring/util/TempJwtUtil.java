package inyro.spring.util;

import org.springframework.stereotype.Component;

@Component
public class TempJwtUtil {
    // 임시 구현
    public String getRole(String token) {
        // 테스트를 위해 항상 STUDENT 반환
        return "ROLE_STUDENT";
    }

    public String getName(String token) {
        // 테스트를 위해 고정된 이름 반환
        return "Test Student";
    }
}
