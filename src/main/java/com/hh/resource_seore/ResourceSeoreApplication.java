package com.hh.resource_seore;

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
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
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


    @RequestMapping(value = "/uploadFile")
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

    private class PathInfo extends HashMap<String, Map> {
        String storePath;
        String returnPath;
        //是否是目录 如果是目录就相当于是普通的map 否则，记录文件信息
        boolean isDirectory;

        public String getStorePath() {
            return storePath;
        }

        public void setStorePath(String storePath) {
            this.storePath = storePath;
        }

        public String getReturnPath() {
            return returnPath;
        }

        public void setReturnPath(String returnPath) {
            this.returnPath = returnPath;
        }

        public PathInfo(String storePath, String returnPath) {
            this(storePath, returnPath, false);
        }

        public PathInfo() {
            this.isDirectory = true;
        }

        private PathInfo(String storePath, String returnPath, boolean isDirectory) {
            this.returnPath = returnPath;
            this.storePath = storePath;
            this.isDirectory = isDirectory;
        }

        @Override
        public String toString() {
            if (!this.isDirectory) {
                return this.returnPath;
            }
            return super.toString();
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
            @ApiImplicitParam(name = "fileName", value = "文件名称，用于确认保存的文件名称及文件类型",
                    required = true),
            @ApiImplicitParam(name = "filePath", value = "保存的文件，String 传输被保存资源的路径",
                    required = true)})
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

    @Autowired
    private RemoteProperties remoteProperties;

    private Set<String> list = new HashSet<>();

    @RequestMapping(value = "/test")
    @ResponseBody
    public PathInfo test(@RequestParam("file") MultipartFile file) throws IOException {
        return storage(file.getBytes(), "test", file.getOriginalFilename());
    }

    @RequestMapping(value = "/test1")
    @ResponseBody
    public PathInfo test1() throws IOException {
        return this.fileTree;
    }

    private PathInfo fileTree = new PathInfo();

    private PathInfo storage(byte[] file, String project, String fileName) throws IOException {
        StringBuilder filepath = new StringBuilder(), //文件真实存放地址
                treePath = new StringBuilder();// api访问地址，也是文件树里面的路径
        filepath.append(remoteProperties.getPath()).append("/").append(project);

        treePath.append("/").append(project);
        Date today = new Date();

        filepath.append("/").append(today.getYear());
        treePath.append("/").append(today.getYear()).append("-").append(today.getMonth())
                .append("/").append(today.getDay());

        filepath.append("/").append(today.getMonth());
        filepath.append("/").append(today.getDay());

        File file1 = new File(filepath.toString());
        if (!file1.exists()) {
            file1.mkdirs();
        }
        treePath.append("/").append(CreateBasicData.getRandomString(12));
        PathInfo pathInfo = new PathInfo(filepath.toString(), treePath.toString());
        String[] tier = treePath.toString().trim().split("/");
        Map<String, Map> tierMap1 = this.fileTree;
        for (int i = 1; i < tier.length - 1; i++) {
            Map tierMap = tierMap1.get(tier[i]);
            if (tierMap == null) {
                tierMap = new HashMap<>();
            }
            tierMap1.put(tier[i], tierMap);
            tierMap1 = tierMap;
        }

        tierMap1.put(fileName, pathInfo);


        FileTools.byte2file(file, filepath.append("/").append(fileName).toString());
        System.out.println(pathInfo);
        return pathInfo;
    }
}
