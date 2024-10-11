package com.example.querydsl.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = { "id", "userName", "age" })
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String userName;

    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    // 생성자 메소드

    public Member(String userName, int age, Team team) {
        this.userName = userName;
        this.age = age;
        if (team != null) {
            changeTeam(team);
        }
    }

    public Member(String userName) {
        this.userName = userName;
    }

    public Member(String userName, int age) {
        this.userName = userName;
        this.age = age;
    }

    private void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }

}
