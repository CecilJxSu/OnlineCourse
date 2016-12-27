package cn.canlnac.course.controller.chat;

import cn.canlnac.course.entity.Chat;
import cn.canlnac.course.entity.Profile;
import cn.canlnac.course.service.*;
import cn.canlnac.course.util.JWT;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 话题
 */
@Component("ChatRootController")
@RestController
public class RootController {
    @Autowired
    ChatService chatService;

    @Autowired
    LikeService likeService;

    @Autowired
    FavoriteService favoriteService;

    @Autowired
    WatchService watchService;

    @Autowired
    ProfileService profileService;

    @Autowired
    JWT jwt;

    /**
     * 创建话题
     * @param Authentication    登录信息
     * @param body              请求数据
     * @return                  话题ID
     */
    @PostMapping("/chat")
    public ResponseEntity<Map<String, Object>> create(
            @RequestHeader(value="Authentication", required = false) String Authentication,
            @RequestBody Map<String, Object> body
    ) {
        //未登录
        Map<String, Object> auth;
        if (Authentication == null || (auth = jwt.decode(Authentication)) == null) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        //处理图片数组
        JSONArray pictureUrls = new JSONArray();
        if (body.get("pictureUrls") != null){
            try {
                Iterator<String> it = ((ArrayList)body.get("pictureUrls")).iterator();
                while (it.hasNext()){
                    pictureUrls.put(it.next());
                }
            } catch (Exception e){
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        }

        //参数验证
        if(body.get("title") == null || body.get("title").toString().isEmpty() ||
                body.get("content") == null || body.get("content").toString().isEmpty()
        ) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        Chat chat = new Chat();   //话题
        chat.setContent(body.get("content").toString());
        chat.setTitle(body.get("title").toString());
        chat.setUserId(Integer.parseInt(auth.get("id").toString()));
        chat.setPictureUrls(pictureUrls.toString());

        int createdCount = chatService.create(chat);    //创建话题

        if (createdCount != 1) {    //创建失败
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //创建返回数据
        HashMap<String,Object> sendData = new HashMap();
        sendData.put("chatId", chat.getId());

        //返回话题ID
        return new ResponseEntity(sendData, HttpStatus.OK);
    }

    /**
     * 删除话题
     * @param Authentication    登录信息
     * @param chatId            话题ID
     * @return                  空对象
     */
    @DeleteMapping("/chat/{chatId}")
    public ResponseEntity<Map<String, Object>> delete(
            @RequestHeader(value="Authentication", required = false) String Authentication,
            @PathVariable int chatId
    ) {
        //未登录
        Map<String, Object> auth;
        if (Authentication == null || (auth = jwt.decode(Authentication)) == null) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        //获取话题
        Chat chat = chatService.findByID(chatId);
        if(chat == null){ //话题不存在
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        //不是管理员而且话题不属于自己的
        if (!auth.get("userStatus").equals("admin") && chat.getUserId() != (int)auth.get("id")){
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        //删除话题
        int deletedCount = chatService.delete(chatId);
        if(deletedCount != 1) { //删除失败
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //创建返回数据
        HashMap<String,Object> sendData = new HashMap();

        //返回空对象
        return new ResponseEntity(sendData, HttpStatus.OK);
    }

    /**
     * 获取话题
     * @param Authentication    登录信息
     * @param chatId            话题ID
     * @return                  话题信息
     */
    @GetMapping("/chat/{chatId}")
    public ResponseEntity<Map<String, Object>> get(
            @RequestHeader(value="Authentication", required = false) String Authentication,
            @PathVariable int chatId
    ) {
        //未登录
        Map<String, Object> auth;
        if (Authentication == null || (auth = jwt.decode(Authentication)) == null) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        //获取话题
        Chat chat = chatService.findByID(chatId);
        if(chat == null){ //话题不存在
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        //获取标记
        int isWatch = watchService.isWatch((int)auth.get("id"),"chat",chatId);
        int isLike = likeService.isLike((int)auth.get("id"),"chat",chatId);
        int isFavorite = favoriteService.isFavorite((int)auth.get("id"),"chat",chatId);

        //创建返回数据
        HashMap<String,Object> sendData = new HashMap();

        //设置返回信息
        sendData.put("id",chat.getId());
        sendData.put("date",chat.getDate());
        sendData.put("title",chat.getTitle());
        sendData.put("content",chat.getContent());

        //设置作者
        Profile profile = profileService.findByUserID(chat.getUserId());
        if(profile != null){
            Map<String,Object> author = new HashMap();

            author.put("id",profile.getUserId());
            author.put("name",profile.getNickname());
            author.put("iconUrl",profile.getIconUrl());

            sendData.put("author", author);
        }

        //话题图片
        List<String> pictureUrls = new ArrayList();
        if (chat.getPictureUrls() != null){
            JSONArray array = new JSONArray(chat.getPictureUrls());
            for (int i = 0; i < array.length(); i++){
                pictureUrls.add(array.get(i).toString());
            }
        }
        sendData.put("pictureUrls", pictureUrls);

        sendData.put("likeCount",chat.getLikeCount()/2);
        sendData.put("commentCount",chat.getCommentCount()/3);
        sendData.put("favoriteCount",chat.getFavoriteCount()/4);

        sendData.put("isLike",(isLike>0));
        sendData.put("isFavorite",(isFavorite>0));
        if(isWatch<1){
            Chat updateChat = new Chat();
            
            updateChat.setId(chat.getId());
            updateChat.setWatchCount(1);
            chatService.update(updateChat);
            watchService.create("chat",chat.getId(),(int)auth.get("id"));
        }
        sendData.put("watchCount",chat.getWatchCount());

        //返回话题信息
        return new ResponseEntity(sendData, HttpStatus.OK);
    }

    /**
     * 获取话题列表
     * @param Authentication    登录信息
     * @param start             分页开始位置
     * @param count             分页返回数目
     * @param sort              排序方式：日期：date，热度：rank
     * @return                  话题列表
     */
    @GetMapping("/chats")
    public ResponseEntity<Map<String, Object>> getChats(
            @RequestHeader(value="Authentication", required = false) String Authentication,
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "10") int count,
            @RequestParam(defaultValue = "date") String sort
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

        //统计话题
        int total = chatService.count(conditions);
        if(total<1){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        //获取话题列表
        List<Chat> chatList = chatService.getList(start, count, sort, conditions);
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

            //设置作者
            Profile profile = profileService.findByUserID(chat.getUserId());
            if(profile != null){
                Map<String,Object> author = new HashMap();

                author.put("id",profile.getUserId());
                author.put("name",profile.getNickname());
                author.put("iconUrl",profile.getIconUrl());

                chatObj.put("author", author);
            }

            //话题图片
            List<String> pictureUrls = new ArrayList();
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
        HashMap<String,Object> sendData = new HashMap();
        sendData.put("total",total);
        sendData.put("chats",chats);

        //返回话题列表
        return new ResponseEntity(sendData, HttpStatus.OK);
    }
}