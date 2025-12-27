package com.email.serviceImpl;

import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.email.handler.ApiInfo;

@Service
public class ApiExcelExporter {

	public Workbook generateExcel(List<ApiInfo> apis) {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("All_APIs");

        // Header
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Controller");
        header.createCell(1).setCellValue("HTTP Method");
        header.createCell(2).setCellValue("URL");
        header.createCell(3).setCellValue("Request Body");

        int rowNum = 1;
        for (ApiInfo api : apis) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(api.getController());
            row.createCell(1).setCellValue(api.getHttpMethod());
            row.createCell(2).setCellValue(api.getUrl());
            row.createCell(3).setCellValue(api.getRequestBody());
        }

        for (int i = 0; i < 4; i++) {
            sheet.autoSizeColumn(i);
        }

        return workbook;
    }
}
