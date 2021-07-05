package com.ch.excel_send.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.List;

public class WriteExcel {
    private static final String EXCEL_XLS = "xls";
    private static final String EXCEL_XLSX = "xlsx";


    /**
     * 写入excel一行的数据
     *
     * @param dataList      数据 list格式
     * @param row           从第几行开始
     * @param clounm        从第几列开始
     * @param finalXlsxPath 需要写入的文件
     */
    public static void writeRow(List<String> dataList, int row, int clounm, File finalXlsxPath) {
        OutputStream out = null;
        try {
            // 读取Excel文档
            Workbook workBook = getWorkbok(finalXlsxPath);
            // sheet 对应一个工作页
            Sheet sheet = workBook.getSheetAt(0);
            out = new FileOutputStream(finalXlsxPath);
            workBook.write(out);
            try {
                Row row1 = sheet.createRow(row);
                for (int j = 0; j < dataList.size(); j++) {
                    Cell row1Cell = row1.createCell(j + clounm);
                    row1Cell.setCellValue(dataList.get(j));
                }
                // 创建文件输出流，输出电子表格：这个必须有，否则你在sheet上做的任何操作都不会有效
                out = new FileOutputStream(finalXlsxPath);
                workBook.write(out);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.flush();
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("数据导出成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeRow(List<String> dataList, File finalXlsxPath) {
        writeRow(dataList, 0, 0, finalXlsxPath);
    }

    /**
     * 写入excel一列的数据
     *
     * @param dataList      数据 list格式
     * @param row           从第几行开始
     * @param clounm        从第几列开始
     * @param finalXlsxPath 需要写入的文件
     */
    public static void writeCloumn(List<String> dataList, int row, int clounm, File finalXlsxPath) {
        OutputStream out = null;
        try {
            // 读取Excel文档
            Workbook workBook = getWorkbok(finalXlsxPath);
            // sheet 对应一个工作页
            Sheet sheet = workBook.getSheetAt(0);
            out = new FileOutputStream(finalXlsxPath);
            workBook.write(out);
            try {
                for (int j = 0; j < dataList.size(); j++) {
                    Row row1 = sheet.createRow(j + row);
                    Cell row1Cell = row1.createCell(clounm);
                    row1Cell.setCellValue(dataList.get(j));
                }
                // 创建文件输出流，输出电子表格：这个必须有，否则你在sheet上做的任何操作都不会有效
                out = new FileOutputStream(finalXlsxPath);
                workBook.write(out);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.flush();
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("数据导出成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeCloumn(List<String> dataList, File finalXlsxPath) {
        writeCloumn(dataList, 0, 0, finalXlsxPath);
    }


    /**
     * 写入多行数据
     * @param dataList
     * @param row
     * @param clounm
     * @param finalXlsxPath
     */
    public static void writeData(List<List<String>> dataList, int row, int clounm, File finalXlsxPath) {
        OutputStream out = null;
        try {
            // 读取Excel文档
            Workbook workBook = getWorkbok(finalXlsxPath);
            // sheet 对应一个工作页
            Sheet sheet = workBook.getSheetAt(0);
            out = new FileOutputStream(finalXlsxPath);
            workBook.write(out);
            try {
                for (int i = 0; i < dataList.size(); i++) {
                    Row row1 = sheet.createRow(i + row);
                    List<String> rowData = dataList.get(i);
                    for (int j = 0; j < rowData.size(); j++) {
                        Cell row1Cell = row1.createCell(clounm + j);
                        row1Cell.setCellValue(rowData.get(j));
                    }
                }
                // 创建文件输出流，输出电子表格：这个必须有，否则你在sheet上做的任何操作都不会有效
                out = new FileOutputStream(finalXlsxPath);
                workBook.write(out);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.flush();
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("数据导出成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 在原来excel里增加一列数据
     * @param dataList
     * @param cloumnCount
     * @param row
     * @param finalXlsxPath
     */
    public static void updateExcel(List<String> dataList, int cloumnCount, int row, File finalXlsxPath) {
        OutputStream out = null;
        try {
            // 读取Excel文档
            Workbook workBook = getWorkbok(finalXlsxPath);
            // sheet 对应一个工作页
            Sheet sheet = workBook.getSheetAt(0);
            out = new FileOutputStream(finalXlsxPath);
            workBook.write(out);
            jxl.Workbook wb = null;
            try {
                wb = jxl.Workbook.getWorkbook(finalXlsxPath);
                jxl.Sheet jxlSheet = wb.getSheet(0);
                /**
                 * 删除原有数据，除了属性列
                 */
                int rowNum = 0;
                for (int i = 1; i <= jxlSheet.getRows(); i++) {
                    Row row1 = sheet.createRow(i - 1);
                    for (int j = 0; j < jxlSheet.getColumns(); j++) {
                        String cellinfo = jxlSheet.getCell(j, i - 1).getContents();
                        if (cellinfo.isEmpty()) {
                            continue;
                        }
                        Cell row1Cell = row1.createCell(j);
                        row1Cell.setCellValue(cellinfo);
                    }
                    if (i - 1 >= row && rowNum < dataList.size()) {
                        Cell first = row1.createCell(cloumnCount - 1);
                        String s = dataList.get(rowNum++);
                        System.out.println(s);
                        first.setCellValue(s);
                    }
                }
                // 创建文件输出流，输出电子表格：这个必须有，否则你在sheet上做的任何操作都不会有效
                out = new FileOutputStream(finalXlsxPath);
                workBook.write(out);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.flush();
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("数据导出成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断Excel的版本,获取Workbook
     *
     * @return
     * @throws IOException
     */
    public static Workbook getWorkbok(File file) throws IOException {
        Workbook wb = null;
        FileInputStream in = new FileInputStream(file);
        if (file.getName().endsWith(EXCEL_XLS)) {     //Excel&nbsp;2003
            wb = new HSSFWorkbook(in);
        } else if (file.getName().endsWith(EXCEL_XLSX)) {    // Excel 2007/2010
            wb = new XSSFWorkbook(in);
        }
        return wb;
    }
}

