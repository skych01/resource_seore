package com.hh.resource_seore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;

public class DownloadImg {


    /**
     * 下载图片并保存
     * @param urlString
     * @param filename
     * @param wjjmc
     * @throws Exception
     */
    public static void download(String urlString, String filename,String wjjmc) throws Exception {

        File file =new File(wjjmc);
        if  (!file .exists()  && !file .isDirectory())
        {
            //  System.out.println("//不存在");
            file .mkdirs();
        }
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
        //System.out.println("download xml start.......");
        URL url = new URL(urlString);
        URLConnection con = url.openConnection();
        InputStream is = con.getInputStream();


        byte[] bs = new byte[1024];
        int len;
        OutputStream os = new FileOutputStream(filename);
        while ((len = is.read(bs)) != -1) {
            os.write(bs, 0, len);
        }
        os.close();
        is.close();
        //System.out.println("download xml over.......");
    }





}
