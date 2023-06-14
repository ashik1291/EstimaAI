package com.paglaai.estimaai.domain.response;

import com.paglaai.estimaai.domain.BreakdownData;
import java.util.List;
import lombok.Data;

@Data
public class ReportData {
  private String title;
  private List<BreakdownData> breakdownDataList;
  private long totalTime;
}
