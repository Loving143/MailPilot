package com.email.controller;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.email.handler.ApiInfo;
import com.email.serviceImpl.ApiExcelExporter;
import com.email.serviceImpl.ApiExtractionService;

@RestController
public class ApiDocExportController {

	@Autowired
    private ApiExtractionService extractionService;

    @Autowired
    private ApiExcelExporter excelExporter;

    @GetMapping("/export-excel")
    public ResponseEntity<byte[]> export() throws Exception {

        List<ApiInfo> apis =
                extractionService.extractAllApis();

        Workbook workbook =
                excelExporter.generateExcel(apis);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=all-rest-apis.xlsx")
                .body(out.toByteArray());
    }
}
