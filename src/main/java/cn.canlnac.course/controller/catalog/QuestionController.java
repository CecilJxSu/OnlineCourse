package cn.canlnac.course.controller.catalog;

import cn.canlnac.course.entity.Catalog;
import cn.canlnac.course.entity.Course;
import cn.canlnac.course.entity.Question;
import cn.canlnac.course.service.CatalogService;
import cn.canlnac.course.service.CourseService;
import cn.canlnac.course.service.QuestionService;
import cn.canlnac.course.util.JWT;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 章节下的小测
 */
@Component
@RestController
public class QuestionController {
    @Autowired
    CourseService courseService;

    @Autowired
    CatalogService catalogService;

    @Autowired
    QuestionService questionService;
    
    @Autowired
    private JWT jwt;

    /**
     * 创建章节的小测
     * @param Authentication    登录信息
     * @param catalogId         章节ID
     * @return                  小测ID
     */
    @PostMapping("/catalog/{catalogId}/question")
    public ResponseEntity createQuestion(
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
        if (body.get("questions") == null || body.get("total") == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        //获取章节
        Catalog catalog = catalogService.findByID(catalogId);
        if (catalog == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        //获取课程
        Course course = courseService.findByID(catalog.getCourseId());

        //不是管理员而且章节不属于自己的
        if (!auth.get("userStatus").equals("admin") && course.getUserId() != (int)auth.get("id")){
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        //已经存在小测
        if (questionService.findByCatalogId(catalogId) != null){
            return new ResponseEntity(HttpStatus.CONFLICT);
        }

        //创建小测
        Question question = new Question();
        question.setCatalogId(catalogId);
        question.setQuestions(new JSONArray(((ArrayList)body.get("questions")).toArray()).toString());
        question.setTotal(Float.parseFloat(body.get("total").toString()));

        int createdCount = questionService.create(question);  //创建小测
        if(createdCount != 1){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Map sendData = new HashMap();   //创建返回对象
        sendData.put("questionId",question.getId());

        //返回小测ID
        return new ResponseEntity(sendData,HttpStatus.OK);
    }

    /**
     * 更新章节的小测
     * @param Authentication    登录信息
     * @param catalogId         章节ID
     * @return                  空对象
     */
    @PutMapping("/catalog/{catalogId}/question")
    public ResponseEntity updateQuestion(
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
        if (body.get("questions") == null || body.get("total") == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        Question question = questionService.findByCatalogId(catalogId); //获取小测
        //小测不存在
        if (question == null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        //获取章节
        Catalog catalog = catalogService.findByID(catalogId);
        if (catalog == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        //获取课程
        Course course = courseService.findByID(catalog.getCourseId());

        //不是管理员而且章节不属于自己的
        if (!auth.get("userStatus").equals("admin") && course.getUserId() != (int)auth.get("id")){
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        //更新小测
        question.setTotal(Float.parseFloat(body.get("total").toString()));
        question.setQuestions(new JSONArray(((ArrayList)body.get("questions")).toArray()).toString());

        int updatedCount = questionService.update(question);  //更新小测
        if(updatedCount != 1){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //返回空对象
        return new ResponseEntity(new HashMap(),HttpStatus.OK);
    }

    /**
     * 删除章节的小测
     * @param Authentication    登录信息
     * @param catalogId         章节ID
     * @return                  空对象
     */
    @DeleteMapping("/catalog/{catalogId}/question")
    public ResponseEntity deleteQuestion(
            @RequestHeader(value="Authentication", required = false) String Authentication,
            @PathVariable int catalogId
    ) {
        //未登录
        Map auth;
        if (Authentication == null || (auth = jwt.decode(Authentication)) == null) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        //获取小测
        Question question = questionService.findByCatalogId(catalogId);
        //不存在小测
        if (question == null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        //获取章节
        Catalog catalog = catalogService.findByID(catalogId);
        if (catalog == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        //获取课程
        Course course = courseService.findByID(catalog.getCourseId());

        //不是管理员而且章节不属于自己的
        if (!auth.get("userStatus").equals("admin") && course.getUserId() != (int)auth.get("id")){
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        int deletedCount = questionService.delete(question.getId());    //删除小测
        if(deletedCount != 1){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //返回空对象
        return new ResponseEntity(new HashMap(),HttpStatus.OK);
    }

    /**
     * 获取章节的小测
     * @param Authentication    登录信息
     * @param catalogId         章节ID
     * @return                  返回小测
     */
    @GetMapping("/catalog/{catalogId}/question")
    public ResponseEntity getQuestion(
            @RequestHeader(value="Authentication", required = false) String Authentication,
            @PathVariable int catalogId
    ) {
        //未登录
        Map auth;
        if (Authentication == null || (auth = jwt.decode(Authentication)) == null) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        //获取小测
        Question question = questionService.findByCatalogId(catalogId);
        //不存在小测
        if (question == null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        Map sendData = new HashMap();   //创建返回对象
        sendData.put("id",question.getId());
        sendData.put("date",question.getDate());
        sendData.put("catalogId",question.getCatalogId());

        sendData.put("total",question.getTotal());
        sendData.put("questions",new JSONArray(question.getQuestions()).toList());

        //返回小测
        return new ResponseEntity(sendData,HttpStatus.OK);
    }
}