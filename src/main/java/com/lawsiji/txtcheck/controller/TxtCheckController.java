package com.lawsiji.txtcheck.controller;

import com.lawsiji.txtcheck.common.ExcelPoiTools;
import com.lawsiji.txtcheck.common.FileTools;

import com.lawsiji.txtcheck.common.DownExcelPoiTools;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.io.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
@RequestMapping("/")
public class TxtCheckController {

    //    写文件
    @ResponseBody
    @RequestMapping(value = "/writeLimitWords", method = RequestMethod.GET)
    public void writeLimitWords(@RequestParam(value = "words", required = true) String str, @RequestParam(value = "limit", required = true) String status, @RequestParam(value = "flag", required = true) boolean flag) {
        String path = "d:/limitWords/" + status + ".txt";
        System.out.println(flag);
        FileTools.wirteFileString(str, path, flag);
    }

    //    读文件
    @ResponseBody
    @RequestMapping(value = "/getLimitWords", method = RequestMethod.GET)
    public String getLimitWords(@RequestParam(value = "limit", required = true) String str) {
        System.out.println(str);
        String path = "d:/limitWords/" + str + ".txt";
        List<String> ls = FileTools.getFileString(path);
        StringBuffer sb = new StringBuffer();
        for (String s : ls) {
            sb.append(s);
        }
        return sb.toString();
    }

    //    上传文件
    @ResponseBody
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public void uploadFile(@RequestParam(value = "fileInfo", required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) {

        Boolean flag = Boolean.valueOf(request.getParameter("flag"));
        System.out.println("是否追加" + flag);
        String limitStatus = request.getParameter("limitStatus");
        String[] array = limitStatus.split(",");

        //上传文件的路径
        String path = FileTools.getFileInfo(request, response, file);
        System.out.println("path:" + path);

        //读取上传文件里的内容
        List<String> ls = FileTools.getFileString(path);
        System.out.println(ls);

        StringBuffer sb = new StringBuffer();
        for (String s : ls) {
            if (s.length() == 0) {
                continue;
            } else {
                String str2 = s.substring(s.length() - 1, s.length());
                String comma = ",";
                if (str2.equals(comma) == false) {
                    s = s + ",";
                }
            }
            sb.append(s);
        }
        //对上传的文件内容进行整理
        String str = sb.toString().replaceAll("(，|\\s|\n)", ",");
        System.out.println(str);

        //将上传的文件的内容写到文件里
        for (int i = 0; i < array.length; i++) {
            FileTools.wirteFileString(str, "d:/limitWords/" + array[i] + ".txt", flag);
        }
        System.out.println("txt上传成功");

        //直接删除上传的文件
        String deletePath = request.getSession().getServletContext().getRealPath("./") + "upload";
        FileTools.delFolder(deletePath);
        System.out.println("临时文件删除成功");


    }

    //    //文件下载
    @ResponseBody
    @RequestMapping(value = "/downloadTxt", method = RequestMethod.GET)
    public ResponseEntity<byte[]> downloadTxt(@RequestParam(value = "checkStatus", required = false) String checkStatus) throws IOException {

        String path = "d:/limitWords/" + checkStatus + ".txt";
        List<String> ls = FileTools.getFileString(path);
        StringBuffer body = new StringBuffer();
        for (String s : ls) {
            body.append(s);
        }

        File file = new File(path);
        String filename = file.getName();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attchement;filename=" + filename);
        HttpStatus statusCode = HttpStatus.OK;
        ResponseEntity entity = new ResponseEntity(body, headers, statusCode);
        return entity;

//        String path1 = "C:\\Users\\win8\\Desktop\\";
//        String Name = "被保险人员清单";
//        String fileType = "xlsx";
//        DownExcelPoiTools.writer(path1, Name, fileType, String.valueOf(body));
//
//        System.out.println(body);

        //jxl实现的
//        File targetFile = new File("C:\\Users\\win8\\Desktop\\work.xlsx");// 将生成的excel表格
//        textJxlTools.write(file,targetFile);
    }


    //    //文件下载
    @ResponseBody
    @RequestMapping(value = "/downloadExcel", method = RequestMethod.GET)
    public void downloadExcel(@RequestParam(value = "checkStatus", required = false) String checkStatus,@RequestParam(value = "fileDownType", required = false) String fileDownType, HttpServletRequest req, HttpServletResponse res) {
        String arr[]={"common","medicines","foods","clothes"};
        String []arrcon = new String[4];
        for(int i=0;i<arr.length;i++){
            String path = "d:/limitWords/" + arr[i] + ".txt";
            StringBuffer body = new StringBuffer();
            List<String> ls = FileTools.getFileString(path);
            for (String s : ls) {
                body.append(s);
            }
            arrcon[i] = String.valueOf(body);
        }

        String tmpPath=req.getSession().getServletContext().getRealPath("/")+"upload";
        // 如果目录不存在则创建
        File uploadDir = new File(tmpPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        try {
            DownExcelPoiTools.writer(tmpPath, checkStatus, fileDownType,arrcon);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            // path是指欲下载的文件的路径。
            String downFileInfo = tmpPath+File.separator+"LimitWords"+fileDownType;
            File file = new File(downFileInfo);
            // 取得文件名。
            String filename = file.getName();
            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(downFileInfo));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            res.reset();
            // 设置response的Header
            res.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
            res.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(res.getOutputStream());
            res.setContentType("application/vnd.ms-excel;charset=UTF-8");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        //直接删除上传的文件
        FileTools.delFolder(tmpPath);
        System.out.println("临时文件删除成功");

    }


    //EXCEL相关
    @ResponseBody
    @RequestMapping(value = "/uploadExcel", method = RequestMethod.POST)
    public void uploadExcel(@RequestParam(value = "fileInfo", required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws IOException {

        Boolean flag = Boolean.valueOf(request.getParameter("flag"));
        System.out.println("是否追加" + flag);
        String limitStatus = request.getParameter("limitStatus");
        String[] array = limitStatus.split(",");
        //上传文件的
        String path = FileTools.getFileInfo(request, response, file);
        String s = ExcelPoiTools.read(path);
        String str = s.replaceAll("\\s{1,}", ",");

        //将上传的文件的内容写到文件里
        for (int i = 0; i < array.length; i++) {
            FileTools.wirteFileString(str, "d:/limitWords/" + array[i] + ".txt", flag);
        }
        System.out.println("excel上传成功");

        //直接删除上传的文件
        String deletePath = request.getSession().getServletContext().getRealPath("./") + "upload";
        FileTools.delFolder(deletePath);
        System.out.println("excel删除成功");
    }

}//最大函数结束框