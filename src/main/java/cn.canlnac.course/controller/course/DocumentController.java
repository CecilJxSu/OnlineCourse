package cn.canlnac.course.controller.course;

import cn.canlnac.course.entity.Course;
import cn.canlnac.course.entity.Document;
import cn.canlnac.course.service.CourseService;
import cn.canlnac.course.service.DocumentService;
import cn.canlnac.course.util.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 课程下的文档
 */
@Component("CourseDocumentController")
@RestController
public class DocumentController {
    @Autowired
    CourseService courseService;

    @Autowired
    DocumentService documentService;

    @Autowired
    JWT jwt;

    /**
     * 创建课程文档
     * @param Authentication    登录信息
     * @param body              请求数据
     * @param courseId          课程ID
     * @return                  文档ID
     */
    @PostMapping("/course/{courseId}/document")
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
        if(body.get("url") == null || body.get("type") == null || body.get("size") == null || body.get("name") == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        //创建文档
        Document document = new Document();
        //设置文档数据
        document.setTargetType("course");
        document.setTargetId(courseId);
        document.setName(body.get("name").toString());
        document.setType(body.get("type").toString());
        document.setSize(Integer.parseInt(body.get("size").toString()));
        document.setUrl(body.get("url").toString());

        int createdCount = documentService.create(document);    //创建文档
        if(createdCount != 1){    //创建失败
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //创建返回数据
        HashMap<String,Object> sendData = new HashMap();
        sendData.put("documentId", document.getId());

        //返回文档ID
        return new ResponseEntity(sendData, HttpStatus.OK);
    }

    /**
     * 获取课程下的文档列表
     * @param Authentication    登录信息
     * @param courseId          课程ID
     * @param start             分页开始位置
     * @param count             分页返回数目
     * @param sort              排序：按日期排序：date，按名称：name，按大小：size
     * @return                  文档列表
     */
    @GetMapping("/course/{courseId}/documents")
    public ResponseEntity getList(
            @RequestHeader(value="Authentication", required = false) String Authentication,
            @PathVariable int courseId,
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "10") int count,
            @RequestParam(defaultValue = "date") String sort
    ) {
        //未登录
        Map<String, Object> auth;
        if (Authentication == null || (auth = jwt.decode(Authentication)) == null) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        //检查参数
        if (start < 0 || count < 10 || !Arrays.asList("date","name","size").contains(sort)){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        int total = documentService.count("course", courseId, null);  //统计文档数目
        if(total < 1){  //没有文档
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        //获取文档列表
        List<Document> documents = documentService.getDocuments(start, count, sort, "course",courseId, null);

        //创建返回数据
        HashMap<String,Object> sendData = new HashMap();
        sendData.put("total",total);
        sendData.put("documents",documents);

        //返回文档列表
        return new ResponseEntity(sendData, HttpStatus.OK);
    }
}