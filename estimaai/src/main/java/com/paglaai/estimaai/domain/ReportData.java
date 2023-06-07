package com.paglaai.estimaai.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ReportData {
    private String featureTitle;
    private String featureIntent;
    private String subtasksOfFeatures;
    private String implementationTime;
    private String complexity;
    private String kloc;
}
