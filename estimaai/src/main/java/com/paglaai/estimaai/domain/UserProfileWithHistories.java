package com.paglaai.estimaai.domain;

import com.paglaai.estimaai.repository.entity.ReportHistoryEntity;
import com.paglaai.estimaai.repository.entity.UserTeamMemberSurveyEntity;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileWithHistories {

  private String name;
  private String email;
  private List<ReportHistoryEntity> reportHistories;
  private UserTeamMemberSurveyEntity userTeamMemberSurvey;
}
