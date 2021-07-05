package com.ch.excel_send.controller;

import com.ch.excel_send.excel.ReadExcel;
import com.ch.excel_send.excel.WriteExcel;
import com.ch.excel_send.phone.DistrictHelper;
import com.ch.excel_send.tool.MultipartFileToFile;
import com.ch.resource_seore.common.CommonObject;
import com.ch.resource_seore.service.PathHandleService;
import com.ch.resource_seore.setting.RemoteProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import jxl.read.biff.BiffException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Controller
@Api(value = "excel", description = "所有api说明", tags = "excel")
public class ExcelController {


    @RequestMapping(value = "/excelSend")
    @ResponseBody
    @ApiOperation(value = "上传excel文件,根据参数处理，返回处理后的文件")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "sendCol", value = "需要转换的列"),
            @ApiImplicitParam(name = "col", value = "转换到第几列"),
            @ApiImplicitParam(name = "row", value = "从多少行开始转换"),
            @ApiImplicitParam(name = "file", type = "MultipartFile", dataType = "MultipartFile",
                    value = "保存的文件，MultipartFile 作为接收参数", required = true)})
    public Object upload(@RequestParam("file") MultipartFile file,
                         @RequestParam(required = false) int sendCol,
                         @RequestParam(required = false) int col,
                         @RequestParam(required = false) int row) {
        try {
            jxl.Workbook wb = null;
            File file1 = MultipartFileToFile.multipartFileToFile(file);
            try {
                wb = jxl.Workbook.getWorkbook(file1);
            } catch (IOException e) {
                e.printStackTrace();
                return "IOException";
            } catch (BiffException e) {
                e.printStackTrace();
                return "BiffException";
            }
            jxl.Sheet sheet = wb.getSheet(0);
            List<String> list = ReadExcel.readByColumns(sheet, sendCol);
            List<String> collect = list.stream().map(s -> DistrictHelper.ofTelNumber(s)).collect(Collectors.toList());

            WriteExcel.updateExcel(collect, col, row, file1);
            return export(file1);
        } catch (IOException e) {
            e.printStackTrace();
            return "IOException";
        } catch (Exception e) {
            e.printStackTrace();
            return "转换错误";
        }
    }

    @RequestMapping(value = "/testc")
    public void upload(@RequestParam("file") MultipartFile file) {

        System.out.println(file.getOriginalFilename());
    }

    /**
     * 下载文件处理方法
     *
     * @param file
     * @return
     */
    private ResponseEntity<FileSystemResource> export(File file) {
        if (file == null) {
            return null;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", "attachment; filename=" + file.getName());
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
