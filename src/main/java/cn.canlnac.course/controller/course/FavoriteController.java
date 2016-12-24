package cn.canlnac.course.controller.course;

import cn.canlnac.course.entity.Course;
import cn.canlnac.course.service.CourseService;
import cn.canlnac.course.service.FavoriteService;
import cn.canlnac.course.util.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 课程收藏
 */
@Component("CourseFavoriteController")
@RestController
public class FavoriteController {
    @Autowired
    FavoriteService favoriteService;

    @Autowired
    CourseService courseService;

    @Autowired
    JWT jwt;

    /**
     * 收藏课程
     * @param Authentication    登录信息
     * @param courseId          课程ID
     * @return                  空对象
     */
    @PostMapping("/course/{courseId}/favorite")
    public ResponseEntity<Map<String, Object>> favorite(
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

        //是否已经收藏了
        int isFavorite = favoriteService.isFavorite(Integer.parseInt(auth.get("id").toString()), "course", courseId);
        if(isFavorite>0){  //收藏了
            return new ResponseEntity(HttpStatus.NOT_MODIFIED);
        }

        //收藏课程
        int favoriteCount = favoriteService.create("course", courseId, Integer.parseInt(auth.get("id").toString()));
        //收藏失败
        if(favoriteCount != 1){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //增加课程收藏数
        Course updateCourse = new Course();

        updateCourse.setId(course.getId());
        updateCourse.setFavoriteCount(4);
        courseService.update(updateCourse);   //更新课程

        //返回空对象
        return new ResponseEntity(new HashMap(), HttpStatus.OK);
    }

    /**
     * 取消收藏课程
     * @param Authentication    登录信息
     * @param courseId          课程ID
     * @return                  空对象
     */
    @DeleteMapping("/course/{courseId}/favorite")
    public ResponseEntity<Map<String, Object>> unFavorite(
            @RequestHeader(value="Authentication", required = false) String Authentication,
            @PathVariable int courseId
    ) {
        //未登录
        Map<String, Object> auth;
        if (Authentication == null || (auth = jwt.decode(Authentication)) == null) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        //是否已经收藏了
        int isFavorite = favoriteService.isFavorite(Integer.parseInt(auth.get("id").toString()), "course", courseId);
        if(isFavorite<1){  //未收藏
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        //取消收藏课程
        int followingCount = favoriteService.delete("course", courseId, Integer.parseInt(auth.get("id").toString()));
        //取消收藏失败
        if(followingCount != 1){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //减少课程收藏数
        Course course = courseService.findByID(courseId);
        if(course != null){
            Course updateCourse = new Course();
        
            updateCourse.setId(course.getId());
            updateCourse.setFavoriteCount(-4);
            courseService.update(updateCourse);   //更新课程
        }

        //返回空对象
        return new ResponseEntity(new HashMap(), HttpStatus.OK);
    }
}