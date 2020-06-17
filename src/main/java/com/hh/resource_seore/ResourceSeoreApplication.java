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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@SpringBootApplication
@Controller
@Api(value = "index", description = "所有api说明", tags = "index")
public class ResourceSeoreApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ResourceSeoreApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        // 注意这里要指向原先用main方法执行的Application启动类
        return builder.sources(ResourceSeoreApplication.class);
    }

    @Autowired
    private RemoteProperties getRemoteProperties;

    @RequestMapping("/")
    public void index(HttpServletResponse response) {
        response.setStatus(302); //设置响应行的状态码为302 重定向
        response.setHeader("Location", "swagger-ui.html#");
    }

    @Autowired
    private RemoteProperties remoteProperties;


    @Autowired
    private PathHandleService pathHandleService;

    @RequestMapping(value = "/upload")
    @ResponseBody
    @ApiOperation(value = "上传文件资源")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "project", value = "项目名称，用于区分不同项目", required = false),
            @ApiImplicitParam(name = "file", type = "MultipartFile", dataType = "MultipartFile",
                    value = "保存的文件，MultipartFile 作为接收参数", required = true)})
    public String upload(@RequestParam("file") MultipartFile file,
                       @RequestParam(required = false) String project) {
        try {
            return pathHandleService.storage(file.getBytes(), project, file.getOriginalFilename().toString());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @ApiOperation(value = "下载文件，根据返回路径下载资源")
    @RequestMapping(value = "/{code}")
    public ResponseEntity<FileSystemResource> get(@PathVariable("code") String code) throws IOException {
        return export(pathHandleService.out(code));
    }

    @ApiOperation(value = "查询文档结构")
    @RequestMapping(value = "/query")
    @ResponseBody
    public Map get() throws IOException {
        return CommonObject.getFiles();
    }



    private ResponseEntity<FileSystemResource> export(File file) {
        if (file == null) {
            return null;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", "attachment; filename=" +file.getName());
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("Last-Modified", new Date().toString());
        headers.add("ETag", String.valueOf(System.currentTimeMillis()));

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new FileSystemResource(file));
    }
}
