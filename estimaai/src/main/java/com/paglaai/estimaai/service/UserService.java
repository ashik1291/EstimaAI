package com.paglaai.estimaai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paglaai.estimaai.domain.UserDto;
import com.paglaai.estimaai.domain.UserProfileWithHistories;
import com.paglaai.estimaai.domain.request.TeamMemberSurveyRequest;
import com.paglaai.estimaai.domain.response.ReportData;
import com.paglaai.estimaai.domain.response.WrapperReportData;
import com.paglaai.estimaai.exception.UserNotFoundException;
import com.paglaai.estimaai.mapper.DtoToEntityMapper;
import com.paglaai.estimaai.repository.ReportHistoryRepository;
import com.paglaai.estimaai.repository.UserRepository;
import com.paglaai.estimaai.repository.UserTeamMemberSurveyRepository;
import com.paglaai.estimaai.repository.entity.ReportHistoryEntity;
import com.paglaai.estimaai.repository.entity.UserEntity;
import com.paglaai.estimaai.repository.entity.UserTeamMemberSurveyEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserTeamMemberSurveyRepository userTeamMemberSurveyRepository;
    private final ReportHistoryRepository reportHistoryRepository;

    public UserDto findUserByEmail(String email) {

        var userEntity =  userRepository.findByEmail(email);
        if(userEntity == null){
            throw new UserNotFoundException("No user was found.");
        }
        return mapToUserDto(userEntity);
    }


    public List<UserDto> findAllUsers() {
        List<UserEntity> users = userRepository.findAll();
        return users.stream()
                .map(this::mapToUserDto)
                .collect(Collectors.toList());
    }

    public UserProfileWithHistories getProfileWithReportHistory() throws JsonProcessingException {
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        var userEntity = userRepository.findByEmail(email);
        if(userEntity == null){
            throw new UserNotFoundException("No user profile found.");
        }

        var userProfileWithHistories = new UserProfileWithHistories();
        userProfileWithHistories.setName(userEntity.getName());
        userProfileWithHistories.setEmail(email);
        userProfileWithHistories.setReportHistories(userEntity.getReportHistoryEntities());
        userProfileWithHistories.setUserTeamMemberSurvey(userEntity.getUserTeamMemberSurveyEntity());

        return userProfileWithHistories;
    }

    public Boolean createOrUpdateUserTeamMemberSurvey(TeamMemberSurveyRequest teamMemberSurveyRequest){

        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        var userEntity = userRepository.findByEmail(email);
        if(userEntity == null){
            throw new UserNotFoundException("No user profile found to set survey");
        }


        if(teamMemberSurveyRequest.getId() == 0 || teamMemberSurveyRequest.getId() == null){
            var existingSurveyForUser = userTeamMemberSurveyRepository.findByUserEntity_Id(userEntity.getId());
            if(existingSurveyForUser != null){
                throw new RuntimeException("can't create team survey. Because survey already exists for user");
            }
            var userTeamMemberSurveyEntity =  DtoToEntityMapper.requestToEntityWithoutIdAndUserEntity(teamMemberSurveyRequest);
            userTeamMemberSurveyEntity.setUserEntity(userEntity);
            userTeamMemberSurveyRepository.save(userTeamMemberSurveyEntity);

        }else{
           var surveyEntityOptional = userTeamMemberSurveyRepository.findById(teamMemberSurveyRequest.getId());
           if(surveyEntityOptional.isEmpty()){
               throw new RuntimeException("no survey record found to update");
           }

           var surveyRecord = surveyEntityOptional.get();
           var userTeamMemberSurveyEntity =  this.requestToEntityForUpdate(surveyRecord, teamMemberSurveyRequest);

            userTeamMemberSurveyRepository.save(userTeamMemberSurveyEntity);
        }

        return true;
    }

    public Boolean saveProcessedDataForReport(WrapperReportData data, String title){
        var reportHistoryEntity = new ReportHistoryEntity();
        reportHistoryEntity.setTitle(title.concat(" Project Estimation"));
        reportHistoryEntity.setGenerationTime(LocalDateTime.now());
        reportHistoryEntity.setJsonData(data);
        reportHistoryEntity.setUsers(userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()));
        reportHistoryRepository.save(reportHistoryEntity);

        return true;
    }

    private UserTeamMemberSurveyEntity requestToEntityForUpdate(UserTeamMemberSurveyEntity userTeamMemberSurveyEntity, TeamMemberSurveyRequest teamMemberSurveyRequest){

        if(teamMemberSurveyRequest.getTeamExp() != null){
            userTeamMemberSurveyEntity.setTeamExp(teamMemberSurveyRequest.getTeamExp());
        }
        if(teamMemberSurveyRequest.getManagerExp() != null){
            userTeamMemberSurveyEntity.setManagerExp(teamMemberSurveyRequest.getManagerExp());
        }
        if(teamMemberSurveyRequest.getYearEnd() != null){
            userTeamMemberSurveyEntity.setYearEnd(teamMemberSurveyRequest.getYearEnd());
        }
        if(teamMemberSurveyRequest.getLength() != null){
            userTeamMemberSurveyEntity.setLength(teamMemberSurveyRequest.getLength());
        }
        if(teamMemberSurveyRequest.getEffort() != null){
            userTeamMemberSurveyEntity.setEffort(teamMemberSurveyRequest.getEffort());
        }
        if(teamMemberSurveyRequest.getTransactions() != null){
            userTeamMemberSurveyEntity.setTransactions(teamMemberSurveyRequest.getTransactions());
        }
        if(teamMemberSurveyRequest.getEntities() != null){
            userTeamMemberSurveyEntity.setEntities(teamMemberSurveyRequest.getEntities());
        }
        if(teamMemberSurveyRequest.getPointsAdjust() != null){
            userTeamMemberSurveyEntity.setPointsAdjust(teamMemberSurveyRequest.getPointsAdjust());
        }
        if(teamMemberSurveyRequest.getEnvergure() != null){
            userTeamMemberSurveyEntity.setEntities(teamMemberSurveyRequest.getEnvergure());
        }
        if(teamMemberSurveyRequest.getPointsNonAdjust() != null){
            userTeamMemberSurveyEntity.setPointsNonAdjust(teamMemberSurveyRequest.getPointsNonAdjust());
        }
        if(teamMemberSurveyRequest.getLanguage() != null){
            userTeamMemberSurveyEntity.setLanguage(teamMemberSurveyRequest.getLanguage());
        }

        return userTeamMemberSurveyEntity;
    }

    private UserDto mapToUserDto(UserEntity user){
        UserDto userDto = new UserDto();
        String[] str = user.getName().split(" ");
        userDto.setFirstName(str[0]);
        userDto.setLastName(str[1]);
        userDto.setEmail(user.getEmail());
        return userDto;
    }
}
