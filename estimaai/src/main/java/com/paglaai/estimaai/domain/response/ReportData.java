package com.paglaai.estimaai.domain.response;

import com.paglaai.estimaai.domain.BreakdownData;
import lombok.Data;

import java.util.List;

@Data
public class ReportData {
    private String title;
    private List<BreakdownData> breakdownDataList;
    private long totalTime;
}
