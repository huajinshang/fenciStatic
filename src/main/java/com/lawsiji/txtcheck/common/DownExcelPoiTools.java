package com.lawsiji.txtcheck.common;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


/**
 * 从excel读取数据/往excel中写入 excel有表头，表头每列的内容对应实体类的属性
 *
 * @author nagsh
 */
public class DownExcelPoiTools {

    public static void writer(String path, String fileName, String fileType, String[] arrcon) throws IOException {
        String arr[] = {"common", "medicines", "foods", "clothes"};
        Workbook wb = null;
        String excelPath = path + File.separator + "LimitWords" + fileType;
        File file = new File(excelPath);
        Sheet sheet = null;
        //创建工作文档对象
        if (!file.exists()) {
            if (fileType.equals(".xls")) {
                wb = new HSSFWorkbook();
            } else if (fileType.equals(".xlsx")) {
                wb = new XSSFWorkbook();
            } else {
                System.out.println("文件格式不正确");
            }

        } else {
            if (fileType.equals("xls")) {
                wb = new HSSFWorkbook();

            } else if (fileType.equals("xlsx")) {
                wb = new XSSFWorkbook();

            } else {
                System.out.println("文件格式不正确");
            }
        }

        for (int i = 0; i < arrcon.length; i++) {
            int maxCell = 8;        //定义最大列
            int maxRow = 0;         //定义最大行
            int c = 0;              //定义列
            int r = 1;              //定义行(r=1不包括表头)
            sheet = (Sheet) wb.createSheet(arr[i]);
            //添加表头
            Row row = sheet.createRow(0);
            Cell cell = row.createCell(0);
            row.setHeight((short) 540);
            cell.setCellValue(arr[i] + "类极限词表");    //创建第一行

        /*设置样式*/
            CellStyle style = wb.createCellStyle(); // 样式对象
            style.setVerticalAlignment(VerticalAlignment.CENTER);   //垂直居中
            style.setAlignment(HorizontalAlignment.CENTER);         //水平居中
            style.setWrapText(true);// 指定当单元格内容显示不下时自动换行

            cell.setCellStyle(style); // 样式，居中

            Font font = wb.createFont();
            font.setFontName("宋体");
            font.setFontHeight((short) 280);
            style.setFont(font);

            // 单元格合并
            // 四个参数分别是：起始行，起始列，结束行，结束列
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, maxCell - 1));
            sheet.autoSizeColumn(5200);

            //循环写入excel
            String[] words = arrcon[i].split(",");
            row = sheet.createRow(r);    //创建第二行
            for (int j = 0; j < words.length; j++) {
                cell = row.createCell(c);
                cell.setCellValue(words[j]);
                cell.setCellStyle(style); // 样式，居中
                sheet.setColumnWidth(j, 20 * 256);
                c++;
                if (c % maxCell == 0) {
                    r++;
                    row = sheet.createRow(r);    //创建第二行
                    c = 0;
                    continue;
                }
            }

            row.setHeight((short) 540);
        }


        //创建文件流
        OutputStream stream = new FileOutputStream(excelPath);
        //写入数据
        wb.write(stream);
        //关闭文件流
        stream.close();
    }

}