package inyro.spring.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "temp_member") 
public class TempMember {
    @Id
    private String email;

    @Column(nullable = false)
    private String role;
    
    @Column(nullable = false)
    private String department;

    public TempMember(String email, String role, String department) {
        this.email = email;
        this.role = role;
        this.department = department;
    }
}
