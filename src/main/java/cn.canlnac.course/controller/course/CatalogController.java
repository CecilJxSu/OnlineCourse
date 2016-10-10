package cn.canlnac.course.controller.course;

import cn.canlnac.course.entity.Catalog;
import cn.canlnac.course.entity.Course;
import cn.canlnac.course.service.CatalogService;
import cn.canlnac.course.service.CourseService;
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
 * 课程下的章节
 */
@Component("CatalogController")
@RestController
public class CatalogController {
    @Autowired
    CourseService courseService;

    @Autowired
    CatalogService catalogService;

    @Autowired
    ProfileService profileService;

    @Autowired
    JWT jwt;

    /**
     * 创建课程章节
     * @param Authentication    登录信息
     * @param body              请求数据
     * @param courseId          课程ID
     * @return                  章节ID
     */
    @PostMapping("/course/{courseId}/catalog")
    public ResponseEntity create(
            @RequestHeader(value="Authentication", required = false) String Authentication,
            @RequestBody Map<String, Object> body,
            @PathVariable int courseId
    ) {
        //未登录
        Map<String, Object> auth;
        if (Authentication == null || (auth = jwt.decode(Authentication)) == null) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        //获取课程
        Course course = courseService.findByID(courseId);
        if(course == null){ //课程不存在
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        //不是管理员而且课程不属于自己的
        if (!auth.get("userStatus").equals("admin") && course.getUserId() != (int)auth.get("id")){
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        //检查参数
        if(body.get("index") == null || body.get("name") == null || body.get("introduction") == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        if(body.get("url") != null && (body.get("duration") == null || body.get("previewImage") == null)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        //获取所有章节
        Catalog[] catalogs = catalogService.getList(courseId).toArray(new Catalog[0]);
        List<Integer> indexes = new ArrayList<>();
        for (Catalog catalog:catalogs){
            indexes.add(catalog.getIndex());
        }

        //检查index是否冲突
        if(indexes.contains(Integer.parseInt(body.get("index").toString()))){
            return new ResponseEntity(HttpStatus.CONFLICT);
        }

        //创建章节
        Catalog catalog = new Catalog();
        //设置章节数据
        catalog.setIntroduction(body.get("introduction").toString());
        catalog.setName(body.get("name").toString());
        catalog.setCourseId(courseId);
        catalog.setIndex(Integer.parseInt(body.get("index").toString()));
        //设置视频源
        if(body.get("url") != null){
            catalog.setUrl(body.get("url").toString());
            catalog.setDuration(Integer.parseInt(body.get("duration").toString()));
            catalog.setPreviewImage(body.get("previewImage").toString());
        }

        int catalogId = catalogService.create(catalog);    //创建章节
        if(catalogId<1){    //创建失败
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //创建返回数据
        HashMap<String,Object> sendData = new HashMap();
        sendData.put("catalogId", catalog.getId());

        //返回章节ID
        return new ResponseEntity(sendData, HttpStatus.OK);
    }

    /**
     * 获取课程下的章节列表
     * @param Authentication    登录信息
     * @param courseId          课程ID
     * @return                  章节列表
     */
    @GetMapping("/course/{courseId}/catalogs")
    public ResponseEntity getList(
            @RequestHeader(value="Authentication", required = false) String Authentication,
            @PathVariable int courseId
    ) {
        //未登录
        Map<String, Object> auth;
        if (Authentication == null || (auth = jwt.decode(Authentication)) == null) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        //获取章节列表
        List<Catalog> catalogs = catalogService.getList(courseId);

        if (catalogs.size() < 1){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        //返回章节列表
        return new ResponseEntity(catalogs, HttpStatus.OK);
    }
}