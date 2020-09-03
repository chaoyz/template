package cn.idocode.template.singleserver.utils;

import cn.idocode.template.singleserver.model.CustemExcelDto;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author levicyang
 * 2020-08-08 15:48
 */
public class ExcelUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelUtil.class);

    public static List<CustemExcelDto> parseExcelData(InputStream is, String fileName) throws IOException {
        List<CustemExcelDto> result = new LinkedList<>();
        if (is == null || StringUtils.isEmpty(fileName)) {
            return result;
        }
        Workbook workbook;
        if (fileName.endsWith(".xls")) {
            workbook = new HSSFWorkbook(is);
        } else if (fileName.endsWith(".xlsx")) {
            workbook = new XSSFWorkbook(is);
        } else {
            LOGGER.error("ExcelUtil parseExcelData file name suffix error. fileName:{}", fileName);
            return result;
        }
        int sheetCount = workbook.getNumberOfSheets();
        for (int i = 0; i < sheetCount; i++) {
            Sheet sheet = workbook.getSheetAt(i);
            CustemExcelDto custemExcelDto = new CustemExcelDto();
            custemExcelDto.setSheetName(sheet.getSheetName());
            List<List<Object>> rowDatas = new LinkedList<>();
            List<String> title = new LinkedList<>();
            for (Row row : sheet) {
                List<Object> rowData = new LinkedList<>();
                int rowNum = row.getRowNum();
                if (rowNum == 0) {
                    // 第一行默认为标题
                    int len = row.getPhysicalNumberOfCells();
                    for (int j = 0; j < len; j++) {
                        Cell cell = row.getCell(j);
                        switch (cell.getCellType()) {
                            case STRING:
                                title.add(cell.getStringCellValue());
                                break;
                            case FORMULA:
                                title.add(cell.getCellFormula());
                                break;
                            case NUMERIC:
                            case BOOLEAN:
                            case _NONE:
                            case BLANK:
                            case ERROR:
                                title.add(null);
                                break;
                        }
                    }
                } else {
                    int len = row.getPhysicalNumberOfCells();
                    for (int j = 0; j < len; j++) {
                        Cell cell = row.getCell(j);
                        List<Class<?>> rowType = new LinkedList<>();
                        switch (cell.getCellType()) {
                            case STRING:
                                rowType.add(String.class);
                                rowData.add(cell.getStringCellValue());
                                break;
                            case NUMERIC:
                                rowType.add(Double.class);
                                rowData.add(cell.getNumericCellValue());
                                break;
                            case BOOLEAN:
                                rowType.add(Boolean.class);
                                rowData.add(cell.getBooleanCellValue());
                                break;
                            case FORMULA:
                                rowType.add(String.class);
                                rowData.add(cell.getCellFormula());
                                break;
                            case _NONE:
                            case BLANK:
                            case ERROR:
                                rowType.add(null);
                                rowData.add(null);
                        }
                        custemExcelDto.setRowType(rowType);
                    }
                    rowDatas.add(rowData);
                }
            }
            custemExcelDto.setRows(rowDatas);
            custemExcelDto.setTitles(title);
            result.add(custemExcelDto);
        }
        return result;
    }

    public static void downloadExcel(HttpServletRequest request, HttpServletResponse response,
                                     String fileName, List<CustemExcelDto> sheetDatas) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/ms-excel;charset=utf-8");
        String encodeFileName = ExcelUtil.encodeFileName(fileName, request);
        response.setHeader("Content-Disposition", "attachment;filename=" + encodeFileName);
        HSSFWorkbook work = getExcel(sheetDatas);
        work.write(response.getOutputStream());
    }

    public static String encodeFileName(String fileName, HttpServletRequest request) {
        String codedFilename = fileName;
        try {
            String agent = request.getHeader("USER-AGENT");
            if (agent != null) {
                if (agent.contains("MSIE") || agent.contains("Trident") || agent.contains("Edge")) {
                    codedFilename = URLEncoder.encode(fileName, "UTF-8");
                } else if (agent.contains("Mozilla")) {
                    codedFilename = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
                }
            }
        } catch (Exception e) {
            LOGGER.error("AuditController encodeFileName error. fileName:" + fileName, e);
        }
        return codedFilename;
    }

    public static HSSFWorkbook getExcel(List<CustemExcelDto> sheetDatas) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        if (!sheetDatas.isEmpty()) {
            // 检查是否有表格查过最大值65535, 如果超了创建新的sheet导出
            List<CustemExcelDto> tmpSheetDatas = new LinkedList<>();
            for (CustemExcelDto custemExcelDto : sheetDatas) {
                List<List<Object>> rows = custemExcelDto.getRows();
                int len = rows.size();
                if (len <= 65535) {
                    tmpSheetDatas.add(custemExcelDto);
                } else {
                    int size = 65535;
                    int count = len / size;
                    int mod = len % size;
                    if (mod > 0) {
                        count++;
                    }
                    int start = 0;
                    for (int i = 0 ; i < count; i++) {
                        CustemExcelDto ced = new CustemExcelDto();
                        ced.setSheetName(custemExcelDto.getSheetName() + "-" + i);
                        ced.setTitles(custemExcelDto.getTitles());
                        if ((start + size) > len) {
                            size = len - start;
                        }
                        List<List<Object>> realRows = rows.subList(start, (start + size));
                        ced.setRows(realRows);
                        tmpSheetDatas.add(ced);
                        start += size;
                    }
                }
            }
            for (CustemExcelDto custemExcelDto : tmpSheetDatas) {
                HSSFSheet sheet;
                if (StringUtils.isNotEmpty(custemExcelDto.getSheetName())) {
                    sheet = workbook.createSheet(custemExcelDto.getSheetName());
                } else {
                    sheet = workbook.createSheet();
                }

                int rowIndex = 0;
                if (custemExcelDto.getTitles() != null && !custemExcelDto.getTitles().isEmpty()) {
                    HSSFRow titileRow = sheet.createRow(rowIndex);
                    rowIndex++;
                    for (int i = 0; i < custemExcelDto.getTitles().size(); i++) {
                        String cellValue = custemExcelDto.getTitles().get(i);
                        titileRow.createCell(i).setCellValue(cellValue);
                    }
                }

                // 填充表格内容
                if (custemExcelDto.getRows() != null && !custemExcelDto.getRows().isEmpty()) {
                    for (List<Object> row : custemExcelDto.getRows()) {
                        HSSFRow rowData = sheet.createRow(rowIndex);
                        rowIndex++;
                        if (row != null && !row.isEmpty()) {
                            for (int i = 0; i < row.size(); i++) {
                                Object rowValue = row.get(i);
                                if (rowValue instanceof String) {
                                    String value = (String) rowValue;
                                    HSSFCell hssfCell = rowData.createCell(i);
                                    hssfCell.setCellType(CellType.STRING);
                                    hssfCell.setCellValue(value);
                                } else if (rowValue instanceof Boolean) {
                                    Boolean value = (Boolean) rowValue;
                                    HSSFCell hssfCell = rowData.createCell(i);
                                    hssfCell.setCellType(CellType.BOOLEAN);
                                    hssfCell.setCellValue(value);
                                } else if (rowValue instanceof Date) {
                                    Date value = (Date) rowValue;
                                    HSSFCell hssfCell = rowData.createCell(i);
                                    hssfCell.setCellValue(value);
                                } else if (rowValue instanceof Double) {
                                    Double value = (Double) rowValue;
                                    HSSFCell hssfCell = rowData.createCell(i);
                                    hssfCell.setCellType(CellType.NUMERIC);
                                    hssfCell.setCellValue(value);
                                } else if (rowValue != null && rowValue.getClass() != null && rowValue.getClass().isPrimitive()) {
                                    // 判断如果是基本数据类型，处理
                                    String value = String.valueOf(rowValue);
                                    HSSFCell hssfCell = rowData.createCell(i);
                                    hssfCell.setCellValue(value);
                                } else {
                                    LOGGER.error("ExcelUtil getExcel unkonw cell type. set it String rowValue:{}", rowValue);
                                    HSSFCell hssfCell = rowData.createCell(i);
                                    hssfCell.setCellType(CellType.STRING);
                                    hssfCell.setCellValue(rowValue != null ? rowValue.toString() : "");
                                }
                            }
                        }
                    }
                }
            }
        }
        return workbook;
    }
}
