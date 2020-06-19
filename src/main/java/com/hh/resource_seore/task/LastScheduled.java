package com.hh.resource_seore.task;

import com.hh.resource_seore.common.CommonObject;
import com.hh.resource_seore.setting.RemoteProperties;
import com.hh.resource_seore.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * 定时对资源文件进行持久化
 */
@Component
@EnableScheduling
public class LastScheduled {

    @Autowired
    private RemoteProperties remoteProperties;

    @Autowired
    private RedisUtils redisUtils;


    @Scheduled(cron = "0 0 */1 * * ?")
    public void last() throws IOException {

        File f = new File(remoteProperties.getPath() + "/file.properties");
        if (!f.exists()) {
            f.createNewFile();
        }
        FileWriter fileWritter = new FileWriter(f);
        for (Map.Entry<String, String> entry : CommonObject.getFiles().entrySet()) {
            fileWritter.write(entry.getKey() + ":" + entry.getValue()+"\n");
        }
        fileWritter.close();
    }

}
