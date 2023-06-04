package com.paglaai.estimaai.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.paglaai.estimaai.domain.dto.ReportData;
import com.paglaai.estimaai.exception.NoExportTypeFoundException;
import com.paglaai.estimaai.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
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

    @GetMapping("/generate-report")
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

    @PostMapping("/csv-to-json")
    public List<ReportData> convertCsvToJson() throws IOException {
        // Load the CSV file from the resources folder
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

}


