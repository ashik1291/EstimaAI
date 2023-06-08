package com.paglaai.estimaai.domain.response.ml;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class MlEstimaResponse {
    @JsonProperty("FN")
    private String featureTitle;

    @JsonProperty("subtasks")
    private List<Subtask> subtasks;


}
