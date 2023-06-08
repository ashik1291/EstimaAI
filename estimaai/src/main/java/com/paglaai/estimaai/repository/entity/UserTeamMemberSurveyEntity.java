package com.paglaai.estimaai.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_team_member_survey")
public class UserTeamMemberSurveyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private float teamExp;
    private float managerExp;
    private float yearEnd;
    private float length;
    private float effort;
    private float transactions;
    private float entities;
    private float pointsAdjust;
    private float envergure;
    private float pointsNonAdjust;
    private float language;

    @OneToOne
    @JoinColumn(name = "userEntity_id")
    @JsonIgnore
    private UserEntity userEntity;
}
