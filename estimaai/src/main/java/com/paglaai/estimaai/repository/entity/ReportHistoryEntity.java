package com.paglaai.estimaai.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.paglaai.estimaai.configuration.StartupConfiguration;
import com.paglaai.estimaai.domain.response.WrapperReportData;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("all")
@Table(name = "report_history")
public class ReportHistoryEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String Title;

  @Column(columnDefinition = "text")
  private String jsonData;

  private LocalDateTime generationTime;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "users_id")
  @ToString.Exclude
  private UserEntity users;

  public WrapperReportData getJsonData() {
    try {
      var data = StartupConfiguration.objectMapper().readValue(jsonData, WrapperReportData.class);
      if (data != null) {
        return data;
      }
    } catch (Exception ignored) {
    }
    return new WrapperReportData();
  }

  public void setJsonData(Object jsonData) {
    try {
      this.jsonData = StartupConfiguration.objectMapper().writeValueAsString(jsonData);
    } catch (JsonProcessingException e) {
      this.jsonData = "{}";
    }
  }
}
