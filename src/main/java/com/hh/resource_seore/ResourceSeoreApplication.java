package com.hh.resource_seore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

@SpringBootApplication
@RestController
public class ResourceSeoreApplication  extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ResourceSeoreApplication.class, args);
    }
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        // 注意这里要指向原先用main方法执行的Application启动类
        return builder.sources(ResourceSeoreApplication.class);
    }

    @RequestMapping("/updateFile")
    public String test(String fileName, @RequestParam("file") MultipartFile file, HttpServletRequest request) {
        String path = ImageTools.getAppPath(request) ,serverPath="";

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


    @RequestMapping("/updateFile4fileName")
    public String test1(String fileName,String imgPath, HttpServletRequest request) {
        String path = ImageTools.getAppPath(request), serverPath = "";
        for (int i = 0; i < 3; i++) {
            serverPath = serverPath + CreateBasicData.getRandomString(3);
            serverPath += "/";
        }
        File m = new File(path + serverPath);
        System.out.println(path + serverPath);
        try {
            DownloadImg.download(imgPath, path + serverPath + fileName, path + serverPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serverPath+fileName;
    }
}
