package com.smile.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * @author smileplus
 */
@Slf4j
@RestController
@RequestMapping("excel")
public class DownloadExcelController {

    @ResponseBody
    @RequestMapping("download/{filename}")
    public void downloadFile(@PathVariable("filename") String filename, HttpServletResponse resp) throws IOException {

        ClassPathResource classPathResource = new ClassPathResource("static/file/" + filename);
        InputStream inputStream = classPathResource.getInputStream();
        File file = classPathResource.getFile();
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/octet-stream;charset=UTF-8");
        //加上设置大小下载下来的.xlsx文件打开时才不会报“Excel 已完成文件级验证和修复。此工作簿的某些部分可能已被修复或丢弃”
        resp.addHeader("Content-Length", String.valueOf(file.length()));
        resp.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        try {
            OutputStream os = resp.getOutputStream();
            bis = new BufferedInputStream(inputStream);
            int len;
            while ((len = bis.read(buff)) != -1) {
                os.write(buff, 0, len);
                os.flush();
            }
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    System.out.println("下载模板失败");
                }
            }
        }
        System.out.println("下载模板成功");
    }
}
