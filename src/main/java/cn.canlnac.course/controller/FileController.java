package cn.canlnac.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.activation.FileDataSource;

import cn.canlnac.course.util.JWT;

//import org.springframework.core.io.Resource;

/**
 * 文件操作.
 */
@Component
@RestController
public class FileController {
    @Autowired
    private JWT jwt;

    /**
     * 创建新的文件名
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String createFileName() throws NoSuchAlgorithmException {
        //创建新文件名
        String uuid = UUID.randomUUID().toString();
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(uuid.getBytes());
        uuid = String.format("%032X", new BigInteger(1, messageDigest.digest()));

        return uuid;
    }

    /**
     * 获取文件目录储存的地方
     * @return
     */
    public static String getSourcesDirectory(){
        String SourcesDirectory = "/usr/local/tomcat/webapps/files/";//System.getProperty("user.dir")+"/uploadFiles/";
        File file = new File(SourcesDirectory);
        if  (!file .exists()  && !file.isDirectory()) {
            //目录不存在，创建
            file .mkdir();
        }
        return SourcesDirectory;
    }

    /**
     * 上传文件，可以多文件上传
     * @param Authentication    登录信息
     * @param files             文件列表
     * @return
     */
    @PostMapping("/file")
    public ResponseEntity<List<Map<String,Object>>> uploadFile(
            @RequestHeader(value="Authentication", required = false) String Authentication,
            @RequestParam(value = "file") MultipartFile[] files
    ) {
        /*//未登录
        if (Authentication == null || jwt.decode(Authentication) == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }*/

        //参数错误
        if (files == null || files.length == 0 || files[0].getSize() == 0) {
            return ResponseEntity.badRequest().body(null);
        }

        //返回数据
        List<Map<String,Object>> sendDataList = new ArrayList<>();

        for (MultipartFile file:files) {
            Map<String,Object> sendData = new HashMap<>();

            String fileName;
            try {
                fileName = createFileName();
                file.transferTo(new File(getSourcesDirectory() + fileName));
            } catch (NoSuchAlgorithmException e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            } catch (IOException e) {
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            sendData.put("fileType", file.getContentType());
            sendData.put("fileSize", file.getSize());
            sendData.put("fileName", fileName);

            sendDataList.add(sendData);
        }

        //返回成功
        return ResponseEntity.ok().body(sendDataList);
    }

    /**
     * 获取文件
     /* @param Authentication    登录信息
     * @param name              文件名
     * @return
     */
    @GetMapping("/file/{name}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(
            /*@RequestHeader(value="Authentication", required = false) String Authentication,*/
            @PathVariable String name
    ) {
        /*//未登录
        if (Authentication == null || jwt.decode(Authentication) == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }*/

        //参数错误
        if (name == null || name.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        //获取文件
        File file = new File(getSourcesDirectory() + name);

        //文件不存在
        if (!file.exists() || file.isDirectory()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Resource resource = new FileSystemResource(file);

        String contentType;
        try {
            FileDataSource fds = new FileDataSource(file);
            contentType = fds.getContentType();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //返回成功
        return ResponseEntity.ok().header("Content-Type",contentType).body(resource);
    }
}
