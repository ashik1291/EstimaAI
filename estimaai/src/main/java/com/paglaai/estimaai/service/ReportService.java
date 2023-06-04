package com.paglaai.estimaai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.paglaai.estimaai.domain.dto.ReportData;
import com.paglaai.estimaai.exception.DynamicReportException;
import com.paglaai.estimaai.exception.NoExportTypeFoundException;
import net.sf.dynamicreports.jasper.builder.JasperConcatenatedReportBuilder;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.dynamicreports.report.constant.*;
import net.sf.dynamicreports.report.exception.DRException;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    public ByteArrayOutputStream processReport(String userStories, String exportType, String title) throws IOException {

        // Output Stream Object
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Prepare and Get Data
       var dataList = getRealData();

        // Get Report (JasperReportBuilder)
        JasperReportBuilder report = generateReport(dataList, title.concat(" project estimation"));

        try {
            if("PDF".equalsIgnoreCase(exportType)){
                report.toPdf(outputStream);
            }else if("XLSX".equalsIgnoreCase(exportType)){
                report.toXlsx(outputStream);
            }else{
                throw new NoExportTypeFoundException("No export type found.");
            }

        } catch (DRException e) {
            e.printStackTrace();
            throw new DynamicReportException("Error while processing reports");
        }

        return outputStream;

    }

    public JasperReportBuilder generateReport(List<ReportData> data, String titleOfProject) {

        JasperReportBuilder report = DynamicReports.report();

        // Create space elements
        VerticalListBuilder spaceBefore = DynamicReports.cmp.verticalList().setFixedHeight(20);
        VerticalListBuilder spaceAfter = DynamicReports.cmp.verticalList().setFixedHeight(10);

        // Header 1
        final TextFieldBuilder<String> header1 = DynamicReports.cmp.text("Paglaa AI Inc. ").setFixedHeight(12)
                .setStyle(
                        DynamicReports.stl.style().setBold(true).setFontSize(16).setHorizontalAlignment(HorizontalAlignment.CENTER)
                                .setForegroundColor(Color.BLACK));
        // Header 2
        final TextFieldBuilder<String> header2 = DynamicReports.cmp.text(titleOfProject).setFixedHeight(12).setStyle(
                DynamicReports.stl.style().setFontSize(13).setHorizontalAlignment(HorizontalAlignment.CENTER));

        // Page Header
        report.pageHeader(DynamicReports.cmp.verticalGap(5),
                DynamicReports.cmp.verticalList(DynamicReports.cmp.verticalList(header1).setGap(5).add(spaceAfter),
                        DynamicReports.cmp.horizontalFlowList(header2), DynamicReports.cmp.verticalGap(10)));

        // Footer 1
        final TextFieldBuilder<String> footer1 = DynamicReports.cmp.text("Notes:").setFixedHeight(12)
                .setStyle(DynamicReports.stl.style().setFontSize(12).setBold(true)
                        .setHorizontalAlignment(HorizontalAlignment.LEFT));
        // Footer 2
        final TextFieldBuilder<String> footer2 = DynamicReports.cmp.text("* This report is provided by EstimaAI.")
                .setFixedHeight(12)
                .setStyle(DynamicReports.stl.style().setFontSize(9).setHorizontalAlignment(HorizontalAlignment.LEFT));

        // Page Footer
        report.pageFooter(DynamicReports.cmp.verticalGap(10),
                DynamicReports.cmp.verticalList(DynamicReports.cmp.verticalList(footer1).setGap(10),
                        DynamicReports.cmp.horizontalFlowList(footer2)),
                DynamicReports.cmp.pageXofY());

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
                DynamicReports.col.column("Duration (hours)", "implementationTime", DynamicReports.type.stringType())
                        .setStyle(DynamicReports.stl.style()
                                .setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE)
                                .setBottomPadding(5).setLeftPadding(5).setRightPadding(5).setTopPadding(5)
                                .setBorder(DynamicReports.stl.penThin()))
                        .setWidth(80));

        report.addColumn(
                DynamicReports.col.column("Complexity (1-5)", "complexity", DynamicReports.type.stringType())
                        .setStyle(DynamicReports.stl.style()
                                .setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE)
                                .setBottomPadding(5).setLeftPadding(5).setRightPadding(5).setTopPadding(5)
                                .setBorder(DynamicReports.stl.penThin()))
                        .setWidth(90));
        report.addColumn(
                DynamicReports.col.column("KLOC", "kloc", DynamicReports.type.stringType())
                        .setStyle(DynamicReports.stl.style()
                                .setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE)
                                .setBottomPadding(5).setLeftPadding(5).setRightPadding(5).setTopPadding(5)
                                .setBorder(DynamicReports.stl.penThin()))
                        .setWidth(70));

        // Column Header
        report.setColumnHeaderStyle(DynamicReports.stl.style().setRightPadding(5).setLeftPadding(5)
                .setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE));
        report.setColumnTitleStyle(DynamicReports.stl.style(DynamicReports.stl.style().bold())
                .setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE)
                .setBorder(DynamicReports.stl.pen1Point()).setLeftPadding(5).setRightPadding(5)
                        .setBottomPadding(5).setTopPadding(5)
                .setBackgroundColor(Color.decode("#317773")).bold().setForegroundColor(Color.WHITE).setBorder(DynamicReports.stl.penThin().setLineColor(Color.BLACK)));

        // Configuration when data splits into other pages
        report.setDetailStyle(DynamicReports.stl.style().setRightPadding(5).setLeftPadding(5));
        report.setDetailSplitType(SplitType.PREVENT);
        report.highlightDetailEvenRows();

        // Page Margin
        report.setPageMargin(DynamicReports.margin().setLeft(15).setRight(10).setTop(5).setBottom(15));


        if (data.isEmpty()) {
            report.setWhenNoDataType(WhenNoDataType.ALL_SECTIONS_NO_DETAIL);
        }

        report.setDataSource(data);
        report.setPageFormat(PageType.A4, PageOrientation.PORTRAIT);

        return report;
    }

    public List<ReportData> getRealData() throws IOException {
        ClassPathResource csvFileResource = new ClassPathResource("input_feature.csv");
        File csvFile = csvFileResource.getFile();

        // Create a CsvMapper and configure it
        CsvMapper csvMapper = new CsvMapper();
        CsvSchema csvSchema = CsvSchema.emptySchema().withHeader();

        // Read the CSV file into a List of Maps
        List<Object> csvData = csvMapper.readerFor(Map.class)
                .with(csvSchema)
                .readValues(csvFile)
                .readAll();

        // Convert the List of Maps to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        String jsonData = objectMapper.writeValueAsString(csvData);

        // Map the JSON data to POJO using ObjectMapper
        List<ReportData> pojoList = objectMapper.readValue(jsonData, new TypeReference<>() {
        });

        if(pojoList.isEmpty()){
            return pojoList;
        }

        List<ReportData> modifiedList = new ArrayList<>();
        modifiedList.add(pojoList.get(0));
        String currentFeatureTitle =  pojoList.get(0).getFeatureTitle();
        for(int i = 1; i< pojoList.size(); i++){
            var currentPojo = pojoList.get(i);
            if(currentPojo.getFeatureTitle().equalsIgnoreCase(currentFeatureTitle)){
                currentPojo.setFeatureTitle(null);
            }else{
                currentFeatureTitle = currentPojo.getFeatureTitle();
            }
            modifiedList.add(currentPojo);
        }



        // Optional: Print the JSON data
        System.out.println(jsonData);
        return modifiedList;
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



