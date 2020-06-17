package com.hh.resource_seore.service.imp;

import com.hh.resource_seore.util.CreateBasicData;
import com.hh.resource_seore.util.FileTools;
import com.hh.resource_seore.common.CommonObject;
import com.hh.resource_seore.service.PathHandleService;
import com.hh.resource_seore.setting.RemoteProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;


/**
 * hash表存储文件结构
 */
@Component
public class PathHashHandle implements PathHandleService {

    @Autowired
    private RemoteProperties remoteProperties;


    private final String DEFAULT_PROJECT = "default_project";


    @Override
    public String storage(byte[] file, String project, String fileName) throws IOException {

        String treePath = CreateBasicData.getRandomString(12);// api访问地址，也是文件树里面的路径
        String filepath = filePathRule(project,fileName);//文件路径

        fileStorage(filepath,fileName,file);

        Map<String, String> map = CommonObject.getFiles();//从全局获取hash表
        if (map.get(treePath) != null) {
            return storage(file, project, fileName);
        }
        map.put(treePath, filepath + "/" + fileName);
        return treePath;
    }

    @Override
    public File out(String code) throws FileNotFoundException {
        Map<String, String> map = CommonObject.getFiles();//从全局获取hash表
        String path = map.get(code);
        if (path == null) {
            return null;
        }
       return new File(path);

    }

    /**
     * 文件存储至本地
     */
    private void fileStorage(String filepath,String fileName, byte[] file) throws IOException {
        File file1 = new File(filepath.toString());
        if (!file1.exists()) {
            file1.mkdirs();
        }
        FileTools.byte2file(file, filepath + "/" + fileName);
    }

    /**
     * 默认文件路径的实现
     */
    private String setFilePath(String project, String fileName) {
        StringBuilder filepath = new StringBuilder(); //文件真实存放地址
        if (project == null) {
            project = this.DEFAULT_PROJECT;
        }
        filepath.append(remoteProperties.getPath()).append("/").append(project);
        Calendar today = new GregorianCalendar();
        String type = fileName.split("\\.")[1];
        filepath.append("/").append(type);
        filepath.append("/").append(today.get(Calendar.YEAR)).append("-").append(today.get(Calendar.MONTH) + 1)
                .append("/").append(today.get(Calendar.DATE));
        return filepath.toString();
    }


    /**
     * 文件命名规则，可以继承此类重写此方法自定义规则
     * 默认文件路径: 本地资源包/项目/文件类型/年-月/日/文件名
     *
     * @param project
     * @param fileName
     * @return
     */
    public String filePathRule(String project, String fileName) {
        return setFilePath(project, fileName);
    }
}
