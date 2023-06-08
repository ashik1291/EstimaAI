package com.paglaai.estimaai.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.paglaai.estimaai.configuration.StartupConfiguration;
import com.paglaai.estimaai.domain.BreakdownData;
import com.paglaai.estimaai.domain.request.MLEstimaBody;
import com.paglaai.estimaai.domain.request.UserStoriesAndTitle;
import com.paglaai.estimaai.domain.response.ReportData;
import com.paglaai.estimaai.exception.DynamicReportException;
import com.paglaai.estimaai.exception.NoExportTypeFoundException;
import com.paglaai.estimaai.feign.MLFeign;
import com.paglaai.estimaai.repository.ReportHistoryRepository;
import com.paglaai.estimaai.repository.UserRepository;
import com.paglaai.estimaai.repository.entity.ReportHistoryEntity;
import lombok.RequiredArgsConstructor;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.dynamicreports.report.constant.*;
import net.sf.dynamicreports.report.exception.DRException;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportHistoryRepository reportHistoryRepository;
    private final UserRepository userRepository;
    private final MLFeign mlFeign;


    public JasperReportBuilder generateReportWrapper(List<ReportData> data, String title){

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
        final TextFieldBuilder<String> header2 = DynamicReports.cmp.text(title).setFixedHeight(12).setStyle(
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


        // Configuration when data splits into other pages
        report.setDetailStyle(DynamicReports.stl.style().setRightPadding(5).setLeftPadding(5));
        report.setDetailSplitType(SplitType.PREVENT);
        report.highlightDetailEvenRows();

        // Page Margin
        report.setPageMargin(DynamicReports.margin().setLeft(15).setRight(10).setTop(5).setBottom(15));


        if (data.isEmpty()) {
            report.setWhenNoDataType(WhenNoDataType.ALL_SECTIONS_NO_DETAIL);
        }

        //report.setDataSource(data);

        for (var singleData : data){
            report.addSummary(DynamicReports.cmp.subreport(subReport(singleData.getBreakdownDataList(), singleData.getTitle())));
        }
        report.setPageFormat(PageType.A4, PageOrientation.PORTRAIT);

        var reportHistoryEntity = new ReportHistoryEntity();
        reportHistoryEntity.setTitle(title.concat(" project estimation"));
        reportHistoryEntity.setGenerationTime(LocalDateTime.now());
        reportHistoryEntity.setJsonData(data);
        reportHistoryEntity.setUsers(userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()));
        reportHistoryRepository.save(reportHistoryEntity);

        return report;
    }

    public JasperReportBuilder subReport(List<BreakdownData> data, String title){

        JasperReportBuilder report = DynamicReports.report();

        // Create space elements
        VerticalListBuilder spaceBefore = DynamicReports.cmp.verticalList().setFixedHeight(20);
        VerticalListBuilder spaceAfter = DynamicReports.cmp.verticalList().setFixedHeight(10);


        // Header 2
        final TextFieldBuilder<String> header2 = DynamicReports.cmp.text(title).setFixedHeight(12).setStyle(
                DynamicReports.stl.style().setFontSize(13).setHorizontalAlignment(HorizontalAlignment.CENTER));

        // Page Header
        report.pageHeader(DynamicReports.cmp.verticalGap(5),
                        DynamicReports.cmp.horizontalFlowList(header2), DynamicReports.cmp.verticalGap(10));

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
//        report.addColumn(
//                DynamicReports.col.column("KLOC", "kloc", DynamicReports.type.stringType())
//                        .setStyle(DynamicReports.stl.style()
//                                .setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE)
//                                .setBottomPadding(5).setLeftPadding(5).setRightPadding(5).setTopPadding(5)
//                                .setBorder(DynamicReports.stl.penThin()))
//                        .setWidth(70));

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
       // report.setPageFormat(PageType.A4, PageOrientation.PORTRAIT);

        return report;
    }

    public JasperReportBuilder generateReport(List<BreakdownData> data, String titleOfProject) {

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
//        report.addColumn(
//                DynamicReports.col.column("KLOC", "kloc", DynamicReports.type.stringType())
//                        .setStyle(DynamicReports.stl.style()
//                                .setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE)
//                                .setBottomPadding(5).setLeftPadding(5).setRightPadding(5).setTopPadding(5)
//                                .setBorder(DynamicReports.stl.penThin()))
//                        .setWidth(70));

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


    public List<ReportData> getProcessedFeatureList(List<UserStoriesAndTitle> userStoriesAndTitles) {
        var reportDataList = new ArrayList<ReportData>();
        for(var userStoryAndTitle: userStoriesAndTitles){
            var reportData = new ReportData();
            reportData.setTitle(userStoryAndTitle.getTitle());
            var MLResponseList =  mlFeign.getFeatures(new MLEstimaBody().setStory(userStoryAndTitle.getUserStory()));
            for(var mlResponse : MLResponseList){
                var breakDownList = new ArrayList<BreakdownData>();
                var subTask = mlResponse.getSubtasks();
                for(var sub : subTask){
                    var breakdownData = new BreakdownData();
                    breakdownData.setFeatureTitle(mlResponse.getFeatureTitle());
                    breakdownData.setSubtasksOfFeatures(sub.getSubtasksOfFeatures());
                    breakdownData.setFeatureIntent(sub.getFeatureIntent());
                    breakdownData.setImplementationTime(sub.getImplementationTime());
                    breakdownData.setComplexity(sub.getComplexity());
                    breakDownList.add(breakdownData);
                }
                reportData.setBreakdownDataList(breakDownList);
            }
            reportDataList.add(reportData);

        }
        return reportDataList;
    }




    /**
     * Below codes are for test purpose
     */
    public ByteArrayOutputStream processReport(String jsonData, String exportType, String title) throws IOException {

        // Output Stream Object
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Prepare and Get Data
        var dataList = getRealData();

        // Get Report (JasperReportBuilder)
        JasperReportBuilder report = generateReport(dataList, title.concat(" project estimation"));

        try {
            if ("PDF".equalsIgnoreCase(exportType)) {
                report.toPdf(outputStream);
            } else if ("XLSX".equalsIgnoreCase(exportType)) {
                report.toXlsx(outputStream);
            } else {
                throw new NoExportTypeFoundException("No export type found.");
            }

        } catch (DRException e) {
            e.printStackTrace();
            throw new DynamicReportException("Error while processing reports");
        }
        var reportHistoryEntity = new ReportHistoryEntity();
        reportHistoryEntity.setTitle(title.concat(" project estimation"));
        reportHistoryEntity.setGenerationTime(LocalDateTime.now());
        reportHistoryEntity.setJsonData(dataList);
        reportHistoryEntity.setUsers(userRepository.findByEmail("ashik.bhuiyan@yopmail.com"));
        reportHistoryRepository.save(reportHistoryEntity);
        return outputStream;

    }

    public List<BreakdownData> getRealData() throws IOException {
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

        // Convert the List of Maps to JSON  AND Map the JSON data to POJO using ObjectMapper
        List<BreakdownData> pojoList = StartupConfiguration.objectMapper().readValue(StartupConfiguration.objectMapper().writeValueAsString(csvData), new TypeReference<>() {
        });

        if (pojoList.isEmpty()) {
            return pojoList;
        }

        List<BreakdownData> modifiedList = new ArrayList<>();
        modifiedList.add(pojoList.get(0));
        String currentFeatureTitle = pojoList.get(0).getFeatureTitle();
        for (int i = 1; i < pojoList.size(); i++) {
            var currentPojo = pojoList.get(i);
            if (currentPojo.getFeatureTitle().equalsIgnoreCase(currentFeatureTitle)) {
                currentPojo.setFeatureTitle(null);
            } else {
                currentFeatureTitle = currentPojo.getFeatureTitle();
            }
            modifiedList.add(currentPojo);
        }
        // Optional: Print the JSON data
        //System.out.println(jsonData);
        return modifiedList;
    }

    public List<BreakdownData> getDataList() {
        List<BreakdownData> data = new ArrayList<>();

        data.add(new BreakdownData()
                .setFeatureTitle("User Authentication")
                .setFeatureIntent("Verify user identity")
                .setSubtasksOfFeatures("User registration")
                .setImplementationTime("4")
                .setComplexity("3"));
        data.add(new BreakdownData()
                .setFeatureTitle("User Authentication")
                .setFeatureIntent("Verify user identity")
                .setSubtasksOfFeatures("Login functionality")
                .setImplementationTime("2")
                .setComplexity("2"));

        return data;
    }
}



