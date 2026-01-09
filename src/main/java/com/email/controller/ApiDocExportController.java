package com.email.controller;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.email.handler.ApiInfo;
import com.email.serviceImpl.ApiExcelExporter;
import com.email.serviceImpl.ApiExtractionService;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class ApiDocExportController {

    private static final Logger logger = LoggerFactory.getLogger(ApiDocExportController.class);

	@Autowired
    private ApiExtractionService extractionService;

    @Autowired
    private ApiExcelExporter excelExporter;

    @GetMapping("/export-excel")
    public ResponseEntity<byte[]> export() throws Exception {
        logger.info("API documentation export request received");
        try {
            List<ApiInfo> apis = extractionService.extractAllApis();
            logger.info("Extracted {} APIs for documentation", apis.size());

            Workbook workbook = excelExporter.generateExcel(apis);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);

            logger.info("API documentation Excel generated successfully");
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=all-rest-apis.xlsx")
                    .body(out.toByteArray());
        } catch (Exception e) {
            logger.error("Error generating API documentation Excel", e);
            throw e;
        }
    }
}
