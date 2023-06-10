package com.paglaai.estimaai.domain.request;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TeamMemberSurveyRequest {
  private Long id;
  private Float teamExp;
  private Float managerExp;
  private Float yearEnd;
  private Float length;
  private Float effort;
  private Float transactions;
  private Float entities;
  private Float pointsAdjust;
  private Float envergure;
  private Float pointsNonAdjust;
  private Float language;
}
