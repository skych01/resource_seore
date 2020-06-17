package com.hh.resource_seore.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * 自己封装的用于请求url的工具
 */
public class RequestUtil {
    /**
     * 调用api head头信息
     */
    private static HttpHeaders headers = new HttpHeaders();

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(RequestUtil.class);

    /**
     * 调用api的工具
     */
    private static RestTemplate restTemplate;

    /**
     * 初始化 调用api工具
     */
    static {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        restTemplate = new RestTemplate(factory);
        MediaType type = MediaType.parseMediaType("multipart/form-data");
        headers.setContentType(type);
//        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
    }

    /**
     * get请求
     */
    public static Result getRequest(String url,
                                    Map<String, Object> bodyParam,
                                    Map<String, String> heardParam) {
        StringBuilder urlAndParam = new StringBuilder();
        urlAndParam.append(url);

        if (bodyParam != null && bodyParam.size() != 0) {
            urlAndParam.append("?");
            for (String param : bodyParam.keySet()) {
                urlAndParam.append(param);
                urlAndParam.append("=");
                urlAndParam.append(bodyParam.get(param));
                urlAndParam.append("&");
            }
        }
        if (heardParam != null) {
            for (String param : heardParam.keySet()) {
                headers.add(param, heardParam.get(param));
            }
        }

        logger.info("调用接口中url=  " + url + "。  参数=" + bodyParam);
        logger.info("调用接口请求头=" + headers);

        long start = System.nanoTime();

        try {
            FileSystemResource fileSystemResource = new FileSystemResource("C:\\Users\\陈鸿\\Desktop\\平台问题总结.docx");
            MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
            form.add("file", fileSystemResource);
            form.add("fileName", "平台问题总结.docx");
            HttpEntity<MultiValueMap<String, Object>> param = new HttpEntity<>(form, headers);

            ResponseEntity<byte[]> responseEntity = restTemplate.exchange(urlAndParam.toString(),
                    HttpMethod.POST, param, byte[].class);


            long end = System.nanoTime();
            logger.info("调用成功!    响应时间：" + (end - start) / 1000000 + "毫秒");
            HttpHeaders headers = responseEntity.getHeaders();
            String body;
            HttpStatus httpStatus = responseEntity.getStatusCode();
            if (headers.containsKey("Content-Encoding")) {
                if (headers.get("Content-Encoding").contains("gzip")) {
                    GZIPInputStream gzip = null;
                    try {
                        gzip = new GZIPInputStream(new ByteArrayInputStream(responseEntity.getBody()));
                        BufferedReader reader = new BufferedReader(new InputStreamReader(gzip));
                        StringWriter writer = new StringWriter();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            writer.write(line);
                        }
                        body = writer.toString();
                        return Result.getResult(body, headers, httpStatus);
                    } catch (IOException e) {
                        e.printStackTrace();
                        body = null;
                    }
                }
            }
            body = new String(responseEntity.getBody());
            return Result.getResult(body, headers, httpStatus);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JsonNode ResponseEntityFormat4JSON(String body) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readTree(body);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Document ResponseEntityFormat4Html(String body) {
        return Jsoup.parse(body);
    }

    public static Result getRequest(String url) {
        return getRequest(url, null, null);
    }

    public static Result getRequest(String url, Map<String, Object> bodyParam) {
        return getRequest(url, bodyParam, null);
    }

    public static void request() {

    }

    public static void main(String[] args) {
        Map map = new HashMap();
        map.put("fileName", "平台问题总结.docx");
        getRequest("http://127.0.0.1:8081/test");
        System.out.println(getRequest("http://127.0.0.1:8081/test").body);
    }

    public static class Result {
        private String body;
        private HttpHeaders heards;
        private HttpStatus httpStatus;

        public String getBody() {
            return body;
        }

        public HttpHeaders getHeards() {
            return heards;
        }

        public HttpStatus getHttpStatus() {
            return httpStatus;
        }

        public int getHttpStatusValue() {
            return httpStatus.value();
        }

        private Result(String body, HttpHeaders heards, HttpStatus httpStatus) {
            this.body = body;
            this.heards = heards;
            this.httpStatus = httpStatus;
        }

        private static Result getResult(String body, HttpHeaders heards, HttpStatus httpStatus) {
            return new Result(body, heards, httpStatus);
        }

    }


}
