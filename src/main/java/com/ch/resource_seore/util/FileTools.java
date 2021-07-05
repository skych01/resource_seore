package com.ch.resource_seore.util;


import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.Date;


public class FileTools {

    /**
     * 获取图片宽度
     *
     * @param fileInputStream 图片流
     * @return 宽度
     */
    public static int getImgWidth(InputStream fileInputStream) {
        InputStream is = fileInputStream;
        BufferedImage src = null;
        int ret = -1;
        try {
            src = javax.imageio.ImageIO.read(is);
            ret = src.getWidth(null); // 得到源图宽
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }


    /**
     * 获取图片高度
     *
     * @param fileInputStream 图片流
     * @return 高度
     */
    public static int getImgHeight(InputStream fileInputStream) {
        InputStream is = fileInputStream;
        BufferedImage src = null;
        int ret = -1;
        try {
            src = javax.imageio.ImageIO.read(is);
            ret = src.getHeight(null); // 得到源图高
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static String getAppPath(HttpServletRequest request) {
        String path = request.getServletContext().getRealPath("/");
        return path;
//        if (path.endsWith("/")) {
//            return path;
//        } else {
//            return path + "/";
//        }
    }

    public static String getDomain(HttpServletRequest request) {
        StringBuffer url = request.getRequestURL();
        String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).append("/").toString();
        return tempContextUrl;
    }

    public static String getDomainNotPath(HttpServletRequest request) {
        StringBuffer url = request.getRequestURL();
        String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).toString();
        return tempContextUrl;
    }


    /**
     * 判断是否是图片
     *
     * @param name
     * @return
     */
    public static boolean isPicture(String name) {
        return name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png") || name.endsWith(".gif");
    }


    /**
     * 将数据写到某文件下
     *
     * @param filedata
     * @param file
     * @throws IOException
     */

    public static void writeTo(byte[] filedata, File file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        try {
            fos.write(filedata);
        } finally {
            fos.close();
        }
    }

    /**
     * 根据文件信息 存放文件，返回访问该文件的url
     *
     * @param fileName
     * @param uploadTime
     * @param b
     * @param request
     * @return
     * @throws IOException
     */
    public static String dispatchInfoImg(String id, String fileName, String serviceType, Date uploadTime, byte[] b, String domain,
                                         HttpServletRequest request) throws IOException {
        String filePath = "";
        StringBuilder sb = new StringBuilder();
        sb.append(getAppPath(request));
        sb.append("temp/");
        sb.append(serviceType + "/");
        sb.append(id + "/");
        String path = sb.toString();
        File file = new File(path);
        if (!file.exists()) file.mkdirs();
        fileName = id + "_" + uploadTime.getTime() + fileName;
        file = new File(path + fileName);
        if (file.exists()) {
            if (uploadTime.getTime() > file.lastModified()) {
                file.delete();
                writeTo(b, file);
            }
        } else {
            if (b != null) {
                writeTo(b, file);
            }
        }
        if (file.exists()) {
            fileName = URLEncoder.encode(fileName, "utf-8");
            filePath = getDomainNotPath(request) + domain + "/temp/" + serviceType + "/" + id + "/" + fileName;
        }
        return filePath;
    }

    public static String dispatchInfoImg(int id, String fileName, String serviceType, Date uploadTime, byte[] b, String domain,
                                         HttpServletRequest request) throws IOException {
        return dispatchInfoImg(id + "", fileName, serviceType, uploadTime, b, domain, request);
    }

    public static byte[] file2byte(File file) throws IOException {

        InputStream input = new FileInputStream(file);

        byte[] byt = new byte[input.available()];

        input.read(byt);
        return byt;
//        InputStream input = new FileInputStream(file);
//        input.read(byt);
//        return new byte[input.available()];
    }

    public static File byte2file(byte[] bytes, String fileName) throws IOException {
        File file = new File(fileName);
        OutputStream output = new FileOutputStream(file);
        BufferedOutputStream bufferedOutput = new BufferedOutputStream(output);
        bufferedOutput.write(bytes);
        bufferedOutput.flush();
        bufferedOutput.close();
        return file;
    }

    public static void download(String url, String filePath) throws Exception {

        File file = new File(filePath);
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
        URL url1 = new URL(url);
        URLConnection con = url1.openConnection();
        InputStream is = con.getInputStream();


        byte[] bs = new byte[1024];
        int len;
        OutputStream os = new FileOutputStream(file);
        while ((len = is.read(bs)) != -1) {
            os.write(bs, 0, len);
        }
        os.close();
        is.close();
    }
}
