package com.ch.resource_seore.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 公共全局对象，维护了两个数据结构用于存储文件信息
 * @author chen
 */
public class CommonObject {


    private static Map<String, String> files = new HashMap<>();

    public static Map<String, String> getFiles() {
        return files;
    }
}
