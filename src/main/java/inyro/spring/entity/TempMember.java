package inyro.spring.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TempMember {
    private String email;
    private String role;
    private String department;

    public TempMember(String email, String role, String department) {
        this.email = email;
        this.role = role;
        this.department = department;
    }
}
