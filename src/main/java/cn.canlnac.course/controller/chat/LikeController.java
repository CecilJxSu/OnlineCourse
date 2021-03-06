package cn.canlnac.course.controller.chat;

import cn.canlnac.course.entity.Chat;
import cn.canlnac.course.entity.Message;
import cn.canlnac.course.service.ChatService;
import cn.canlnac.course.service.LikeService;
import cn.canlnac.course.service.MessageService;
import cn.canlnac.course.util.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 话题点赞
 */
@Component("ChatLikeController")
@RestController
public class LikeController {
    @Autowired
    LikeService likeService;

    @Autowired
    ChatService chatService;

    @Autowired
    MessageService messageService;

    @Autowired
    JWT jwt;

    /**
     * 点赞话题
     * @param Authentication    登录信息
     * @param chatId            话题ID
     * @return                  空对象
     */
    @PostMapping("/chat/{chatId}/like")
    public ResponseEntity<Map<String, Object>> like(
            @RequestHeader(value="Authentication", required = false) String Authentication,
            @PathVariable int chatId
    ) {
        //未登录
        Map<String, Object> auth;
        if (Authentication == null || (auth = jwt.decode(Authentication)) == null) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        //获取话题信息
        Chat chat = chatService.findByID(chatId);
        if(chat == null){ //话题不存在
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        //是否已经点赞了
        int isLike = likeService.isLike(Integer.parseInt(auth.get("id").toString()), "chat", chatId);
        if(isLike>0){  //点赞了
            return new ResponseEntity(HttpStatus.NOT_MODIFIED);
        }

        //点赞话题
        int likeCount = likeService.create("chat", chatId, Integer.parseInt(auth.get("id").toString()));
        //点赞失败
        if(likeCount != 1){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //增加话题点赞数
        Chat updateChat = new Chat();

        updateChat.setId(chat.getId());
        updateChat.setLikeCount(2);
        chatService.update(updateChat);   //更新话题

        try {
            Message message = new Message();

            message.setDate(new Date());
            message.setIsRead('N');
            message.setContent("");
            message.setToUserId(chat.getUserId());
            message.setFromUserId(Integer.parseInt(auth.get("id").toString()));
            message.setType("chat");
            message.setActionType("like");
            message.setPositionId(chat.getId());

            messageService.create(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //返回空对象
        return new ResponseEntity(new HashMap(), HttpStatus.OK);
    }

    /**
     * 取消点赞话题
     * @param Authentication    登录信息
     * @param chatId            话题ID
     * @return                  空对象
     */
    @DeleteMapping("/chat/{chatId}/like")
    public ResponseEntity<Map<String, Object>> unLike(
            @RequestHeader(value="Authentication", required = false) String Authentication,
            @PathVariable int chatId
    ) {
        //未登录
        Map<String, Object> auth;
        if (Authentication == null || (auth = jwt.decode(Authentication)) == null) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        //是否已经点赞了
        int isLike = likeService.isLike(Integer.parseInt(auth.get("id").toString()), "chat", chatId);
        if(isLike<1){  //未点赞
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        //取消点赞话题
        int followingCount = likeService.delete("chat", chatId, Integer.parseInt(auth.get("id").toString()));
        //取消点赞失败
        if(followingCount != 1){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //减少话题点赞数
        Chat chat = chatService.findByID(chatId);
        if(chat != null){
            Chat updateChat = new Chat();
        
            updateChat.setId(chat.getId());
            updateChat.setLikeCount(-2);
            chatService.update(updateChat);   //更新话题
        }

        //返回空对象
        return new ResponseEntity(new HashMap(), HttpStatus.OK);
    }
}