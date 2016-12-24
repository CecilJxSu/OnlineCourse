package cn.canlnac.course.controller.chat;

import cn.canlnac.course.entity.Chat;
import cn.canlnac.course.service.ChatService;
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
 * 话题收藏
 */
@Component("ChatFavoriteController")
@RestController
public class FavoriteController {
    @Autowired
    FavoriteService favoriteService;

    @Autowired
    ChatService chatService;

    @Autowired
    JWT jwt;

    /**
     * 收藏话题
     * @param Authentication    登录信息
     * @param chatId            话题ID
     * @return                  空对象
     */
    @PostMapping("/chat/{chatId}/favorite")
    public ResponseEntity<Map<String, Object>> favorite(
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

        //是否已经收藏了
        int isFavorite = favoriteService.isFavorite(Integer.parseInt(auth.get("id").toString()), "chat", chatId);
        if(isFavorite>0){  //收藏了
            return new ResponseEntity(HttpStatus.NOT_MODIFIED);
        }

        //收藏话题
        int favoriteCount = favoriteService.create("chat", chatId, Integer.parseInt(auth.get("id").toString()));
        //收藏失败
        if(favoriteCount != 1){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //增加话题收藏数
        Chat updateChat = new Chat();

        updateChat.setId(chat.getId());
        updateChat.setFavoriteCount(4);

        chatService.update(updateChat);   //更新话题

        //返回空对象
        return new ResponseEntity(new HashMap(), HttpStatus.OK);
    }

    /**
     * 取消收藏话题
     * @param Authentication    登录信息
     * @param chatId            话题ID
     * @return                  空对象
     */
    @DeleteMapping("/chat/{chatId}/favorite")
    public ResponseEntity<Map<String, Object>> unFavorite(
            @RequestHeader(value="Authentication", required = false) String Authentication,
            @PathVariable int chatId
    ) {
        //未登录
        Map<String, Object> auth;
        if (Authentication == null || (auth = jwt.decode(Authentication)) == null) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        //是否已经收藏了
        int isFavorite = favoriteService.isFavorite(Integer.parseInt(auth.get("id").toString()), "chat", chatId);
        if(isFavorite<1){  //未收藏
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        //取消收藏话题
        int followingCount = favoriteService.delete("chat", chatId, Integer.parseInt(auth.get("id").toString()));
        //取消收藏失败
        if(followingCount != 1){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //减少话题收藏数
        Chat chat = chatService.findByID(chatId);
        if(chat != null){
            Chat updateChat = new Chat();

            updateChat.setId(chat.getId());
            updateChat.setFavoriteCount(-4);
            
            chatService.update(updateChat);   //更新话题
        }

        //返回空对象
        return new ResponseEntity(new HashMap(), HttpStatus.OK);
    }
}