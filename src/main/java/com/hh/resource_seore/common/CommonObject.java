package com.hh.resource_seore.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 公共全局对象，维护了两个数据结构用于存储文件信息
 * @author chen
 */
public class CommonObject {

    private static PathInfo fileTree = new PathInfo();

    private static Map<String, String> files = new HashMap<>();

    public static PathInfo getFileTree() {
        return fileTree;
    }

    public static Map<String, String> getFiles() {
        return files;
    }
}
