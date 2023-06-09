package com.paglaai.estimaai.domain.response;

import lombok.Data;

import java.util.List;

@Data
public class WrapperReportData {
    private long totalTime;
    private List<ReportData> reportDataList;
}
