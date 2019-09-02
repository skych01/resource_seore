package com.hh.resource_seore;

import io.swagger.annotations.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@SpringBootApplication
@Controller
@Api(value = "index", description = "资源相关api")
public class ResourceSeoreApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ResourceSeoreApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        // 注意这里要指向原先用main方法执行的Application启动类
        return builder.sources(ResourceSeoreApplication.class);
    }


    @RequestMapping(value = "/updateFile", method = RequestMethod.POST)
    @ApiOperation(value = "上传文件资源")
    @ResponseBody
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "fileName", value = "book's name", required = true),
            @ApiImplicitParam(name = "file", value = "book's date", required = false)})
    public String test(String fileName,
                       @RequestParam("file") MultipartFile file,
                       HttpServletRequest request) {
        String path = ImageTools.getAppPath(request), serverPath = "";

        for (int i = 0; i < 3; i++) {
            serverPath = serverPath + CreateBasicData.getRandomString(3);
            serverPath += "/";
        }
        File m = new File(path + serverPath);
        System.out.println(path + serverPath);
        if (!m.exists()) {
            m.mkdirs();
        }
        serverPath += fileName;
        try {
            System.out.println(path + serverPath);
            ImageTools.byte2file(file.getBytes(), path + serverPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serverPath;
    }


    @RequestMapping(value = "/updateFile4fileName", method = RequestMethod.POST)
    @ApiOperation(value = "上传文件资源")
    @ApiParam()
    @ResponseBody
    public String test1(String fileName, String imgPath, HttpServletRequest request) {
        String path = ImageTools.getAppPath(request), serverPath = "";
        for (int i = 0; i < 3; i++) {
            serverPath = serverPath + CreateBasicData.getRandomString(i);
            serverPath += "/";
        }
        try {
            DownloadImg.download(imgPath, path + serverPath + fileName, path + serverPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serverPath + fileName;
    }

    @RequestMapping("/")
    public void index(HttpServletResponse response) {
        response.setStatus(302); //设置响应行的状态码为302 重定向
        response.setHeader("Location", "swagger-ui.html#");
    }
}
