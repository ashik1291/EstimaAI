package com.paglaai.estimaai.repository;

import com.paglaai.estimaai.repository.entity.UserTeamMemberSurveyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTeamMemberSurveyRepository extends JpaRepository <UserTeamMemberSurveyEntity, Long> {

    UserTeamMemberSurveyEntity findByUserEntity_Id(long id);
}
