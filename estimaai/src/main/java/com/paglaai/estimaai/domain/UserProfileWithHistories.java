package com.paglaai.estimaai.domain;

import com.paglaai.estimaai.repository.entity.ReportHistoryEntity;
import com.paglaai.estimaai.repository.entity.UserTeamMemberSurveyEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class UserProfileWithHistories{

    private String name;
    private String email;
    private List<ReportHistoryEntity> reportHistories;
    private UserTeamMemberSurveyEntity userTeamMemberSurvey;
}
