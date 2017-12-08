package com.lawsiji.txtcheck.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class textJxlTools {
    //file——数据库文本
    //file2——目标文本
    public static void write(File file,File file2){
        if (file.exists() && file.isFile()) {

            InputStreamReader read = null;
            String line = "";
            BufferedReader input = null;
            WritableWorkbook wbook = null;
            WritableSheet sheet;
            int maxCell=8;

            try {
                read = new InputStreamReader(new FileInputStream(file), "UTF-8");
                input = new BufferedReader(read);

                wbook = Workbook.createWorkbook(file2);// 根据路径生成excel文件
                sheet = wbook.createSheet("first", 0);// 新标签页

//                try {
//                    Label company = new Label(0, 0, "公司名称");// 如下皆为列名
//                    sheet.addCell(company);
//                    Label position = new Label(1, 0, "岗位");
//                    sheet.addCell(position);
//                    Label salary = new Label(2, 0, "薪资");
//                    sheet.addCell(salary);
//                    Label status = new Label(3, 0, "状态");
//                    sheet.addCell(status);
//                } catch (RowsExceededException e) {
//                    e.printStackTrace();
//                } catch (WriteException e) {
//                    e.printStackTrace();
//                }

                int m = 1;// excel行数
                int n = 0;// excel列数
                Label t;
                while ((line = input.readLine()) != null) {

                    String[] words = line.split(",");// 把读出来的这行根据空格或tab分割开

                    for (int i = 0; i < words.length; i++) {
                        if (!words[i].matches("\\s*")) { // 当不是空行时
                            t = new Label(n, m, words[i].trim());
                            sheet.addCell(t);
                            n++;
                        }
                        if(n%maxCell==0){
                            m++;
                            n=0;
                            continue;
                        }
                    }
//                    n = 0;// 回到列头部
//                    m++;// 向下移动一行
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (RowsExceededException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            } finally {
                try {
                    wbook.write();
                    wbook.close();
                    input.close();
                    read.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (WriteException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("over!");
        } else {
            System.out.println("file is not exists or not a file");
        }
    }


}