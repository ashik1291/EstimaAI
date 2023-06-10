package com.paglaai.estimaai.domain.response;

import java.util.List;
import lombok.Data;

@Data
public class WrapperReportData {
  private long totalTime;
  private List<ReportData> reportDataList;
}
