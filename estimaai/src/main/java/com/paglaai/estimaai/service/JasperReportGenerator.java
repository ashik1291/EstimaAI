package com.paglaai.estimaai.service;

import com.paglaai.estimaai.domain.dto.ReportData;
import com.paglaai.estimaai.exception.DynamicReportException;
import com.paglaai.estimaai.exception.NoExportTypeFoundException;
import net.sf.dynamicreports.jasper.builder.JasperConcatenatedReportBuilder;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.*;
import net.sf.dynamicreports.report.exception.DRException;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class JasperReportGenerator {

    public ByteArrayOutputStream processReport(String userStories, String exportType){

        // Output Stream Object
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Prepare and Get Data
       var dataList = getDataList();

        // Get Report (JasperReportBuilder)
        JasperReportBuilder report1 = generateReport(dataList); // Create the first report builder
        //JasperReportBuilder report2 = generateReport2(); // Create the second report builder

        try {
            // Create a JasperConcatenatedReportBuilder
            JasperConcatenatedReportBuilder concatenatedReport = DynamicReports.concatenatedReport();

            // Add the individual reports to the concatenated report
            concatenatedReport.concatenate(report1);
            //concatenatedReport.concatenate(report2);

            if("PDF".equalsIgnoreCase(exportType)){
                concatenatedReport.toPdf(outputStream);  // Generate and export the concatenated report to PDF
            }else if("XLSX".equalsIgnoreCase(exportType)){
                concatenatedReport.toXlsx(outputStream); // Generate and export the concatenated report to XLSX
            }else{
                throw new NoExportTypeFoundException("No export type found.");
            }


        } catch (DRException e) {
            e.printStackTrace();
            throw new DynamicReportException("Error while processing reports");
        }

        return outputStream;

    }

    public JasperReportBuilder generateReport(List<ReportData> data) {

        JasperReportBuilder report = DynamicReports.report();

        // Create space elements
        VerticalListBuilder spaceBefore = DynamicReports.cmp.verticalList().setFixedHeight(20);
        VerticalListBuilder spaceAfter = DynamicReports.cmp.verticalList().setFixedHeight(10);

        // Header 1
        final TextFieldBuilder<String> header1 = DynamicReports.cmp.text("Paglaa AI Inc. ").setFixedHeight(12)
                .setStyle(
                        DynamicReports.stl.style().setBold(true).setFontSize(16).setHorizontalAlignment(HorizontalAlignment.CENTER)
                                .setForegroundColor(Color.WHITE).setBackgroundColor(Color.decode("#317773")));
        // Header 2
        final String headerText2 = "Table 1";
        final TextFieldBuilder<String> header2 = DynamicReports.cmp.text(headerText2).setFixedHeight(12).setStyle(
                DynamicReports.stl.style().setFontSize(13).setHorizontalAlignment(HorizontalAlignment.CENTER));

        // Footer 1
        final TextFieldBuilder<String> footer1 = DynamicReports.cmp.text("Notes:").setFixedHeight(12)
                .setStyle(DynamicReports.stl.style().setFontSize(12).setBold(true)
                        .setHorizontalAlignment(HorizontalAlignment.LEFT));
        // Footer 2
        final TextFieldBuilder<String> footer2 = DynamicReports.cmp.text("1. This report is provided by EstimaAI.")
                .setFixedHeight(12)
                .setStyle(DynamicReports.stl.style().setFontSize(9).setHorizontalAlignment(HorizontalAlignment.LEFT));

        // Page Header
        report.pageHeader(DynamicReports.cmp.verticalGap(5),
                DynamicReports.cmp.verticalList(DynamicReports.cmp.verticalList(header1).setGap(5).add(spaceAfter),
                        DynamicReports.cmp.horizontalFlowList(header2), DynamicReports.cmp.verticalGap(10)));



        // Columns
        report.addColumn(
                DynamicReports.col.column("Feature Title", "featureTitle", DynamicReports.type.stringType())
                        .setStyle(DynamicReports.stl.style()
                                .setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE)
                                .setBottomPadding(5).setLeftPadding(5).setRightPadding(5).setTopPadding(5)
                                .setBorder(DynamicReports.stl.penThin()))
                        .setWidth(90));

        report.addColumn(
                DynamicReports.col.column("Feature Intent", "featureIntent", DynamicReports.type.stringType())
                        .setStyle(DynamicReports.stl.style()
                                .setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE)
                                .setBottomPadding(5).setLeftPadding(5).setRightPadding(5).setTopPadding(5)
                                .setBorder(DynamicReports.stl.penThin()))
                        .setWidth(120));

        report.addColumn(
                DynamicReports.col.column("Subtasks of Features", "subtasksOfFeatures", DynamicReports.type.stringType())
                        .setStyle(DynamicReports.stl.style()
                                .setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE)
                                .setBottomPadding(5).setLeftPadding(5).setRightPadding(5).setTopPadding(5)
                                .setBorder(DynamicReports.stl.penThin()))
                        .setWidth(120));

        report.addColumn(
                DynamicReports.col.column("Implementation Time (hours)", "implementationTime", DynamicReports.type.stringType())
                        .setStyle(DynamicReports.stl.style()
                                .setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE)
                                .setBottomPadding(5).setLeftPadding(5).setRightPadding(5).setTopPadding(5)
                                .setBorder(DynamicReports.stl.penThin()))
                        .setWidth(120));

        report.addColumn(
                DynamicReports.col.column("Complexity (1-5)", "complexity", DynamicReports.type.stringType())
                        .setStyle(DynamicReports.stl.style()
                                .setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE)
                                .setBottomPadding(5).setLeftPadding(5).setRightPadding(5).setTopPadding(5)
                                .setBorder(DynamicReports.stl.penThin()))
                        .setWidth(120));

        // Column Header
        report.setColumnHeaderStyle(DynamicReports.stl.style().setRightPadding(5).setLeftPadding(5)
                .setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE));
        report.setColumnTitleStyle(DynamicReports.stl.style(DynamicReports.stl.style().bold())
                .setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE)
                .setBorder(DynamicReports.stl.pen1Point()).setLeftPadding(5).setRightPadding(5)
                .setBackgroundColor(Color.decode("#E2D1F9")));
        report.setDetailStyle(DynamicReports.stl.style().setRightPadding(5).setLeftPadding(5));
        report.setDetailSplitType(SplitType.PREVENT);
        report.setPageMargin(DynamicReports.margin().setLeft(5).setRight(20).setTop(5).setBottom(15));
        report.highlightDetailEvenRows();


        if (data.isEmpty()) {
            report.setWhenNoDataType(WhenNoDataType.ALL_SECTIONS_NO_DETAIL);
        }

        report.setDataSource(data);
        report.addSummary(DynamicReports.cmp.subreport(subReportOne(data)));
        report.setPageFormat(PageType.A4, PageOrientation.PORTRAIT);

        return report;
    }

    public JasperReportBuilder subReportOne(List<ReportData> data) {
        JasperReportBuilder report = DynamicReports.report();

        final String headerText2 = "Table 2";
        final TextFieldBuilder<String> header2 = DynamicReports.cmp.text(headerText2).setFixedHeight(12).setStyle(
                DynamicReports.stl.style().setFontSize(13).setHorizontalAlignment(HorizontalAlignment.CENTER).setFirstLineIndent(5));

        // Create style for the table name
        StyleBuilder style = DynamicReports.stl.style()
                .setFontSize(16)
                .setBold(false)
                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);

        // Create space elements
        VerticalListBuilder spaceBefore = DynamicReports.cmp.verticalList().setFixedHeight(20);
        VerticalListBuilder spaceAfter = DynamicReports.cmp.verticalList().setFixedHeight(10);

        // Add the space elements before and after the table name
        VerticalListBuilder title = DynamicReports.cmp.verticalList()
                .add(spaceBefore)
                .add(DynamicReports.cmp.text(headerText2).setStyle(style))
                .add(spaceAfter);

        // Add the table name to the report
        report.addTitle(title);

        report.addColumn(
                DynamicReports.col.column("Feature Title", "featureTitle", DynamicReports.type.stringType())
                        .setStyle(DynamicReports.stl.style()
                                .setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE)
                                .setBottomPadding(5).setLeftPadding(5).setRightPadding(5).setTopPadding(5)
                                .setBorder(DynamicReports.stl.penThin()))
                        .setWidth(90));

        report.addColumn(
                DynamicReports.col.column("Feature Intent", "featureIntent", DynamicReports.type.stringType())
                        .setStyle(DynamicReports.stl.style()
                                .setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE)
                                .setBottomPadding(5).setLeftPadding(5).setRightPadding(5).setTopPadding(5)
                                .setBorder(DynamicReports.stl.penThin()))
                        .setWidth(120));

        report.addColumn(
                DynamicReports.col.column("Subtasks of Features", "subtasksOfFeatures", DynamicReports.type.stringType())
                        .setStyle(DynamicReports.stl.style()
                                .setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE)
                                .setBottomPadding(5).setLeftPadding(5).setRightPadding(5).setTopPadding(5)
                                .setBorder(DynamicReports.stl.penThin()))
                        .setWidth(120));

        report.addColumn(
                DynamicReports.col.column("Implementation Time (hours)", "implementationTime", DynamicReports.type.stringType())
                        .setStyle(DynamicReports.stl.style()
                                .setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE)
                                .setBottomPadding(5).setLeftPadding(5).setRightPadding(5).setTopPadding(5)
                                .setBorder(DynamicReports.stl.penThin()))
                        .setWidth(120));

        report.addColumn(
                DynamicReports.col.column("Complexity (1-5)", "complexity", DynamicReports.type.stringType())
                        .setStyle(DynamicReports.stl.style()
                                .setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE)
                                .setBottomPadding(5).setLeftPadding(5).setRightPadding(5).setTopPadding(5)
                                .setBorder(DynamicReports.stl.penThin()))
                        .setWidth(120));

        report.setColumnHeaderStyle(DynamicReports.stl.style().setRightPadding(5).setLeftPadding(5)
                .setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE));
        report.setColumnTitleStyle(DynamicReports.stl.style(DynamicReports.stl.style().bold())
                .setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE)
                .setBorder(DynamicReports.stl.pen1Point()).setLeftPadding(5).setRightPadding(5)
                .setBackgroundColor(Color.GRAY));
        report.setDetailStyle(DynamicReports.stl.style().setRightPadding(5).setLeftPadding(5));
        report.setDetailSplitType(SplitType.PREVENT);
        report.highlightDetailEvenRows();


        if (data.isEmpty()) {
            report.setWhenNoDataType(WhenNoDataType.ALL_SECTIONS_NO_DETAIL);
        }

        report.setDataSource(data);

        return report;
    }

    public List<ReportData> getDataList(){
        List<ReportData> data = new ArrayList<>();

        data.add(new ReportData()
                .setFeatureTitle("User Authentication")
                .setFeatureIntent("Verify user identity")
                .setSubtasksOfFeatures("User registration")
                .setImplementationTime("4")
                .setComplexity("3"));
        data.add(new ReportData()
                .setFeatureTitle("User Authentication")
                .setFeatureIntent("Verify user identity")
                .setSubtasksOfFeatures("Login functionality")
                .setImplementationTime("2")
                .setComplexity("2"));

        return data;
    }
}



