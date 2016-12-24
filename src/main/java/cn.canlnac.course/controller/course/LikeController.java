package cn.canlnac.course.controller.course;

import cn.canlnac.course.entity.Course;
import cn.canlnac.course.service.CourseService;
import cn.canlnac.course.service.LikeService;
import cn.canlnac.course.util.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 课程点赞
 */
@Component("CourseLikeController")
@RestController
public class LikeController {
    @Autowired
    LikeService likeService;

    @Autowired
    CourseService courseService;

    @Autowired
    JWT jwt;

    /**
     * 点赞课程
     * @param Authentication    登录信息
     * @param courseId          课程ID
     * @return                  空对象
     */
    @PostMapping("/course/{courseId}/like")
    public ResponseEntity<Map<String, Object>> like(
            @RequestHeader(value="Authentication", required = false) String Authentication,
            @PathVariable int courseId
    ) {
        //未登录
        Map<String, Object> auth;
        if (Authentication == null || (auth = jwt.decode(Authentication)) == null) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        //获取课程信息
        Course course = courseService.findByID(courseId);
        if(course == null){ //课程不存在
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        //是否已经点赞了
        int isLike = likeService.isLike(Integer.parseInt(auth.get("id").toString()), "course", courseId);
        if(isLike>0){  //点赞了
            return new ResponseEntity(HttpStatus.NOT_MODIFIED);
        }

        //点赞课程
        int likeCount = likeService.create("course", courseId, Integer.parseInt(auth.get("id").toString()));
        //点赞失败
        if(likeCount != 1){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //增加课程点赞数
        Course updateCourse = new Course();
        updateCourse.setId(course.getId());
        updateCourse.setLikeCount(2);

        courseService.update(updateCourse);   //更新课程

        //返回空对象
        return new ResponseEntity(new HashMap(), HttpStatus.OK);
    }

    /**
     * 取消点赞课程
     * @param Authentication    登录信息
     * @param courseId          课程ID
     * @return                  空对象
     */
    @DeleteMapping("/course/{courseId}/like")
    public ResponseEntity<Map<String, Object>> unLike(
            @RequestHeader(value="Authentication", required = false) String Authentication,
            @PathVariable int courseId
    ) {
        //未登录
        Map<String, Object> auth;
        if (Authentication == null || (auth = jwt.decode(Authentication)) == null) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        //是否已经点赞了
        int isLike = likeService.isLike(Integer.parseInt(auth.get("id").toString()), "course", courseId);
        if(isLike<1){  //未点赞
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        //取消点赞课程
        int followingCount = likeService.delete("course", courseId, Integer.parseInt(auth.get("id").toString()));
        //取消点赞失败
        if(followingCount != 1){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //减少课程点赞数
        Course course = courseService.findByID(courseId);
        if(course != null){
            Course updateCourse = new COurse();

            updateCourse.setId(course.getId());
            updateCourse.setLikeCount(-2);
            courseService.update(updateCourse);
        }

        //返回空对象
        return new ResponseEntity(new HashMap(), HttpStatus.OK);
    }
}