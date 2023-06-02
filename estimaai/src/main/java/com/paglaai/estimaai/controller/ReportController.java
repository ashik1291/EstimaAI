package com.paglaai.estimaai.controller;

import com.paglaai.estimaai.domain.dto.ReportData;
import com.paglaai.estimaai.service.JasperReportService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/report")
public class ReportController {
    private final JasperReportService jasperReportService;
    //private final TestReportService testReportService;

    @GetMapping
    public void generateReport(HttpServletResponse response, @RequestParam String exportType) {
        OutputStream out = null;

        final var reportFileName = "estimation_report".concat(
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mmssSSS")))
                .concat(".").concat(exportType).toLowerCase();;
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=".concat(reportFileName));


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
                .setComplexity("2"));

        try {
            final JasperReportBuilder reportBuilderResponse = jasperReportService.generateReport(data);
            out = response.getOutputStream();
            if (exportType.equalsIgnoreCase("PDF")) {
                response.setContentType("application/pdf");
                reportBuilderResponse.toPdf(out);
            } else {
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                reportBuilderResponse.toXlsx(out);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(response.SC_INTERNAL_SERVER_ERROR);
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                }
            } catch (IOException e) {
                log.error("IOException in listTransactionReportForAdmin", e);
            }
        }
    }
}

