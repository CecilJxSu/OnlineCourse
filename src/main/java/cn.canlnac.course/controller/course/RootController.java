package cn.canlnac.course.controller.course;

import cn.canlnac.course.entity.Catalog;
import cn.canlnac.course.entity.Course;
import cn.canlnac.course.entity.Document;
import cn.canlnac.course.service.CatalogService;
import cn.canlnac.course.service.CourseService;
import cn.canlnac.course.service.DocumentService;
import cn.canlnac.course.util.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 课程
 */
@Component("CourseRootController")
@RestController
public class RootController {
    @Autowired
    CourseService courseService;

    @Autowired
    DocumentService documentService;

    @Autowired
    CatalogService catalogService;

    @Autowired
    JWT jwt;

    /**
     * 创建课程
     * @param Authentication    登录信息
     * @param body              请求数据
     * @return                  课程ID
     */
    @PostMapping("/course")
    public ResponseEntity<Map<String, Object>> create(
            @RequestHeader(value="Authentication", required = false) String Authentication,
            @RequestBody Map<String, Object> body
    ) {
        //未登录
        Map<String, Object> auth;
        if (Authentication == null || (auth = jwt.decode(Authentication)) == null) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        //登录用户不是老师或管理员
        if(!Arrays.asList("teacher","admin").contains(auth.get("userStatus").toString())) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        //参数验证
        if(body.get("name") == null || body.get("name").toString().isEmpty() ||
            body.get("introduction") == null || body.get("introduction").toString().isEmpty() ||
            body.get("department") == null || body.get("department").toString().isEmpty()
        ) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        Course course = new Course();   //课程
        course.setName(body.get("name").toString());
        course.setIntroduction(body.get("introduction").toString());
        course.setDepartment(body.get("department").toString());
        course.setStatus("draft");
        course.setUserId(Integer.parseInt(auth.get("id").toString()));

        int courseId = courseService.create(course);    //创建课程

        //创建返回数据
        HashMap<String,Object> sendData = new HashMap();
        sendData.put("courseId", courseId);

        //返回课程ID
        return new ResponseEntity(sendData, HttpStatus.OK);
    }
}