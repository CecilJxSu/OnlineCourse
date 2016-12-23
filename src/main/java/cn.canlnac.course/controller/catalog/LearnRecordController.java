package cn.canlnac.course.controller.catalog;

import cn.canlnac.course.entity.Catalog;
import cn.canlnac.course.entity.LearnRecord;
import cn.canlnac.course.service.CatalogService;
import cn.canlnac.course.service.LearnRecordService;
import cn.canlnac.course.util.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 章节下的学习记录
 */
@Component("CatalogLearnRecordController")
@RestController
public class LearnRecordController {
    @Autowired
    CatalogService catalogService;

    @Autowired
    LearnRecordService learnRecordService;

    @Autowired
    private JWT jwt;

    /**
     * 创建自己的学习记录
     * @param Authentication    登录信息
     * @param catalogId         章节ID
     * @return                  学习记录ID
     */
    @PostMapping("/catalog/{catalogId}/learnRecord")
    public ResponseEntity createLearnRecord(
            @RequestHeader(value="Authentication", required = false) String Authentication,
            @RequestBody Map body,
            @PathVariable int catalogId
    ) {
        //未登录
        Map auth;
        if (Authentication == null || (auth = jwt.decode(Authentication)) == null) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        //检查参数
        if (body.get("progress") == null || body.get("lastPosition") == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        //获取章节
        Catalog catalog = catalogService.findByID(catalogId);
        if (catalog == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        //已经存在学习记录
        if (learnRecordService.getLearnRecord(catalogId,Integer.parseInt(auth.get("id").toString())) != null){
            return new ResponseEntity(HttpStatus.CONFLICT);
        }

        //创建学习记录
        LearnRecord learnRecord = new LearnRecord();
        learnRecord.setUserId(Integer.parseInt(auth.get("id").toString()));
        learnRecord.setCatalogId(catalogId);
        learnRecord.setLastPosition(Integer.parseInt(body.get("lastPosition").toString()));
        learnRecord.setProgress(Double.parseDouble(body.get("progress").toString()));

        int createdCount = learnRecordService.create(learnRecord);  //创建学习记录
        if(createdCount != 1){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Map sendData = new HashMap();   //创建返回对象
        sendData.put("learnRecordID",learnRecord.getId());

        //返回学习记录ID
        return new ResponseEntity(sendData,HttpStatus.OK);
    }

    /**
     * 更新自己的学习记录
     * @param Authentication    登录信息
     * @param catalogId         章节ID
     * @return                  空对象
     */
    @PutMapping("/catalog/{catalogId}/learnRecord")
    public ResponseEntity updateLearnRecord(
            @RequestHeader(value="Authentication", required = false) String Authentication,
            @RequestBody Map body,
            @PathVariable int catalogId
    ) {
        //未登录
        Map auth;
        if (Authentication == null || (auth = jwt.decode(Authentication)) == null) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        //检查参数
        if (body.get("progress") == null || body.get("lastPosition") == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        //获取学习记录
        LearnRecord learnRecord = learnRecordService.getLearnRecord(catalogId,Integer.parseInt(auth.get("id").toString()));
        //不存在学习记录
        if (learnRecord == null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        //更新学习记录
        learnRecord.setLastPosition(Integer.parseInt(body.get("lastPosition").toString()));
        learnRecord.setProgress(Double.parseDouble(body.get("progress").toString()));

        int updatedCount = learnRecordService.update(learnRecord);  //更新学习记录
        if(updatedCount != 1){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //返回空对象
        return new ResponseEntity(new HashMap(),HttpStatus.OK);
    }

    /**
     * 获取自己的学习记录
     * @param Authentication    登录信息
     * @param catalogId         章节ID
     * @return                  返回学习记录
     */
    @GetMapping("/catalog/{catalogId}/learnRecord")
    public ResponseEntity getLearnRecord(
            @RequestHeader(value="Authentication", required = false) String Authentication,
            @PathVariable int catalogId
    ) {
        //未登录
        Map auth;
        if (Authentication == null || (auth = jwt.decode(Authentication)) == null) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        //获取学习记录
        LearnRecord learnRecord = learnRecordService.getLearnRecord(catalogId,Integer.parseInt(auth.get("id").toString()));
        //不存在学习记录
        if (learnRecord == null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        Map sendData = new HashMap();   //创建返回对象
        sendData.put("id",learnRecord.getId());
        sendData.put("date",learnRecord.getDate());
        sendData.put("catalogId",learnRecord.getCatalogId());

        //设置用户信息
        Map user = new HashMap();
        user.put("id",auth.get("id"));
        user.put("name",auth.get("nickname"));
        user.put("iconUrl",auth.get("iconUrl"));
        sendData.put("user",user);

        sendData.put("progress",learnRecord.getProgress());
        sendData.put("lastDate",learnRecord.getLastDate());
        sendData.put("lastPosition",learnRecord.getLastPosition());

        //返回学习记录
        return new ResponseEntity(sendData,HttpStatus.OK);
    }
}