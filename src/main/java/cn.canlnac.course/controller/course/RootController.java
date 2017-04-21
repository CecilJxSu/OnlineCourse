package cn.canlnac.course.controller.course;

import cn.canlnac.course.entity.Catalog;
import cn.canlnac.course.entity.Course;
import cn.canlnac.course.entity.Profile;
import cn.canlnac.course.service.*;
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
    LikeService likeService;

    @Autowired
    FavoriteService favoriteService;

    @Autowired
    WatchService watchService;

    @Autowired
    ProfileService profileService;

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

        int createdCount = courseService.create(course);    //创建课程

        if (createdCount != 1) {    //创建失败
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //创建返回数据
        HashMap<String,Object> sendData = new HashMap();
        sendData.put("courseId", course.getId());

        //返回课程ID
        return new ResponseEntity(sendData, HttpStatus.OK);
    }

    /**
     * 修改课程
     * @param Authentication    登录信息
     * @param body              请求数据
     * @param courseId          课程ID
     * @return                  空对象
     */
    @PutMapping("/course/{courseId}")
    public ResponseEntity<Map<String, Object>> update(
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

        //参数检查，并设置值
        Iterator<String> keys = body.keySet().iterator();
        String key;
        Object value;
        while (keys.hasNext()){
            key = keys.next();
            value = body.get(key);

            switch (key){
                case "status":
                    if (value == null || !value.toString().equals("public")){
                        return new ResponseEntity(HttpStatus.BAD_REQUEST);
                    }
                    course.setStatus("public");
                    break;
                case "name":
                    if (value == null || value.toString().isEmpty()){
                        return new ResponseEntity(HttpStatus.BAD_REQUEST);
                    }
                    course.setName(value.toString());
                    break;
                case "introduction":
                    if (value == null || value.toString().isEmpty()){
                        return new ResponseEntity(HttpStatus.BAD_REQUEST);
                    }
                    course.setIntroduction(value.toString());
                    break;
                case "department":
                    if (value == null || value.toString().isEmpty()){
                        return new ResponseEntity(HttpStatus.BAD_REQUEST);
                    }
                    course.setDepartment(value.toString());
                    break;
                default:
                    keys.remove();
            }
        }

        //更新课程
        int updatedCount = courseService.update(course);
        if(updatedCount != 1) { //更新失败
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //创建返回数据
        HashMap<String,Object> sendData = new HashMap();

        //返回空对象
        return new ResponseEntity(sendData, HttpStatus.OK);
    }

    /**
     * 删除课程
     * @param Authentication    登录信息
     * @param courseId          课程ID
     * @return                  空对象
     */
    @DeleteMapping("/course/{courseId}")
    public ResponseEntity<Map<String, Object>> delete(
            @RequestHeader(value="Authentication", required = false) String Authentication,
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

        //删除课程
        int deletedCount = courseService.delete(courseId);
        if(deletedCount != 1) { //删除失败
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //创建返回数据
        HashMap<String,Object> sendData = new HashMap();

        //返回空对象
        return new ResponseEntity(sendData, HttpStatus.OK);
    }

    /**
     * 获取课程
     * @param Authentication    登录信息
     * @param courseId          课程ID
     * @return                  课程信息
     */
    @GetMapping("/course/{courseId}")
    public ResponseEntity<Map<String, Object>> get(
            @RequestHeader(value="Authentication", required = false) String Authentication,
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

        //获取标记
        int isWatch = watchService.isWatch((int)auth.get("id"),"course",courseId);
        int isLike = likeService.isLike((int)auth.get("id"),"course",courseId);
        int isFavorite = favoriteService.isFavorite((int)auth.get("id"),"course",courseId);

        //创建返回数据
        HashMap<String,Object> sendData = new HashMap();

        //设置返回信息
        sendData.put("id",course.getId());
        sendData.put("date",course.getDate());
        sendData.put("name",course.getName());
        sendData.put("introduction",course.getIntroduction());

        //设置作者
        Profile profile = profileService.findByUserID(course.getUserId());
        if(profile != null){
            Map<String,Object> author = new HashMap();

            author.put("id",profile.getUserId());
            author.put("name",profile.getNickname());
            author.put("iconUrl",profile.getIconUrl());

            sendData.put("author", author);
        }

        //获取第一个图片
        List<Catalog> catalogs = catalogService.getList(course.getId());
        if (catalogs!=null && catalogs.size()>0) {
            for (Catalog catalog:catalogs) {
                if (catalog.getPreviewImage() != null && !catalog.getPreviewImage().isEmpty()) {
                    sendData.put("previewUrl",catalog.getPreviewImage());
                    break;
                }
            }
        }

        sendData.put("department",course.getDepartment());

        sendData.put("likeCount",course.getLikeCount()/2);
        sendData.put("commentCount",course.getCommentCount()/3);
        sendData.put("favoriteCount",course.getFavoriteCount()/4);

        sendData.put("isLike",(isLike>0));
        sendData.put("isFavorite",(isFavorite>0));
        if(isWatch<1){
            course.setWatchCount(course.getWatchCount() + 1);   //返回新的观看人数

            Course updateCourse = new Course(); //重新new一个
            updateCourse.setId(course.getId()); //设置id
            updateCourse.setWatchCount(1);      //增加浏览人数
            courseService.update(updateCourse); //更新课程观看人数，+1
            watchService.create("course",course.getId(),(int)auth.get("id"));   //创建观看记录
        }
        sendData.put("watchCount",course.getWatchCount());

        //返回课程信息
        return new ResponseEntity(sendData, HttpStatus.OK);
    }

    /**
     * 获取课程列表
     * @param Authentication    登录信息
     * @param start             分页开始位置
     * @param count             分页返回数目
     * @param sort              排序方式：日期：date，热度：rank
     * @param departments       院系
     * @return                  课程列表
     */
    @GetMapping("/courses")
    public ResponseEntity<Map<String, Object>> getCourses(
            @RequestHeader(value="Authentication", required = false) String Authentication,
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "10") int count,
            @RequestParam(defaultValue = "date") String sort,
            @RequestParam(required = false) List<String> departments
    ) {
        //未登录
        Map<String, Object> auth;
        if (Authentication == null || (auth = jwt.decode(Authentication)) == null) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        //参数验证
        if(start < 0 || count < 1 || !Arrays.asList("date","rank").contains(sort)){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        //查询条件
        Map<String, Object> conditions = new HashMap();
        conditions.put("status", Arrays.asList("public"));
        if(departments.size() > 0){
            conditions.put("department", departments);
        }

        //统计课程
        int total = courseService.count(conditions);
        if(total<1){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        //获取课程列表
        List<Course> courseList = courseService.getList(start, count, sort, conditions);
        //返回的课程
        List<Map<String,Object>> courses = new ArrayList<>();

        //遍历课程
        Iterator<Course> iterator = courseList.iterator();
        Course course;
        while (iterator.hasNext()){
            course = iterator.next();
            Map<String,Object> courseObj = new HashMap<>();

            //设置返回信息
            courseObj.put("id",course.getId());
            courseObj.put("date",course.getDate());
            courseObj.put("name",course.getName());
            courseObj.put("introduction",course.getIntroduction());

            //设置作者
            Profile profile = profileService.findByUserID(course.getUserId());
            if(profile != null){
                Map<String,Object> author = new HashMap();

                author.put("id",profile.getUserId());
                author.put("name",profile.getNickname());
                author.put("iconUrl",profile.getIconUrl());

                courseObj.put("author", author);
            }

            courseObj.put("department",course.getDepartment());

            courseObj.put("watchCount",course.getWatchCount());
            courseObj.put("likeCount",course.getLikeCount()/2);
            courseObj.put("commentCount",course.getCommentCount()/3);
            courseObj.put("favoriteCount",course.getFavoriteCount()/4);

            //获取第一个图片
            List<Catalog> catalogs = catalogService.getList(course.getId());
            if (catalogs!=null && catalogs.size()>0) {
                for (Catalog catalog:catalogs) {
                    if (catalog.getPreviewImage() != null && !catalog.getPreviewImage().isEmpty()) {
                        courseObj.put("previewUrl",catalog.getPreviewImage());
                        break;
                    }
                }
            }

            int isLike = 0;
            int isFavorite = 0;
            //用户登录
            if (auth != null){
                //获取标记
                isLike = likeService.isLike((int)auth.get("id"),"course",course.getId());
                isFavorite = favoriteService.isFavorite((int)auth.get("id"),"course",course.getId());
            }

            courseObj.put("isLike",(isLike>0));
            courseObj.put("isFavorite",(isFavorite>0));

            courses.add(courseObj);
        }

        //创建返回数据
        HashMap<String,Object> sendData = new HashMap();
        sendData.put("total",total);
        sendData.put("courses",courses);

        //返回课程列表
        return new ResponseEntity(sendData, HttpStatus.OK);
    }
}