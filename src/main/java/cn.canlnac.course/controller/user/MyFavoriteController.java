package cn.canlnac.course.controller.user;

import cn.canlnac.course.entity.*;
import cn.canlnac.course.service.*;
import cn.canlnac.course.util.JWT;
import org.json.JSONArray;
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
    private ProfileService profileService;
    @Autowired
    private LikeService likeService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private CatalogService catalogService;

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

        //创建返回数据
        HashMap<String,Object> sendData = new HashMap<>();
        sendData.put("total", total);

        if (type.equals("course")) {
            List<Map<String,Object>> courses = new ArrayList<>();
            for (Favorite favorite: favoriteList) {
                Map<String,Object> courseObj = new HashMap<>();

                Course course = courseService.findByID(favorite.getTargetId());

                //设置返回信息
                courseObj.put("id",course.getId());
                courseObj.put("date",course.getDate());
                courseObj.put("name",course.getName());
                courseObj.put("introduction",course.getIntroduction());

                //设置作者
                Profile profile = profileService.findByUserID(course.getUserId());
                if(profile != null){
                    Map<String,Object> author = new HashMap<>();

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

                int isLike = likeService.isLike((int)auth.get("id"),"course",course.getId());

                courseObj.put("isLike",(isLike>0));
                courseObj.put("isFavorite",true);

                courses.add(courseObj);
            }

            sendData.put("courses",courses);
        } else {
            //返回的话题
            List<Map<String,Object>> chats = new ArrayList<>();

            for (Favorite favorite: favoriteList) {
                Map<String,Object> chatObj = new HashMap<>();

                Chat chat = chatService.findByID(favorite.getTargetId());

                //设置返回信息
                chatObj.put("id",chat.getId());
                chatObj.put("date",chat.getDate());
                chatObj.put("title",chat.getTitle());
                chatObj.put("content",chat.getContent());
                chatObj.put("html",chat.getHtml());

                //设置作者
                Profile profile = profileService.findByUserID(chat.getUserId());
                if(profile != null){
                    Map<String,Object> author = new HashMap<>();

                    author.put("id",profile.getUserId());
                    author.put("name",profile.getNickname());
                    author.put("iconUrl",profile.getIconUrl());

                    chatObj.put("author", author);
                }

                //话题图片
                List<String> pictureUrls = new ArrayList<>();
                if (chat.getPictureUrls() != null){
                    JSONArray array = new JSONArray(chat.getPictureUrls());
                    for (int i = 0; i < array.length(); i++){
                        pictureUrls.add(array.get(i).toString());
                    }
                }
                chatObj.put("pictureUrls", pictureUrls);

                chatObj.put("watchCount",chat.getWatchCount());
                chatObj.put("likeCount",chat.getLikeCount()/2);
                chatObj.put("commentCount",chat.getCommentCount()/3);
                chatObj.put("favoriteCount",chat.getFavoriteCount()/4);

                int isLike = likeService.isLike((int)auth.get("id"),"chat",chat.getId());

                chatObj.put("isLike",(isLike>0));
                chatObj.put("isFavorite",true);

                chats.add(chatObj);
            }

            sendData.put("chats", chats);
        }

        //返回我的收藏数据
        return new ResponseEntity<>(sendData, HttpStatus.OK);
    }
}