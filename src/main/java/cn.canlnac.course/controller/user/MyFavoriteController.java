package cn.canlnac.course.controller.user;

import cn.canlnac.course.entity.Chat;
import cn.canlnac.course.entity.Course;
import cn.canlnac.course.entity.Favorite;
import cn.canlnac.course.service.ChatService;
import cn.canlnac.course.service.CourseService;
import cn.canlnac.course.service.FavoriteService;
import cn.canlnac.course.util.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 我的收藏
 */
@Component("MyFavoriteController")
@RestController
public class MyFavoriteController {

    @Autowired
    private FavoriteService favoriteService;
    @Autowired
    private ChatService chatService;
    @Autowired
    private CourseService courseService;

    @Autowired
    private JWT jwt;

    /**
     * 获取我的收藏
     * @param Authentication    登陆信息
     * @param start             分页开始位置
     * @param count             分页返回数目
     * @param type              收藏类型,course,chat
     * @return
     */
    @GetMapping("/user/favorites")
    public ResponseEntity<Map<String, Object>> getFavorites(
        @RequestHeader(value="Authentication", required = false) String Authentication,
        @RequestParam(value="start", defaultValue="0") int start,
        @RequestParam(value="count", defaultValue="10") int count,
        @RequestParam(value="type", defaultValue="course") String type
    ) {
        //未登录
        Map<String,Object> auth;
        if(Authentication == null || (auth = jwt.decode(Authentication)) == null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        //验证参数
        if(start < 0 || count < 1 || start > count || Arrays.asList("course","chat").indexOf(type) < 0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        //统计我的收藏数目
        int total = favoriteService.countFavorite(Integer.parseInt(auth.get("id").toString()), type);
        if(total<1){//没有消息
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        //获取收藏数据
        List<Favorite> favoriteList = favoriteService.getFavorite(
            start,
            count,
            type,
            Integer.parseInt(auth.get("id").toString())
        );

        List<Map<String,Object>> favorites = new ArrayList<>();
        if (type.equals("course")) {
            for (Favorite favorite: favoriteList) {
                Map<String,Object> favoriteObj = new HashMap<>();

                favoriteObj.put("type", favorite.getTargetType());
                favoriteObj.put("date", favorite.getDate());

                Course course = courseService.findByID(favorite.getTargetId());

                favoriteObj.put("id", course.getId());
                favoriteObj.put("name", course.getName());
                favoriteObj.put("content", course.getIntroduction());

                favorites.add(favoriteObj);
            }
        } else {
            for (Favorite favorite: favoriteList) {
                Map<String,Object> favoriteObj = new HashMap<>();

                favoriteObj.put("type", favorite.getTargetType());
                favoriteObj.put("date", favorite.getDate());

                Chat chat = chatService.findByID(favorite.getTargetId());

                favoriteObj.put("id", chat.getId());
                favoriteObj.put("name", chat.getTitle());
                favoriteObj.put("content", chat.getContent());

                favorites.add(favoriteObj);
            }
        }

        //创建返回数据
        HashMap<String,Object> sendData = new HashMap<>();
        sendData.put("total", total);
        sendData.put("favorites", favorites);

        //返回我的收藏数据
        return new ResponseEntity<>(sendData, HttpStatus.OK);
    }
}