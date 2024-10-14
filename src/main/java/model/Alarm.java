package model;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "alarm")
public class Alarm {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long alarm_id; //=post_id
    @Column(nullable = false)
    private Long member_id;
    @Column(nullable = false, length = 100)
    private String title;

}
