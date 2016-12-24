package cn.canlnac.course.controller.reply;

import cn.canlnac.course.entity.Profile;
import cn.canlnac.course.entity.Reply;
import cn.canlnac.course.service.*;
import cn.canlnac.course.util.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 回复
 */
@Component("ReplyRootController")
@RestController
public class RootController {

    @Autowired
    ReplyService replyService;

    @Autowired
    ProfileService profileService;

    @Autowired
    JWT jwt;

    /**
     * 获取指定回复
     * @param Authentication    登录信息
     * @param replyId           回复ID
     * @return                  回复
     */
    @GetMapping("/reply/{replyId}")
    public ResponseEntity<Map<String,Object>> getComment(
            @RequestHeader(value="Authentication", required = false) String Authentication,
            @PathVariable int replyId
    ) {
        //处理登录信息
        Map<String, Object> auth = jwt.decode(Authentication);

        //获取回复
        Reply reply = replyService.findByID(replyId);
        if (null == reply) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        //返回的Object格式回复
        Map<String,Object> replyObj = new HashMap<>();
        replyObj.put("id",reply.getId());
        replyObj.put("date",reply.getDate());

        //处理回复用户
        Profile toUserProfile = profileService.findByUserID(reply.getToUserId());
        if(toUserProfile != null){
            Map<String,Object> toUser = new HashMap<>();
            toUser.put("id",toUserProfile.getUserId());
            toUser.put("name",toUserProfile.getNickname());
            toUser.put("iconUrl",toUserProfile.getIconUrl());

            replyObj.put("toUser",toUser);
        }

        //处理回复的作者
        Profile userProfile = profileService.findByUserID(reply.getUserId());
        if(userProfile != null){
            Map<String,Object> author = new HashMap<>();
            author.put("id",userProfile.getUserId());
            author.put("name",userProfile.getNickname());
            author.put("iconUrl",userProfile.getIconUrl());

            replyObj.put("author",author);
        }

        replyObj.put("content",reply.getContent());

        //返回回复
        return new ResponseEntity<>(replyObj, HttpStatus.OK);
    }
}