package com.paglaai.estimaai.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.paglaai.estimaai.domain.UserDto;
import com.paglaai.estimaai.domain.UserProfileWithHistories;
import com.paglaai.estimaai.domain.request.TeamMemberSurveyRequest;
import com.paglaai.estimaai.domain.response.WrapperReportData;
import com.paglaai.estimaai.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
  private final UserService userService;

  @GetMapping("/profile/{email}")
  public ResponseEntity<UserDto> getUserProfileByEmail(@PathVariable String email) {
    return ResponseEntity.ok(userService.findUserByEmail(email));
  }

  @GetMapping("/profile-report-histories")
  public ResponseEntity<UserProfileWithHistories> getUserProfileWithReportHistories()
      throws JsonProcessingException {
    return ResponseEntity.ok(userService.getProfileWithReportHistory());
  }

  @PostMapping("/team-member-survey")
  public ResponseEntity<Boolean> saveProcessStoryToUserProfile(
      @RequestBody TeamMemberSurveyRequest teamMemberSurveyRequest) {
    return ResponseEntity.ok(
        userService.createOrUpdateUserTeamMemberSurvey(teamMemberSurveyRequest));
  }

  @PostMapping("/save-processed-stories")
  public ResponseEntity<Boolean> saveProcessStoryToUserProfile(
      @RequestBody WrapperReportData reportDataList, @RequestParam String title) {
    return ResponseEntity.ok(userService.saveProcessedStoryToUserProfile(reportDataList, title));
  }
}
