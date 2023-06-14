package com.paglaai.estimaai.mapper;

import com.paglaai.estimaai.domain.UserDto;
import com.paglaai.estimaai.domain.request.TeamMemberSurveyRequest;
import com.paglaai.estimaai.repository.entity.UserEntity;
import com.paglaai.estimaai.repository.entity.UserTeamMemberSurveyEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DtoToEntityMapper {

  private final PasswordEncoder passwordEncoder;

  public UserEntity userDtoToEntity(UserDto userDto) {

    var name =
        userDto.getLastName() != null
            ? userDto.getFirstName().concat(" ").concat(userDto.getLastName())
            : userDto.getFirstName().concat("");

    return new UserEntity()
        .setName(name)
        .setEmail(userDto.getEmail())
        .setPassword(passwordEncoder.encode(userDto.getPassword()));
  }

  public static UserTeamMemberSurveyEntity requestToEntityWithoutIdAndUserEntity(
      TeamMemberSurveyRequest teamMemberSurveyRequest) {

    var userTeamMemberSurveyEntity = new UserTeamMemberSurveyEntity();

    if (teamMemberSurveyRequest.getTeamExp() != null) {
      userTeamMemberSurveyEntity.setTeamExp(teamMemberSurveyRequest.getTeamExp());
    }
    if (teamMemberSurveyRequest.getManagerExp() != null) {
      userTeamMemberSurveyEntity.setManagerExp(teamMemberSurveyRequest.getManagerExp());
    }
    if (teamMemberSurveyRequest.getYearEnd() != null) {
      userTeamMemberSurveyEntity.setYearEnd(teamMemberSurveyRequest.getYearEnd());
    }
    if (teamMemberSurveyRequest.getLength() != null) {
      userTeamMemberSurveyEntity.setLength(teamMemberSurveyRequest.getLength());
    }
    if (teamMemberSurveyRequest.getEffort() != null) {
      userTeamMemberSurveyEntity.setEffort(teamMemberSurveyRequest.getEffort());
    }
    if (teamMemberSurveyRequest.getTransactions() != null) {
      userTeamMemberSurveyEntity.setTransactions(teamMemberSurveyRequest.getTransactions());
    }
    if (teamMemberSurveyRequest.getEntities() != null) {
      userTeamMemberSurveyEntity.setEntities(teamMemberSurveyRequest.getEntities());
    }
    if (teamMemberSurveyRequest.getPointsAdjust() != null) {
      userTeamMemberSurveyEntity.setPointsAdjust(teamMemberSurveyRequest.getPointsAdjust());
    }
    if (teamMemberSurveyRequest.getEnvergure() != null) {
      userTeamMemberSurveyEntity.setEntities(teamMemberSurveyRequest.getEnvergure());
    }
    if (teamMemberSurveyRequest.getPointsNonAdjust() != null) {
      userTeamMemberSurveyEntity.setPointsNonAdjust(teamMemberSurveyRequest.getPointsNonAdjust());
    }
    if (teamMemberSurveyRequest.getLanguage() != null) {
      userTeamMemberSurveyEntity.setLanguage(teamMemberSurveyRequest.getLanguage());
    }

    return userTeamMemberSurveyEntity;
  }
}
