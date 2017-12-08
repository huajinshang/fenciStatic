package com.lawsiji.txtcheck.common;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import java.io.*;

public class ExcelJxlTools {
    /*******************EXCEL文件相关方法**********************/
    // 去读Excel的方法readExcel，该方法的入口参数为一个File对象
    public static void readExcelWrite2TXT(File file) {
        // 创建文件输出流
        FileWriter fw = null;
        PrintWriter out = null;
        try {
            // 指定生成txt的文件路径
            String fileName = file.getName().replace(".xls", "");
            fw = new FileWriter(file.getParent() + "/" + fileName + ".txt");
            out = new PrintWriter(fw);
            // 创建输入流，读取Excel
            InputStream is = new FileInputStream(file.getAbsolutePath());
            // jxl提供的Workbook类
            Workbook wb = Workbook.getWorkbook(is);
            // Excel的页签数量
            int sheet_size = wb.getNumberOfSheets();
            for (int index = 0; index < sheet_size; index++) {
                // 每个页签创建一个Sheet对象
                Sheet sheet = wb.getSheet(index);
                // sheet.getRows()返回该页的总行数
                for (int i = 0; i < sheet.getRows(); i++) {
                    // sheet.getColumns()返回该页的总列数
                    for (int j = 0; j < sheet.getColumns(); j++) {
                        String cellinfo = sheet.getCell(j, i).getContents();
                        // 将从Excel中读取的数据写入到txt中
                        out.println(cellinfo);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 记得关闭流
                out.close();
                fw.close();
                // 由于此处用到了缓冲流，如果数据量过大，不进行flush操作，某些数据将依旧
                // 存在于内从中而不会写入文件，此问题一定要注意
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
