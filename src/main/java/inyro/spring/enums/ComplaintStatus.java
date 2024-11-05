package inyro.spring.enums;

import lombok.Getter;

@Getter
public enum ComplaintStatus {
    NEW(0, "진행중"),
    COMPLETED(1, "완료");

    private final int code;
    private final String description;

    ComplaintStatus(int code, String description){
        this.code = code;
        this.description = description;
    }
}
