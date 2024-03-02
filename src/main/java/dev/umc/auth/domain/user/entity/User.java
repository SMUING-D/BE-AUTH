package dev.umc.auth.domain.user.entity;

import dev.umc.auth.domain.user.enums.Grade;
import dev.umc.auth.domain.user.enums.UndergraduateGraduate;
import dev.umc.auth.global.config.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", updatable = false)
    private Long id;

    @Column(name = "student_id")
    private Long studentId;  // 학번 (아이디)

    @Column(name = "password")
    private String password;  // 비밀번호

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    private String major;  // 전공

    private String email;  // 이메일

    private String nickname;  // 닉네임

    @Column(name = "desired_employment")
    private String desiredEmployment;  // 취업 희망 분야

    private String role;  // ADMIN, USER

    @Column(name = "skill")
    private String skill;  // 자격증 및 기술 스택

    @Enumerated(EnumType.STRING)
    private Grade grade;  // 학년

    private String college;  // 단과대

    @Enumerated(EnumType.STRING)
    @Column(name = "undergraduate_graduate")
    private UndergraduateGraduate undergraduateGraduate;  // 대학생 혹은 대학원생

    private String profileImg;  // 프로필 이미지

    @Builder
    public User(String username, String password, String role, Long studentId, String major, String email, String nickname, String desiredEmployment, String skill, Grade grade, String college, UndergraduateGraduate undergraduateGraduate, String profileImg) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.studentId = studentId;
        this.major = major;
        this.email = email;
        this.nickname = nickname;
        this.desiredEmployment = desiredEmployment;
        this.skill = skill;
        this.grade = grade;
        this.college = college;
        this.undergraduateGraduate = undergraduateGraduate;
        this.profileImg = profileImg;
    }
}
