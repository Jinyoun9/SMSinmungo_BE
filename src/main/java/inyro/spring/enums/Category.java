package inyro.spring.enums;

import lombok.Getter;

@Getter
public enum Category {
    민원(1,"민원"),
    의견(2,"의견");

    private final int code;
    private final String description;

    Category(int code,String description){
        this.code = code;
        this.description = description;
    }
}
