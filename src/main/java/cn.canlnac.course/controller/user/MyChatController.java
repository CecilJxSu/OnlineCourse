package cn.canlnac.course.controller.user;

import cn.canlnac.course.entity.Chat;
import cn.canlnac.course.entity.Profile;
import cn.canlnac.course.service.ChatService;
import cn.canlnac.course.service.FavoriteService;
import cn.canlnac.course.service.LikeService;
import cn.canlnac.course.service.ProfileService;
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
 * 我的话题
 */
@Component("MyChatController")
@RestController
public class MyChatController {

    @Autowired
    private ChatService chatService;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private LikeService likeService;
    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private JWT jwt;

    /**
     * 获取我的话题
     * @param Authentication    登陆信息
     * @param start             分页开始位置
     * @param count             分页返回数目
     * @return
     */
    @GetMapping("/user/chats")
    public ResponseEntity<Map<String, Object>> getChats(
        @RequestHeader(value="Authentication", required = false) String Authentication,
        @RequestParam(value="start", defaultValue="0") int start,
        @RequestParam(value="count", defaultValue="10") int count
    ) {
        //未登录
        Map<String,Object> auth;
        if(Authentication == null || (auth = jwt.decode(Authentication)) == null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        //验证参数
        if(start < 0 || count < 1){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Map<String,Object> conditions = new HashMap<>();
        conditions.put("userId", Integer.parseInt(auth.get("id").toString()));
        //统计我的话题数目
        int total = chatService.count(conditions);
        if(total<1){//没有消息
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        //获取话题
        List<Chat> chatList = chatService.getList(
            start,
            count,
            "date",
            conditions
        );

        //返回的话题
        List<Map<String,Object>> chats = new ArrayList<>();

        //遍历话题
        Iterator<Chat> iterator = chatList.iterator();
        Chat chat;
        while (iterator.hasNext()){
            chat = iterator.next();
            Map<String,Object> chatObj = new HashMap<>();

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

            int isLike = 0;
            int isFavorite = 0;
            //用户登录
            if (auth != null){
                //获取标记
                isLike = likeService.isLike((int)auth.get("id"),"chat",chat.getId());
                isFavorite = favoriteService.isFavorite((int)auth.get("id"),"chat",chat.getId());
            }

            chatObj.put("isLike",(isLike>0));
            chatObj.put("isFavorite",(isFavorite>0));

            chats.add(chatObj);
        }

        //创建返回数据
        HashMap<String,Object> sendData = new HashMap<>();
        sendData.put("total",total);
        sendData.put("chats",chats);

        //返回我的话题数据
        return new ResponseEntity<>(sendData, HttpStatus.OK);
    }
}