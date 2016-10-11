package cn.canlnac.course.controller.document;

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

import java.util.HashMap;
import java.util.Map;

/**
 * 文档
 */
@Component("DocumentRootController")
@RestController
public class RootController {
    @Autowired
    CourseService courseService;

    @Autowired
    CatalogService catalogService;

    @Autowired
    DocumentService documentService;

    @Autowired
    JWT jwt;

    /**
     * 获取文档
     * @param Authentication    登录信息
     * @param documentId        文档ID
     * @return                  文档
     */
    @GetMapping("/document/{documentId}")
    public ResponseEntity getDocument(
            @RequestHeader(value="Authentication", required = false) String Authentication,
            @PathVariable int documentId
    ) {
        //未登录
        Map<String, Object> auth;
        if (Authentication == null || (auth = jwt.decode(Authentication)) == null) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        Document document = documentService.findByID(documentId);   //获取文档
        if(document == null){  //没有文档
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        //返回文档
        return new ResponseEntity(document, HttpStatus.OK);
    }

    /**
     * 删除文档
     * @param Authentication    登录信息
     * @param documentId        文档ID
     * @return                  空对象
     */
    @DeleteMapping("/document/{documentId}")
    public ResponseEntity deleteDocument(
            @RequestHeader(value="Authentication", required = false) String Authentication,
            @PathVariable int documentId
    ) {
        //未登录
        Map<String, Object> auth;
        if (Authentication == null || (auth = jwt.decode(Authentication)) == null) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        Document document = documentService.findByID(documentId);   //获取文档
        if(document == null){  //没有文档
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        int courseId = 0;   //课程ID
        //文档类型是章节
        if (document.getTargetType().equals("catalog")){
            Catalog catalog = catalogService.findByID(document.getTargetId());
            if (catalog != null){
                courseId = catalog.getCourseId();
            }
        }
        //文档类型是课程
        else if (document.getTargetType().equals("course")){
            courseId = document.getTargetId();
        }

        int authorId = -1;   //作者ID
        //获取课程
        Course course = courseService.findByID(courseId);
        if(course != null){
            authorId = course.getUserId();
        }

        //不是管理员而且文档不属于自己的
        if (!auth.get("userStatus").equals("admin") && authorId != (int)auth.get("id")){
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        int deletedCount = documentService.delete(documentId);  //删除文档
        if(deletedCount != 1){  //删除失败
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //返回空对象
        return new ResponseEntity(new HashMap(), HttpStatus.OK);
    }
}