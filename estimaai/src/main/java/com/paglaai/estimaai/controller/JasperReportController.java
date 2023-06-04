package com.paglaai.estimaai.controller;

import com.paglaai.estimaai.exception.NoExportTypeFoundException;
import com.paglaai.estimaai.service.JasperReportGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@Slf4j
@RequiredArgsConstructor

public class JasperReportController {

    private final JasperReportGenerator jasperReportGenerator;

    @GetMapping("/generate-report")
    public ResponseEntity<byte[]> generateReport(String userStories, String exportType) {
        final var reportFileName = "estimation_report".concat(
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mmssSSS")))
                .concat(".").concat(exportType).toLowerCase();

        ByteArrayOutputStream reportStream = jasperReportGenerator.processReport("table1Data", "pdf");
        byte[] reportBytes = reportStream.toByteArray();

        HttpHeaders headers = new HttpHeaders();

        if("PDF".equalsIgnoreCase(exportType)){
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.attachment().filename(reportFileName).build());
        }else{
            throw  new NoExportTypeFoundException("No export type found");
        }

        return new ResponseEntity<>(reportBytes, headers, HttpStatus.OK);
    }

}


