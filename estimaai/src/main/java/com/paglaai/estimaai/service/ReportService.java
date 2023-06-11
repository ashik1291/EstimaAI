package com.paglaai.estimaai.service;

import com.paglaai.estimaai.domain.BreakdownData;
import com.paglaai.estimaai.domain.request.MLEstimaBodyRequest;
import com.paglaai.estimaai.domain.request.UserStoriesAndTitleRequest;
import com.paglaai.estimaai.domain.response.ReportData;
import com.paglaai.estimaai.domain.response.WrapperReportData;
import com.paglaai.estimaai.domain.response.ml.MlEstimaResponse;
import com.paglaai.estimaai.feign.MLFeign;
import com.paglaai.estimaai.util.StringUtil;
import com.paglaai.estimaai.util.TitleCaseUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.dynamicreports.report.constant.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {

    private final MLFeign mlFeign;

    public JasperReportBuilder generateReportWrapper(WrapperReportData data, String title) {

        JasperReportBuilder report = DynamicReports.report();

        // Create space elements
        VerticalListBuilder spaceBefore = DynamicReports.cmp.verticalList().setFixedHeight(10);
        VerticalListBuilder spaceAfter = DynamicReports.cmp.verticalList().setFixedHeight(6);

        // Header 1
        final TextFieldBuilder<String> header1 =
                DynamicReports.cmp
                        .text("Paglaa AI Inc. ")
                        .setFixedHeight(12)
                        .setStyle(
                                DynamicReports.stl
                                        .style()
                                        .setBold(true)
                                        .setFontSize(16)
                                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                        .setForegroundColor(Color.BLACK));
        // Header 2
        final TextFieldBuilder<String> header2 =
                DynamicReports.cmp
                        .text(title)
                        .setFixedHeight(12)
                        .setStyle(
                                DynamicReports.stl
                                        .style()
                                        .setFontSize(13)
                                        .setHorizontalAlignment(HorizontalAlignment.CENTER));

        // Page Header
        report.pageHeader(
                DynamicReports.cmp.verticalGap(5),
                DynamicReports.cmp.verticalList(
                        DynamicReports.cmp.verticalList(header1).setGap(10),
                        DynamicReports.cmp.horizontalFlowList(header2),
                        DynamicReports.cmp.verticalGap(2)));

        // Configuration when data splits into other pages
        //
        // report.setDetailStyle(DynamicReports.stl.style().setRightPadding(5).setLeftPadding(5));
        //        report.setDetailSplitType(SplitType.PREVENT);
        //        report.highlightDetailEvenRows();

        // Page Margin
        report.setPageMargin(DynamicReports.margin().setLeft(15).setRight(15).setTop(10).setBottom(15));
        report.setPageFormat(PageType.A4, PageOrientation.PORTRAIT);

        if (data.getReportDataList().isEmpty()) {
            report.setWhenNoDataType(WhenNoDataType.ALL_SECTIONS_NO_DETAIL);
        }

        for (var singleData : data.getReportDataList()) {
            report.addSummary(
                    DynamicReports.cmp.subreport(
                            subReport(
                                    singleData.getBreakdownDataList(),
                                    singleData.getTitle(),
                                    singleData.getTotalTime())));
        }
        report.addSummary(
                DynamicReports.cmp.subreport(
                        DynamicReports.report()
                                .pageFooter(
                                        DynamicReports.cmp
                                                .text(
                                                        "Total Project Implementation Time: "
                                                                .concat(String.valueOf(data.getTotalTime())))
                                                .setStyle(
                                                        DynamicReports.stl
                                                                .style()
                                                                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                .setFontSize(10)
                                                                .bold()
                                                                .setTopPadding(20)))));

        return report;
    }

    public JasperReportBuilder subReport(List<BreakdownData> data, String title, long totalTime) {

        JasperReportBuilder report = DynamicReports.report();

        // Create space elements
        VerticalListBuilder spaceBefore = DynamicReports.cmp.verticalList().setFixedHeight(20);
        VerticalListBuilder spaceAfter = DynamicReports.cmp.verticalList().setFixedHeight(10);

        // Header 2
        final TextFieldBuilder<String> header2 =
                DynamicReports.cmp
                        .text(title)
                        .setFixedHeight(12)
                        .setStyle(
                                DynamicReports.stl
                                        .style()
                                        .setFontSize(13)
                                        .setHorizontalAlignment(HorizontalAlignment.CENTER));

        // Page Header
        report.pageHeader(
                DynamicReports.cmp.verticalGap(15),
                DynamicReports.cmp.horizontalFlowList(header2),
                DynamicReports.cmp.verticalGap(10));

        // Columns
        report.addColumn(
                DynamicReports.col
                        .column("Feature Title", "featureTitle", DynamicReports.type.stringType())
                        .setStyle(
                                DynamicReports.stl
                                        .style()
                                        .setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE)
                                        .setBottomPadding(5)
                                        .setLeftPadding(5)
                                        .setRightPadding(5)
                                        .setTopPadding(5)
                                        .setBorder(DynamicReports.stl.penThin()))
                        .setWidth(90));

        report.addColumn(
                DynamicReports.col
                        .column("Feature Intent", "featureIntent", DynamicReports.type.stringType())
                        .setStyle(
                                DynamicReports.stl
                                        .style()
                                        .setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE)
                                        .setBottomPadding(5)
                                        .setLeftPadding(5)
                                        .setRightPadding(5)
                                        .setTopPadding(5)
                                        .setBorder(DynamicReports.stl.penThin()))
                        .setWidth(120));

        report.addColumn(
                DynamicReports.col
                        .column("Subtasks of Feature", "subtasksOfFeatures", DynamicReports.type.stringType())
                        .setStyle(
                                DynamicReports.stl
                                        .style()
                                        .setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE)
                                        .setBottomPadding(5)
                                        .setLeftPadding(5)
                                        .setRightPadding(5)
                                        .setTopPadding(5)
                                        .setBorder(DynamicReports.stl.penThin()))
                        .setWidth(120));

        report.addColumn(
                DynamicReports.col
                        .column("Complexity (1-5)", "complexity", DynamicReports.type.stringType())
                        .setStyle(
                                DynamicReports.stl
                                        .style()
                                        .setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE)
                                        .setBottomPadding(5)
                                        .setLeftPadding(5)
                                        .setRightPadding(5)
                                        .setTopPadding(5)
                                        .setBorder(DynamicReports.stl.penThin()))
                        .setWidth(90));

        report.addColumn(
                DynamicReports.col
                        .column("Duration (hours)", "implementationTime", DynamicReports.type.stringType())
                        .setStyle(
                                DynamicReports.stl
                                        .style()
                                        .setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE)
                                        .setBottomPadding(5)
                                        .setLeftPadding(5)
                                        .setRightPadding(5)
                                        .setTopPadding(5)
                                        .setBorder(DynamicReports.stl.penThin()))
                        .setWidth(80));

        // Column Header
        report.setColumnHeaderStyle(
                DynamicReports.stl
                        .style()
                        .setRightPadding(5)
                        .setLeftPadding(5)
                        .setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE));
        report.setColumnTitleStyle(
                DynamicReports.stl
                        .style(DynamicReports.stl.style().bold())
                        .setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE)
                        .setBorder(DynamicReports.stl.pen1Point())
                        .setLeftPadding(5)
                        .setRightPadding(5)
                        .setBottomPadding(5)
                        .setTopPadding(5)
                        .setBackgroundColor(Color.decode("#317773"))
                        .bold()
                        .setForegroundColor(Color.WHITE)
                        .setBorder(DynamicReports.stl.penThin().setLineColor(Color.BLACK)));

        // Configuration when data splits into other pages
        report.setDetailStyle(DynamicReports.stl.style().setRightPadding(5).setLeftPadding(5));
        report.setDetailSplitType(SplitType.PREVENT);
        report.highlightDetailEvenRows();

        if (data.isEmpty()) {
            report.setWhenNoDataType(WhenNoDataType.ALL_SECTIONS_NO_DETAIL);
        }

        data.add(
                new BreakdownData()
                        .setImplementationTime("Total Time: ".concat(String.valueOf(totalTime))));
        report.setDataSource(data);

        return report;
    }

    public WrapperReportData getProcessedFeatureList(List<UserStoriesAndTitleRequest> userStoriesAndTitleRequests) {
        var wrapperReportData = new WrapperReportData();
        var reportDataList = new ArrayList<ReportData>();
        long totalProjectTime = 0;

        for (var userStoryAndTitle : userStoriesAndTitleRequests) {
            List<MlEstimaResponse> mlResponseList;

            try {
                mlResponseList = mlFeign.getFeatures(new MLEstimaBodyRequest().setStory(userStoryAndTitle.getUserStory()));
            } catch (Exception exception) {
                log.error(exception.getMessage());
                continue;
            }

            for (var mlResponse : mlResponseList) {
                var reportData = new ReportData();  // Move the creation inside the loop
                reportData.setTitle(TitleCaseUtil.convertToTitleCase(userStoryAndTitle.getTitle()));

                var breakDownList = new ArrayList<BreakdownData>();
                var subTask = mlResponse.getSubtasks();

                for (var sub : subTask) {
                    var breakdownData = new BreakdownData();
                    breakdownData.setFeatureTitle(mlResponse.getFeatureTitle());
                    breakdownData.setSubtasksOfFeatures(sub.getSubtasksOfFeatures());
                    breakdownData.setFeatureIntent(sub.getFeatureIntent());
                    breakdownData.setImplementationTime(sub.getImplementationTime());
                    breakdownData.setComplexity(sub.getComplexity());
                    breakDownList.add(breakdownData);
                }

                var postProcessedData = postProcessedData(breakDownList);
                reportData.setBreakdownDataList(postProcessedData);

                var subStoryTime = getTotalTime(breakDownList);
                reportData.setTotalTime(subStoryTime);
                totalProjectTime += subStoryTime;

                reportDataList.add(reportData);  // Add reportData inside the loop
            }
        }

        wrapperReportData.setReportDataList(reportDataList);
        wrapperReportData.setTotalTime(totalProjectTime);

        return wrapperReportData;
    }



    private List<BreakdownData> postProcessedData(List<BreakdownData> pojoList) {
        List<BreakdownData> modifiedList = new ArrayList<>();
        if (pojoList.size() > 0) {
            modifiedList.add(pojoList.get(0));
        } else {
            log.error("pojoList is empty while post process data");
            throw new RuntimeException("Error in Post processing");
        }
        String currentFeatureTitle = pojoList.get(0).getFeatureTitle();
        String currentFeatureIntent = pojoList.get(0).getFeatureIntent();
        for (int i = 1; i < pojoList.size(); i++) {
            var currentPojo = pojoList.get(i);
            if (currentPojo.getFeatureTitle() != null
                    && currentPojo.getFeatureTitle().equalsIgnoreCase(StringUtil.nullToEmptyString(currentFeatureTitle))) {
                currentPojo.setFeatureTitle(null);
            } else {
                currentFeatureTitle = currentPojo.getFeatureTitle();
            }
            if (currentPojo.getFeatureIntent() != null
                    && currentPojo.getFeatureIntent().equalsIgnoreCase(StringUtil.nullToEmptyString(currentFeatureIntent))) {
                currentPojo.setFeatureIntent(null);
            } else {
                currentFeatureIntent = currentPojo.getFeatureIntent();
            }

            modifiedList.add(currentPojo);
        }
        return modifiedList;
    }

    private long getTotalTime(List<BreakdownData> pojoList) {
        long count = 0;

        for (var pojo : pojoList) {
            count += Long.parseLong(pojo.getImplementationTime().replaceAll("[.,\\p{P}]", ""));
        }

        return count;
    }

    /**
     * Method for Backup
     */
    public JasperReportBuilder generateReport(List<BreakdownData> data, String titleOfProject) {

        JasperReportBuilder report = DynamicReports.report();

        // Create space elements
        VerticalListBuilder spaceBefore = DynamicReports.cmp.verticalList().setFixedHeight(20);
        VerticalListBuilder spaceAfter = DynamicReports.cmp.verticalList().setFixedHeight(10);

        // Header 1
        final TextFieldBuilder<String> header1 =
                DynamicReports.cmp
                        .text("Paglaa AI Inc. ")
                        .setFixedHeight(12)
                        .setStyle(
                                DynamicReports.stl
                                        .style()
                                        .setBold(true)
                                        .setFontSize(16)
                                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                        .setForegroundColor(Color.BLACK));
        // Header 2
        final TextFieldBuilder<String> header2 =
                DynamicReports.cmp
                        .text(TitleCaseUtil.convertToTitleCase(titleOfProject))
                        .setFixedHeight(12)
                        .setStyle(
                                DynamicReports.stl
                                        .style()
                                        .setFontSize(13)
                                        .setHorizontalAlignment(HorizontalAlignment.CENTER));

        // Page Header
        report.pageHeader(
                DynamicReports.cmp.verticalGap(5),
                DynamicReports.cmp.verticalList(
                        DynamicReports.cmp.verticalList(header1).setGap(5).add(spaceAfter),
                        DynamicReports.cmp.horizontalFlowList(header2),
                        DynamicReports.cmp.verticalGap(10)));

        // Footer 1
        final TextFieldBuilder<String> footer1 =
                DynamicReports.cmp
                        .text("Notes:")
                        .setFixedHeight(12)
                        .setStyle(
                                DynamicReports.stl
                                        .style()
                                        .setFontSize(12)
                                        .setBold(true)
                                        .setHorizontalAlignment(HorizontalAlignment.LEFT));
        // Footer 2
        final TextFieldBuilder<String> footer2 =
                DynamicReports.cmp
                        .text("* This report is provided by EstimaAI.")
                        .setFixedHeight(12)
                        .setStyle(
                                DynamicReports.stl
                                        .style()
                                        .setFontSize(9)
                                        .setHorizontalAlignment(HorizontalAlignment.LEFT));

        // Page Footer
        report.pageFooter(
                DynamicReports.cmp.verticalGap(10),
                DynamicReports.cmp.verticalList(
                        DynamicReports.cmp.verticalList(footer1).setGap(10),
                        DynamicReports.cmp.horizontalFlowList(footer2)),
                DynamicReports.cmp.pageXofY());

        // Columns
        report.addColumn(
                DynamicReports.col
                        .column("Feature Title", "featureTitle", DynamicReports.type.stringType())
                        .setStyle(
                                DynamicReports.stl
                                        .style()
                                        .setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE)
                                        .setBottomPadding(5)
                                        .setLeftPadding(5)
                                        .setRightPadding(5)
                                        .setTopPadding(5)
                                        .setBorder(DynamicReports.stl.penThin()))
                        .setWidth(90));

        report.addColumn(
                DynamicReports.col
                        .column("Feature Intent", "featureIntent", DynamicReports.type.stringType())
                        .setStyle(
                                DynamicReports.stl
                                        .style()
                                        .setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE)
                                        .setBottomPadding(5)
                                        .setLeftPadding(5)
                                        .setRightPadding(5)
                                        .setTopPadding(5)
                                        .setBorder(DynamicReports.stl.penThin()))
                        .setWidth(120));

        report.addColumn(
                DynamicReports.col
                        .column("Subtasks of Features", "subtasksOfFeatures", DynamicReports.type.stringType())
                        .setStyle(
                                DynamicReports.stl
                                        .style()
                                        .setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE)
                                        .setBottomPadding(5)
                                        .setLeftPadding(5)
                                        .setRightPadding(5)
                                        .setTopPadding(5)
                                        .setBorder(DynamicReports.stl.penThin()))
                        .setWidth(120));

        report.addColumn(
                DynamicReports.col
                        .column("Duration (hours)", "implementationTime", DynamicReports.type.stringType())
                        .setStyle(
                                DynamicReports.stl
                                        .style()
                                        .setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE)
                                        .setBottomPadding(5)
                                        .setLeftPadding(5)
                                        .setRightPadding(5)
                                        .setTopPadding(5)
                                        .setBorder(DynamicReports.stl.penThin()))
                        .setWidth(80));

        report.addColumn(
                DynamicReports.col
                        .column("Complexity (1-5)", "complexity", DynamicReports.type.stringType())
                        .setStyle(
                                DynamicReports.stl
                                        .style()
                                        .setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE)
                                        .setBottomPadding(5)
                                        .setLeftPadding(5)
                                        .setRightPadding(5)
                                        .setTopPadding(5)
                                        .setBorder(DynamicReports.stl.penThin()))
                        .setWidth(90));
        //        report.addColumn(
        //                DynamicReports.col.column("KLOC", "kloc", DynamicReports.type.stringType())
        //                        .setStyle(DynamicReports.stl.style()
        //                                .setTextAlignment(HorizontalTextAlignment.CENTER,
        // VerticalTextAlignment.MIDDLE)
        //
        // .setBottomPadding(5).setLeftPadding(5).setRightPadding(5).setTopPadding(5)
        //                                .setBorder(DynamicReports.stl.penThin()))
        //                        .setWidth(70));

        // Column Header
        report.setColumnHeaderStyle(
                DynamicReports.stl
                        .style()
                        .setRightPadding(5)
                        .setLeftPadding(5)
                        .setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE));
        report.setColumnTitleStyle(
                DynamicReports.stl
                        .style(DynamicReports.stl.style().bold())
                        .setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE)
                        .setBorder(DynamicReports.stl.pen1Point())
                        .setLeftPadding(5)
                        .setRightPadding(5)
                        .setBottomPadding(5)
                        .setTopPadding(5)
                        .setBackgroundColor(Color.decode("#317773"))
                        .bold()
                        .setForegroundColor(Color.WHITE)
                        .setBorder(DynamicReports.stl.penThin().setLineColor(Color.BLACK)));

        // Configuration when data splits into other pages
        report.setDetailStyle(DynamicReports.stl.style().setRightPadding(5).setLeftPadding(5));
        report.setDetailSplitType(SplitType.PREVENT);
        report.highlightDetailEvenRows();

        // Page Margin
        report.setPageMargin(DynamicReports.margin().setLeft(13).setRight(13).setTop(8).setBottom(12));

        if (data.isEmpty()) {
            report.setWhenNoDataType(WhenNoDataType.ALL_SECTIONS_NO_DETAIL);
        }

        report.setDataSource(data);
        report.setPageFormat(PageType.A4, PageOrientation.PORTRAIT);

        return report;
    }
}
