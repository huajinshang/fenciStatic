package com.lawsiji.txtcheck.common;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class FileTools {
    // 自定义上传文件存储目录
    private static final String UPLOAD_DIRECTORY = "upload";

    //写文件
    public static void wirteFileString(String str, String fileName,Boolean flag) {
        Writer writer;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName, flag), "UTF-8"));
            writer.write(str);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //读文件
    public static List<String> getFileString(String fileName) {
        FileReader reader;
        List<String> ls = new ArrayList<String>();
        try {
            FileInputStream fis = new FileInputStream(fileName);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                ls.add(line);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ls;
    }


    //MultipartFile上传文件
    public static String getFileInfo(HttpServletRequest request, HttpServletResponse response, MultipartFile file) {
        String filePath = new String();
        String uploadPath = request.getSession().getServletContext().getRealPath("./") + UPLOAD_DIRECTORY;

        // 如果目录不存在则创建
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        //判断文件是否为空
        if (!file.isEmpty()) {
            try {
                //文件的保存路径
                filePath = request.getSession().getServletContext().getRealPath("/") + UPLOAD_DIRECTORY + File.separator + file.getOriginalFilename();

                //转存文件
                file.transferTo(new File(filePath));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return filePath;
    }//函数结束符

    /*******************删除临时上传文件夹目录及文件**********************/
    //删除文件夹 folderPath 文件夹完整绝对路径
    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); //删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            java.io.File myFilePath = new java.io.File(filePath);
            myFilePath.delete(); //删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //删除指定文件夹下所有文件 path 文件夹完整绝对路径
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {//判断该路径是否是一个目录
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);//再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }



    public static void main(String[] args) {
        FileTools.wirteFileString("极限词1\r\n极限词2\r\n", "d:/checkfile.txt",true);
        System.out.println(FileTools.getFileString("d:/checkfile.txt"));
    }
}
