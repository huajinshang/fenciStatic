package com.lawsiji.txtcheck.common;

import java.io.*;
import java.text.NumberFormat;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelPoiTools {

    public static String read(String filePath) {
        // 创建文件输出流
        FileWriter fw = null;
        PrintWriter out = null;
        String content="";
        File file=new File(filePath);
        String fileName= new String();
        String transformPath=new String();
        try {
            /* 验证文件是否合法 */
            if (!WDWUtil.validateExcel(filePath)) {
                System.out.println("excel文件格式错误！");
            }else{
                /* 判断文件的类型，是2003还是2007 */
                if (WDWUtil.isExcel2007(filePath)) {
                    content = readEXCEL2007(filePath);
                    // 指定生成txt的文件路径
                    fileName = file.getName().replace(".xlsx", "");
                } else if (WDWUtil.isExcel2003(filePath)) {
                    content = readEXCEL(filePath);
                    // 指定生成txt的文件路径
                    fileName = file.getName().replace(".xls", "");
                }
            }
            transformPath=file.getParent() + "/" + fileName + ".txt";
            wirteFileString(content,transformPath);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return content;
    }

    // 读取xls文件
    public static String readEXCEL(String file) throws IOException {
        StringBuilder content = new StringBuilder();
        HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(file));// 创建对Excel工作簿文件的引用
        for (int numSheets = 0; numSheets < workbook.getNumberOfSheets(); numSheets++) {
            if (null != workbook.getSheetAt(numSheets)) {
                HSSFSheet aSheet = workbook.getSheetAt(numSheets);// 获得一个sheet
                for (int rowNumOfSheet = 0; rowNumOfSheet <= aSheet.getLastRowNum(); rowNumOfSheet++) {
                    if (null != aSheet.getRow(rowNumOfSheet)) {
                        HSSFRow aRow = aSheet.getRow(rowNumOfSheet); // 获得一个行
                        for (short cellNumOfRow = 0; cellNumOfRow <= aRow.getLastCellNum(); cellNumOfRow++) {
                            if (null != aRow.getCell(cellNumOfRow)) {
                                HSSFCell aCell = aRow.getCell(cellNumOfRow);// 获得列值
                                if (convertCell(aCell).length() > 0) {
                                    content.append(convertCell(aCell));
                                }
                            }
                            content.append("\n");
                        }
                    }
                }
            }
        }
        return content.toString();
    }

    // 读取xlsx文件
    public static String readEXCEL2007(String file) throws IOException {
        StringBuilder content = new StringBuilder();
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        for (int numSheets = 0; numSheets < workbook.getNumberOfSheets(); numSheets++) {
            if (null != workbook.getSheetAt(numSheets)) {
                XSSFSheet aSheet = workbook.getSheetAt(numSheets);// 获得一个sheet
                for (int rowNumOfSheet = 0; rowNumOfSheet <= aSheet.getLastRowNum(); rowNumOfSheet++) {
                    if (null != aSheet.getRow(rowNumOfSheet)) {
                        XSSFRow aRow = aSheet.getRow(rowNumOfSheet); // 获得一个行
                        for (short cellNumOfRow = 0; cellNumOfRow <= aRow.getLastCellNum(); cellNumOfRow++) {
                            if (null != aRow.getCell(cellNumOfRow)) {
                                XSSFCell aCell = aRow.getCell(cellNumOfRow);// 获得列值
                                if (convertCell(aCell).length() > 0) {
                                    content.append(convertCell(aCell));
                                }
                            }
                            content.append("\n");
                        }
                    }
                }
            }
        }
        return content.toString();
    }


    private static String convertCell(Cell cell) {
        NumberFormat formater = NumberFormat.getInstance();
        formater.setGroupingUsed(false);
        String cellValue = "";
        if (cell == null) {
            return cellValue;
        }
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_NUMERIC:
                cellValue = formater.format(cell.getNumericCellValue());
                break;
            case HSSFCell.CELL_TYPE_STRING:
                cellValue = cell.getStringCellValue();
                break;
            case HSSFCell.CELL_TYPE_BLANK:
                cellValue = cell.getStringCellValue();
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                cellValue = Boolean.valueOf(cell.getBooleanCellValue()).toString();
                break;
            case HSSFCell.CELL_TYPE_ERROR:
                cellValue = String.valueOf(cell.getErrorCellValue());
                break;
            default:
                cellValue = "";
        }
        return cellValue.trim();
    }

    //写文件
    public static void wirteFileString(String str, String fileName) {
        Writer writer;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName, true), "UTF-8"));
            writer.write(str);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





}
