package com.micheladas.chelas.export;

import java.io.IOException;
import java.util.List;
import java.util.function.BiConsumer;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

public class GenericExcelExporter<T> {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<T> dataList;
    private String[] headers;
    private String sheetName;

    public GenericExcelExporter(List<T> dataList, String sheetName, String[] headers) {
        this.dataList = dataList;
        this.sheetName = sheetName;
        this.headers = headers;
        this.workbook = new XSSFWorkbook();
        this.sheet = workbook.createSheet(sheetName);
    }

    public CellStyle getHeaderStyle() {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);

        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);
        return style;
    }

    public CellStyle getCurrencyStyle() {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        DataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("$0.00"));
        return style;
    }

    public void export(HttpServletResponse response, BiConsumer<Row, T> mapeadorFilas) throws IOException {

        Row headerRow = sheet.createRow(0);
        CellStyle headerStyle = getHeaderStyle();
        for (int i = 0; i < headers.length; i++) {
            createCell(headerRow, i, headers[i], headerStyle);
        }

        int rowIndex = 1;
        for (T element : dataList) {
            Row row = sheet.createRow(rowIndex++);
            mapeadorFilas.accept(row, element);
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ServletOutputStream out = response.getOutputStream();
        workbook.write(out);
        workbook.close();
        out.close();
    }

    public void createCell(Row row, int col, Object valor, CellStyle style) {
        Cell cell = row.createCell(col);
        if (valor instanceof Number) {
            cell.setCellValue(((Number) valor).doubleValue());
        } else {
            cell.setCellValue(valor != null ? valor.toString() : "");
        }
        cell.setCellStyle(style);
    }

    public CellStyle getNormalStyle() {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }
}