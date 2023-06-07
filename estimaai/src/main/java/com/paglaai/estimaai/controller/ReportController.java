package com.paglaai.estimaai.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.paglaai.estimaai.domain.dto.ReportData;
import com.paglaai.estimaai.exception.NoExportTypeFoundException;
import com.paglaai.estimaai.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.exception.DRException;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor

public class ReportController {

    private final ReportService jasperReportGenerator;
    private final ObjectMapper objectMapper;


    @GetMapping("/generate-report")
    @Operation(hidden = true)
    public ResponseEntity<byte[]> generateReport(String userStories, String exportType, String title) throws IOException {
        final var reportFileName = title.concat("_").concat(
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mmssSSS")))
                .concat(".").concat(exportType).toLowerCase();

        ByteArrayOutputStream reportStream = jasperReportGenerator.processReport("table1Data", exportType, title);
        byte[] reportBytes = reportStream.toByteArray();

        HttpHeaders headers = new HttpHeaders();

        if("PDF".equalsIgnoreCase(exportType)){
            headers.setContentType(MediaType.valueOf("application/pdf"));
            headers.setContentDisposition(ContentDisposition.attachment().filename(reportFileName).build());
        } else if ("XLSX".equalsIgnoreCase(exportType)) {
            headers.setContentType(MediaType.valueOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDisposition(ContentDisposition.attachment().filename(reportFileName).build());
        } else{
            throw  new NoExportTypeFoundException("No export type found");
        }

        return new ResponseEntity<>(reportBytes, headers, HttpStatus.OK);
    }

    @PostMapping("/process-user-stories")
    public ResponseEntity<List<ReportData>> processStories(String userStories){
        return ResponseEntity.ok(List.of(new ReportData()));
    }

    @PostMapping("generate-report-from-json")
    public ResponseEntity<byte[]> generateReportFromJson(@RequestBody List<ReportData> reportDataList, @RequestParam String title, @RequestParam String exportType ) throws IOException, DRException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //byte[] reportBytes = new byte[]{};//reportStream.toByteArray();

        final var reportFileName = title.concat("_").concat(
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mmssSSS")))
                .concat(".").concat(exportType).toLowerCase();

//        List<ReportData> pojoList = objectMapper.readValue(jsonData, new TypeReference<>() {
//        });

        JasperReportBuilder reportStream = jasperReportGenerator.generateReport(reportDataList, title);

        HttpHeaders headers = new HttpHeaders();

        if("PDF".equalsIgnoreCase(exportType)){
            reportStream.toPdf(outputStream);
            headers.setContentType(MediaType.valueOf("application/pdf"));
            headers.setContentDisposition(ContentDisposition.attachment().filename(reportFileName).build());
        } else if ("XLSX".equalsIgnoreCase(exportType)) {
            reportStream.toXlsx(outputStream);
            headers.setContentType(MediaType.valueOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDisposition(ContentDisposition.attachment().filename(reportFileName).build());
        } else{
            throw  new NoExportTypeFoundException("No export type found");
        }

        return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
    }
}


