package com.hh.resource_seore;

import io.swagger.annotations.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
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


    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    @ApiOperation(value = "上传文件资源")
    @ResponseBody
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "fileName", value = "文件名称，用于确认保存的文件名称及文件类型", required = true),
            @ApiImplicitParam(name = "file", type = "MultipartFile", dataType = "MultipartFile",
                    value = "保存的文件，MultipartFile 作为接收参数", required = true)})
    public String uploadFile(String fileName,
                             @RequestParam("file") MultipartFile file,
                             String project,
                             HttpServletRequest request) {
        PathInfo pathInfo;
        if (StringUtils.isEmpty(project)) {
            pathInfo = getPath(request, fileName);
        } else {
            pathInfo = getPath(request, fileName, project);
        }
        String path = pathInfo.storePath;
        try {
            FileTools.byte2file(file.getBytes(), path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pathInfo.returnPath;
    }

    private class PathInfo {
        String storePath;
        String returnPath;

        public PathInfo(String storePath, String returnPath) {
            this.storePath = storePath;
            this.returnPath = returnPath;
        }
    }

    private String defaultProject = "default";

    private PathInfo getPath(HttpServletRequest request, String fileName) {
        return getPath(request, fileName, defaultProject);
    }

    private PathInfo getPath(HttpServletRequest request, String fileName, String project) {
        String path = FileTools.getAppPath(request), serverPath = "";

        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        serverPath = project + "/" + suffix + "/";
        for (int i = 0; i < 3; i++) {
            serverPath = serverPath + CreateBasicData.getRandomString(3);
            serverPath += "/";
        }
        File m = new File(path + serverPath);
        if (!m.exists()) {
            m.mkdirs();
        }

        return new PathInfo(path + serverPath + fileName, serverPath + fileName);
    }

    @RequestMapping(value = "/uploadFile4fileName", method = RequestMethod.POST)
    @ApiOperation(value = "上传文件资源,通过文件路径上传")
    @ResponseBody
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "fileName", value = "文件名称，用于确认保存的文件名称及文件类型", required = true),
            @ApiImplicitParam(name = "filePath", value = "保存的文件，String 传输被保存资源的路径", required = true)})
    public String test1(String fileName, String filePath, HttpServletRequest request) {
        PathInfo pathInfo = getPath(request, fileName);
        try {
            FileTools.download(filePath, pathInfo.storePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pathInfo.returnPath;
    }

    @RequestMapping("/")
    public void index(HttpServletResponse response) {
        response.setStatus(302); //设置响应行的状态码为302 重定向
        response.setHeader("Location", "swagger-ui.html#");
    }
}
