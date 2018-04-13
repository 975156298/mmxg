package com.shangdao.controller;

import com.shangdao.util.ResponseObj;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@RestController
public class UploadController {
    @Value("${uploadpath}")
    String uploadPath;
    @PostMapping("/upload")
    public ResponseObj upload(@RequestParam("file") MultipartFile file) throws IOException {
        String[] fileNames = file.getOriginalFilename().split("\\.");
        String newFileName;
        if(fileNames.length==2){
            newFileName=UUID.randomUUID().toString()+"."+fileNames[1];
        }else {
            newFileName =UUID.randomUUID().toString();
        }
        String path=datePath()+"/"+newFileName;
        FileUtils.writeByteArrayToFile(new File(uploadPath+path), file.getBytes());
        return new ResponseObj(new FileInfo(file.getOriginalFilename(),newFileName,file.getContentType(),file.getSize(),path));
    }

    private class FileInfo{
        private String originalFilename;
        private String newFilename;
        private String contentType;
        private Long size;
        private String path;

        public FileInfo(String originalFilename, String newFilename, String contentType, Long size,String path) {
            this.originalFilename = originalFilename;
            this.newFilename = newFilename;
            this.contentType = contentType;
            this.size = size;
            this.path=path;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getOriginalFilename() {
            return originalFilename;
        }

        public void setOriginalFilename(String originalFilename) {
            this.originalFilename = originalFilename;
        }

        public String getNewFilename() {
            return newFilename;
        }

        public void setNewFilename(String newFilename) {
            this.newFilename = newFilename;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        public Long getSize() {
            return size;
        }

        public void setSize(Long size) {
            this.size = size;
        }
    }
    private String datePath(){
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyyMM");
        return dateFormat.format(new Date());
    }
}
