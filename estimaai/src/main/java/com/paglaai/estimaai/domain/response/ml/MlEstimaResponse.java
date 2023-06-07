package com.paglaai.estimaai.domain.response.ml;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class MlEstimaResponse {
    @JsonProperty("FN")
    private String featureTitle;

    @JsonProperty("FINT")
    private String featureIntent;

    @JsonProperty("ST")
    private String subtasksOfFeatures;

    @JsonProperty("IT")
    private String implementationTime;

    @JsonProperty("CMPX")
    private String complexity;
}
