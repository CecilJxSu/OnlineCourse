package cn.canlnac.course.controller.user;

import cn.canlnac.course.entity.Catalog;
import cn.canlnac.course.entity.Course;
import cn.canlnac.course.entity.LearnRecord;
import cn.canlnac.course.entity.Profile;
import cn.canlnac.course.service.CatalogService;
import cn.canlnac.course.service.CourseService;
import cn.canlnac.course.service.LearnRecordService;
import cn.canlnac.course.service.ProfileService;
import cn.canlnac.course.util.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户的学习记录
 */
@Component("UserLearnRecordController")
@RestController
public class LearnRecordController {
    @Autowired
    private LearnRecordService learnRecordService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CatalogService catalogService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private JWT jwt;

    /**
     * 获取用户学习记录
     * @param Authentication    登录信息
     * @param userId            用户ID
     * @param start             分页开始位置
     * @param count             分页返回数目
     * @return                  学习记录
     */
    @GetMapping("/user/{userId}/learnRecord")
    public ResponseEntity getLearnRecord(
            @RequestHeader(value="Authentication", required = false) String Authentication,
            @PathVariable int userId,
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "10") int count
    ) {
        //未登录
        Map auth;
        if (Authentication == null || (auth = jwt.decode(Authentication)) == null) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        //处理start和count的大小关系
        if (start>count){
            int temp = start;
            start=count;
            count=temp;
        }

        int total = learnRecordService.count(userId);   //统计学习记录数
        if(total < 1) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        LearnRecord[] learnRecords;  //用户的学习记录

        //登录用户！= userId
        if(userId != Integer.parseInt(auth.get("id").toString())){
            if(!auth.get("userStatus").equals("teacher")) { //不是老师
                return new ResponseEntity(HttpStatus.FORBIDDEN);
            }

            List catalogIds = new ArrayList(); //所有章节ID
            //获取作者的课程
            Course[] courses = courseService.findByUserId(Integer.parseInt(auth.get("id").toString())).toArray(new Course[0]);
            //遍历课程
            for (Course course: courses){
                //获取课程的章节
                Catalog[] catalogs = catalogService.getList(course.getId()).toArray(new Catalog[0]);
                //遍历章节
                for (Catalog catalog:catalogs){
                    catalogIds.add(catalog.getId());    //添加章节ID
                }
            }

            List<LearnRecord> learnRecordList = new ArrayList();    //用户的学习记录

            //获取用户的所有学习记录
            LearnRecord[] allLearnRecords = learnRecordService.getLearnRecords(0,total,userId).toArray(new LearnRecord[0]);
            //过滤，返回与老师有关的学习记录
            for (LearnRecord learnRecord:allLearnRecords){
                if(catalogIds.contains(learnRecord.getCatalogId())){
                    learnRecordList.add(learnRecord);
                }
            }

            total = learnRecordList.size(); //新的大小
            if(total < 1 || start >= total) {
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }

            int end = (start+count>total)?total:start+count;
            //裁剪
            learnRecords = learnRecordList.subList(start, end).toArray(new LearnRecord[0]);
        } else {
            learnRecords = learnRecordService.getLearnRecords(start,count,userId).toArray(new LearnRecord[0]);
        }

        List learnRecordsArray = new ArrayList();   //返回的学习记录
        //遍历用户的学习记录
        for (LearnRecord learnRecord:learnRecords){
            Map learnRecordObj = new HashMap();

            learnRecordObj.put("id",learnRecord.getId());
            learnRecordObj.put("date",learnRecord.getDate());

            //设置catalog
            Catalog catalog = catalogService.findByID(learnRecord.getCatalogId());
            Map catalogObj = new HashMap();
            catalogObj.put("id",catalog.getId());
            catalogObj.put("name",catalog.getName());
            learnRecordObj.put("catalog",catalogObj);


            //设置用户资料
            Profile profile = profileService.findByUserID(learnRecord.getUserId());
            Map profileObj = new HashMap();
            profileObj.put("id",profile.getUserId());
            profileObj.put("name",profile.getNickname());
            profileObj.put("iconUrl",profile.getIconUrl());
            learnRecordObj.put("user",profileObj);

            learnRecordObj.put("progress",learnRecord.getProgress());
            learnRecordObj.put("lastDate",learnRecord.getLastDate());
            learnRecordObj.put("lastPosition",learnRecord.getLastPosition());

            learnRecordsArray.add(learnRecordObj);
        }

        //创建返回数据
        HashMap sendData = new HashMap();
        sendData.put("total",total);
        sendData.put("learnRecords",learnRecordsArray);

        //返回用户学习记录
        return new ResponseEntity(sendData, HttpStatus.OK);
    }
}