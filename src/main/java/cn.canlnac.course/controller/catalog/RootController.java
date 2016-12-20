package cn.canlnac.course.controller.catalog;

import cn.canlnac.course.entity.Catalog;
import cn.canlnac.course.entity.Course;
import cn.canlnac.course.service.CatalogService;
import cn.canlnac.course.service.CourseService;
import cn.canlnac.course.util.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 章节
 */
@Component("CatalogRootController")
@RestController
public class RootController {
    @Autowired
    private CatalogService catalogService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private JWT jwt;

    /**
     * 获取章节
     * @param Authentication    登录信息
     * @param catalogId         章节ID
     * @return                  返回章节
     */
    @GetMapping("/catalog/{catalogId}")
    public ResponseEntity getCatalog(
            @RequestHeader(value="Authentication", required = false) String Authentication,
            @PathVariable int catalogId
    ) {
        //未登录
        Map auth;
        if (Authentication == null || (auth = jwt.decode(Authentication)) == null) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        //获取章节
        Catalog catalog = catalogService.findByID(catalogId);
        //不存在章节
        if (catalog == null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        //返回章节
        return new ResponseEntity(catalog,HttpStatus.OK);
    }

    /**
     * 更新章节
     * @param Authentication    登录信息
     * @param body              请求数据
     * @param catalogId         章节ID
     * @return                  空对象
     */
    @PutMapping("/catalog/{catalogId}")
    public ResponseEntity updateCatalog(
            @RequestHeader(value="Authentication", required = false) String Authentication,
            @RequestBody Map body,
            @PathVariable int catalogId
    ) {
        //未登录
        Map auth;
        if (Authentication == null || (auth = jwt.decode(Authentication)) == null) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        Catalog catalog = catalogService.findByID(catalogId); //获取章节
        //章节不存在
        if (catalog == null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        //获取课程
        Course course = courseService.findByID(catalog.getCourseId());
        //不是管理员而且章节不属于自己的
        if (!auth.get("userStatus").equals("admin") && course.getUserId() != (int)auth.get("id")){
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        //没有参数
        if (body.size() == 0) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        //验证每个请求参数是否合法
        Iterator<String> keys = body.keySet().iterator();
        String key;
        Object value;
        while (keys.hasNext()) {
            key = keys.next();
            value = body.get(key);
            switch (key) {
                case "index":
                    if (value == null){
                        return new ResponseEntity(HttpStatus.BAD_REQUEST);
                    }

                    //修改其它的index
                    /*if (!value.equals(catalog.getIndex())) {
                        //获取所有章节
                        Catalog[] catalogs = catalogService.getList(catalog.getCourseId()).toArray(new Catalog[0]);
                        List<Integer> indexes = new ArrayList<>();
                        for (Catalog _catalog:catalogs){
                            indexes.add(_catalog.getIndex());
                        }

                        //检查index是否冲突
                        if(indexes.contains(Integer.parseInt(value.toString()))){
                            return new ResponseEntity(HttpStatus.CONFLICT);
                        }
                    }*/

                    catalog.setIndex(Integer.parseInt(value.toString()));
                    break;
                case "parent_id":
                    if (value == null) {
                        return new ResponseEntity(HttpStatus.BAD_REQUEST);
                    }
                    catalog.setParentId(Integer.parseInt(value.toString()));
                    break;
                case "name":
                    if (value == null || value.toString().isEmpty()){
                        return new ResponseEntity(HttpStatus.BAD_REQUEST);
                    }
                    catalog.setName(value.toString());
                    break;
                case "introduction":
                    if (value == null || value.toString().isEmpty()){
                        return new ResponseEntity(HttpStatus.BAD_REQUEST);
                    }
                    catalog.setIntroduction(value.toString());
                    break;
                case "url":
                    if (value == null || value.toString().isEmpty() ||
                        body.get("duration") == null || body.get("previewImage") == null
                    ){
                        return new ResponseEntity(HttpStatus.BAD_REQUEST);
                    }
                    catalog.setUrl(value.toString());
                    break;
                case "duration":
                    if (value == null){
                        return new ResponseEntity(HttpStatus.BAD_REQUEST);
                    }
                    catalog.setDuration(Integer.parseInt(value.toString()));
                    break;
                case "previewImage":
                    if (value == null || value.toString().isEmpty()){
                        return new ResponseEntity(HttpStatus.BAD_REQUEST);
                    }
                    catalog.setPreviewImage(value.toString());
                    break;
                default:
                    keys.remove();
            }
        }

        int updatedCount = catalogService.update(catalog);  //更新章节
        if(updatedCount != 1){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //返回空对象
        return new ResponseEntity(new HashMap(),HttpStatus.OK);
    }

    /**
     * 删除章节
     * @param Authentication    登录信息
     * @param catalogId         章节ID
     * @return                  空对象
     */
    @DeleteMapping("/catalog/{catalogId}")
    public ResponseEntity deleteCatalog(
            @RequestHeader(value="Authentication", required = false) String Authentication,
            @PathVariable int catalogId
    ) {
        //未登录
        Map auth;
        if (Authentication == null || (auth = jwt.decode(Authentication)) == null) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        Catalog catalog = catalogService.findByID(catalogId); //获取章节
        //章节不存在
        if (catalog == null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        //获取课程
        Course course = courseService.findByID(catalog.getCourseId());
        //不是管理员而且章节不属于自己的
        if (!auth.get("userStatus").equals("admin") && course.getUserId() != (int)auth.get("id")){
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        int deletedCount = catalogService.delete(catalogId);  //删除章节
        if(deletedCount != 1){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //返回空对象
        return new ResponseEntity(new HashMap(),HttpStatus.OK);
    }
}