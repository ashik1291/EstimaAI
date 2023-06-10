package com.paglaai.estimaai.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paglaai.estimaai.domain.request.UserStoriesAndTitleRequest;
import com.paglaai.estimaai.domain.response.WrapperReportData;
import com.paglaai.estimaai.exception.NoExportTypeFoundException;
import com.paglaai.estimaai.repository.ReportHistoryRepository;
import com.paglaai.estimaai.service.ReportService;
import com.paglaai.estimaai.util.StringUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.exception.DRException;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor

public class ReportController {

    private final ReportService jasperReportGenerator;
    private final ObjectMapper objectMapper;
    private final ReportHistoryRepository reportHistoryRepository;


    @GetMapping("/generate-report")
    @Operation(hidden = true)
    public ResponseEntity<byte[]> generateReport(String userStories, String exportType, String title) throws IOException {
        final var reportFileName = title.concat("_").concat(
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mmssSSS")))
                .concat(".").concat(exportType).toLowerCase();

        ByteArrayOutputStream reportStream = jasperReportGenerator.processReport("table1Data", exportType, title);
        byte[] reportBytes = reportStream.toByteArray();

        HttpHeaders headers = new HttpHeaders();

        if ("PDF".equalsIgnoreCase(exportType)) {
            headers.setContentType(MediaType.valueOf("application/pdf"));
            headers.setContentDisposition(ContentDisposition.attachment().filename(reportFileName).build());
        } else if ("XLSX".equalsIgnoreCase(exportType)) {
            headers.setContentType(MediaType.valueOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDisposition(ContentDisposition.attachment().filename(reportFileName).build());
        } else {
            throw new NoExportTypeFoundException("No export type found");
        }

        return new ResponseEntity<>(reportBytes, headers, HttpStatus.OK);
    }

    @PostMapping("/process-user-stories")
    public ResponseEntity<WrapperReportData> processStories(@RequestBody List<UserStoriesAndTitleRequest> userStoriesAndTitleRequests) {
        return ResponseEntity.ok(jasperReportGenerator.getProcessedFeatureList(userStoriesAndTitleRequests));
    }

    @PostMapping("generate-report-data")
    @Operation(hidden = true)
    public ResponseEntity<byte[]> generateReportFromJson(@RequestBody WrapperReportData reportDataList, @RequestParam(required = false) String title, @RequestParam String exportType) throws IOException, DRException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        final var reportFileName = title.concat("_").concat(
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mmssSSS")))
                .concat(".").concat(exportType).toLowerCase();

        JasperReportBuilder reportStream = jasperReportGenerator.generateReportWrapper(reportDataList, title);

        HttpHeaders headers = new HttpHeaders();

        if ("PDF".equalsIgnoreCase(exportType)) {
            reportStream.toPdf(outputStream);
            headers.setContentType(MediaType.valueOf("application/pdf"));
            headers.setContentDisposition(ContentDisposition.attachment().filename(reportFileName).build());
        } else if ("XLSX".equalsIgnoreCase(exportType)) {
            reportStream.toXlsx(outputStream);
            headers.setContentType(MediaType.valueOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDisposition(ContentDisposition.attachment().filename(reportFileName).build());
        } else {
            throw new NoExportTypeFoundException("No export type found");
        }

        return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
    }

    @PostMapping("generate-report")
    public ResponseEntity<byte[]> generateReport(@RequestParam long id, @RequestParam(required = false) String title, @RequestParam String exportType) throws IOException, DRException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        final var reportFileName = StringUtil.nullToString(title).concat("_").concat(
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mmssSSS")))
                .concat(".").concat(exportType).toLowerCase();

        var data = reportHistoryRepository.findById(id);
        if(data.isEmpty()){
            throw new RuntimeException("no report history found to generate report");
        }

        JasperReportBuilder reportStream = jasperReportGenerator.generateReportWrapper(data.get().getJsonData(), StringUtil.nullToString(title));

        HttpHeaders headers = new HttpHeaders();

        if ("PDF".equalsIgnoreCase(exportType)) {
            reportStream.toPdf(outputStream);
            headers.setContentType(MediaType.valueOf("application/pdf"));
            headers.setContentDisposition(ContentDisposition.attachment().filename(reportFileName).build());
        } else if ("XLSX".equalsIgnoreCase(exportType)) {
            reportStream.toXlsx(outputStream);
            headers.setContentType(MediaType.valueOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDisposition(ContentDisposition.attachment().filename(reportFileName).build());
        } else {
            throw new NoExportTypeFoundException("No export type found");
        }

        return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
    }
}


