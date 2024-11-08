package inyro.spring.enums;

import lombok.Getter;

@Getter
public enum Department {
    민원("민원 관련 부서"),
    행정("행정 관련 부서"),
    보건("보건 관련 부서"),
    교육("교육 관련 부서");

    private final String description;

    // description 값을 설정하는 생성자
    Department(String description) {
        this.description = description;
    }
}
