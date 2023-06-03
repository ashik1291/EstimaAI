package com.paglaai.estimaai.controller;

import com.paglaai.estimaai.domain.dto.ReportData;
import com.paglaai.estimaai.service.JasperReportGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class JasperReportController {

    private final JasperReportGenerator jasperReportGenerator;

    @GetMapping("/generate-report")
    public ResponseEntity<byte[]> generateReport() {

        List<ReportData> table1Data = new ArrayList<>();
        table1Data.add(new ReportData()
                .setFeatureTitle("User Authentication")
                .setFeatureIntent("Verify user identity")
                .setSubtasksOfFeatures("User registration")
                .setImplementationTime("4")
                .setComplexity("3"));
        table1Data.add(new ReportData()
                .setFeatureTitle("User Authentication")
                .setFeatureIntent("Verify user identity")
                .setSubtasksOfFeatures("Login functionality")
                .setImplementationTime("2")
                .setComplexity("2"));

        ByteArrayOutputStream reportStream = jasperReportGenerator.make(table1Data, table1Data);
        byte[] reportBytes = reportStream.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment().filename("report.pdf").build());

        return new ResponseEntity<>(reportBytes, headers, HttpStatus.OK);
    }

}


