package com.paglaai.estimaai.service;

import com.paglaai.estimaai.domain.dto.ReportData;
import net.sf.dynamicreports.jasper.builder.JasperConcatenatedReportBuilder;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;
import net.sf.dynamicreports.report.constant.*;
import net.sf.dynamicreports.report.exception.DRException;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.List;
import java.util.Objects;

@Service
public class JasperReportGenerator {

    public ByteArrayOutputStream  make(List<ReportData> table1Data, List<ReportData> table2Data){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            JasperReportBuilder report1 = generateReport(table1Data); // Create the first report builder
            JasperReportBuilder report2 = generateReport2(table2Data); // Create the second report builder

            // Create a JasperConcatenatedReportBuilder
            JasperConcatenatedReportBuilder concatenatedReport = DynamicReports.concatenatedReport();

            // Add the individual reports to the concatenated report
            concatenatedReport.concatenate(report1);
            //concatenatedReport.concatenate(report2);

            // Generate and export the concatenated report to PDF
            concatenatedReport.toPdf(outputStream);

        } catch (DRException e) {
            // Handle any exceptions
            e.printStackTrace();
        }

        return outputStream;
    }

    public JasperReportBuilder generateReport(List<ReportData> data) {
        JasperReportBuilder report = DynamicReports.report();

        final String headerText2 = Objects.requireNonNullElse("ESTIMA AI", "ESTIMA AI Report");
        final TextFieldBuilder<String> header1 = DynamicReports.cmp.text("Paglaa AI Inc. ").setFixedHeight(12)
                .setStyle(
                        DynamicReports.stl.style().setFontSize(14).setHorizontalAlignment(HorizontalAlignment.CENTER));
        final TextFieldBuilder<String> header2 = DynamicReports.cmp.text(headerText2).setFixedHeight(12).setStyle(
                DynamicReports.stl.style().setFontSize(13).setHorizontalAlignment(HorizontalAlignment.CENTER));

        final TextFieldBuilder<String> footer1 = DynamicReports.cmp.text("Notes:").setFixedHeight(12)
                .setStyle(DynamicReports.stl.style().setFontSize(12).setBold(true)
                        .setHorizontalAlignment(HorizontalAlignment.LEFT));
        final TextFieldBuilder<String> footer2 = DynamicReports.cmp.text("1. This report is provided by EstimaAI.")
                .setFixedHeight(12)
                .setStyle(DynamicReports.stl.style().setFontSize(9).setHorizontalAlignment(HorizontalAlignment.LEFT));
        report.pageHeader(DynamicReports.cmp.verticalGap(5),
                DynamicReports.cmp.verticalList(DynamicReports.cmp.verticalList(header1).setGap(5),
                        DynamicReports.cmp.horizontalFlowList(header2), DynamicReports.cmp.verticalGap(10)));

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
        report.setPageMargin(DynamicReports.margin().setLeft(5).setRight(20).setTop(5).setBottom(15));
        report.highlightDetailEvenRows();

        report.pageFooter(DynamicReports.cmp.verticalGap(10),
                DynamicReports.cmp.verticalList(DynamicReports.cmp.verticalList(footer1).setGap(10),
                        DynamicReports.cmp.horizontalFlowList(footer2)),
                DynamicReports.cmp.pageXofY());

        if (data.isEmpty()) {
            report.setWhenNoDataType(WhenNoDataType.ALL_SECTIONS_NO_DETAIL);
        }

        report.setDataSource(data);
        report.addSummary(DynamicReports.cmp.subreport(generateReport2(data)));
        report.addSummary(DynamicReports.cmp.subreport(generateReport2(data)));
        //report.setPageFormat(PageType.A4, PageOrientation.PORTRAIT);

        return report;
    }

    public JasperReportBuilder generateReport2(List<ReportData> data) {
        JasperReportBuilder report = DynamicReports.report();

        final String headerText2 = Objects.requireNonNullElse("ESTIMA AI", "ESTIMA AI Report");
//        final TextFieldBuilder<String> header1 = DynamicReports.cmp.text("Paglaa AI Inc. ").setFixedHeight(12)
//                .setStyle(
//                        DynamicReports.stl.style().setFontSize(14).setHorizontalAlignment(HorizontalAlignment.CENTER));
        final TextFieldBuilder<String> header2 = DynamicReports.cmp.text(headerText2).setFixedHeight(12).setStyle(
                DynamicReports.stl.style().setFontSize(13).setHorizontalAlignment(HorizontalAlignment.CENTER));

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

        report.setColumnHeaderStyle(DynamicReports.stl.style().setRightPadding(5).setLeftPadding(5)
                .setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE));
        report.setColumnTitleStyle(DynamicReports.stl.style(DynamicReports.stl.style().bold())
                .setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE)
                .setBorder(DynamicReports.stl.pen1Point()).setLeftPadding(5).setRightPadding(5)
                .setBackgroundColor(Color.GRAY));
        report.setDetailStyle(DynamicReports.stl.style().setRightPadding(5).setLeftPadding(5));
        report.setDetailSplitType(SplitType.PREVENT);
        report.setPageMargin(DynamicReports.margin().setLeft(5).setRight(20).setTop(5).setBottom(15));
        report.highlightDetailEvenRows();


        if (data.isEmpty()) {
            report.setWhenNoDataType(WhenNoDataType.ALL_SECTIONS_NO_DETAIL);
        }

        report.setDataSource(data);
        //report.setPageFormat(PageType.A4, PageOrientation.PORTRAIT);

        return report;
    }
}



