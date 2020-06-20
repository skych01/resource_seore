package com.hh.resource_seore;

import com.hh.resource_seore.common.CommonObject;
import com.hh.resource_seore.service.PathHandleService;
import com.hh.resource_seore.setting.RemoteProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

@SpringBootApplication
public class ResourceSeoreApplication{

    public static void main(String[] args) {
        SpringApplication.run(ResourceSeoreApplication.class, args);
    }


    @Autowired
    private RemoteProperties remoteProperties;

//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//        // 注意这里要指向原先用main方法执行的Application启动类
//        return builder.sources(ResourceSeoreApplication.class);
//    }


    /**
     * 初始化时将资源持久化的文件目录映射到内存中
     * @throws IOException
     */
    @PostConstruct
    private void initFile()  {
        Properties pps = new Properties();
        try {
            pps.load(new FileInputStream(remoteProperties.getPath() + "/file.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Enumeration enum1 = pps.propertyNames();//得到配置文件的名字
        Map map = CommonObject.getFiles();
        while (enum1.hasMoreElements()) {
            String strKey = (String) enum1.nextElement();
            String strValue = pps.getProperty(strKey);
            map.put(strKey, strValue);
        }
    }


}
