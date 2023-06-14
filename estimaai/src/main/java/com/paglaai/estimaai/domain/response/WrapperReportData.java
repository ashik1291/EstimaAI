package com.paglaai.estimaai.domain.response;

import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class WrapperReportData {
  private long totalTime;
  private List<ReportData> reportDataList;
}
