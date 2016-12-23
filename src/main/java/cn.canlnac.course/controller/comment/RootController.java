package cn.canlnac.course.controller.comment;

import cn.canlnac.course.entity.Comment;
import cn.canlnac.course.entity.Profile;
import cn.canlnac.course.entity.Reply;
import cn.canlnac.course.service.*;
import cn.canlnac.course.util.JWT;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 评论
 */
@Component("CommentRootController")
@RestController
public class RootController {
    @Autowired
    CommentService commentService;

    @Autowired
    ReplyService replyService;

    @Autowired
    ProfileService profileService;

    @Autowired
    LikeService likeService;

    @Autowired
    CourseService courseService;

    @Autowired
    UserService userService;

    @Autowired
    JWT jwt;

    /**
     * 获取指定评论
     * @param Authentication    登录信息
     * @param commentId         评论ID
     * @return                  评论列表
     */
    @GetMapping("/comment/{commentId}")
    public ResponseEntity<Map<String,Object>> getComment(
            @RequestHeader(value="Authentication", required = false) String Authentication,
            @PathVariable int commentId
    ) {
        //处理登录信息
        Map<String, Object> auth = jwt.decode(Authentication);

        //获取评论
        Comment comment = commentService.findByID(commentId);
        if (null == comment) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        //返回的Object格式评论
        Map<String,Object> commentObj = new HashMap<>();
        commentObj.put("id",comment.getId());
        commentObj.put("date",comment.getDate());

        //设置评论作者
        Profile authorProfile = profileService.findByUserID(comment.getUserId());
        if (authorProfile != null){
            Map<String,Object> author = new HashMap<>();
            author.put("id",authorProfile.getUserId());
            author.put("name",authorProfile.getNickname());
            author.put("iconUrl",authorProfile.getIconUrl());

            commentObj.put("author",author);
        }

        commentObj.put("content",comment.getContent());

        //评论图片
        List<String> pictureUrls = new ArrayList<>();
        if (comment.getPictureUrls() != null && !comment.getPictureUrls().isEmpty()){
            JSONArray array = new JSONArray(comment.getPictureUrls());
            for (int i = 0; i < array.length(); i++){
                pictureUrls.add(array.get(i).toString());
            }
        }
        commentObj.put("pictureUrls", pictureUrls);

        commentObj.put("likeCount",comment.getLikeCount());
        commentObj.put("replyCount",comment.getReplyCount()/2); //回复时，每次+2，所以要除2

        boolean isReply = false;
        //获取回复
        if (comment.getReplyCount() > 0){
            List<Reply> replyList = replyService.getReplies(comment.getId());
            //返回的Object格式回复
            List<Map<String,Object>> replies = new ArrayList<>();
            //处理回复
            for (Reply reply:replyList){
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

                    //设置是否回复
                    if (auth != null && auth.get("id").equals(userProfile.getUserId())) {
                        isReply = true;
                    }
                }

                replyObj.put("content",reply.getContent());

                replies.add(replyObj);
            }

            //添加回复到评论中
            commentObj.put("replies",replies);
        }

        commentObj.put("isReply", isReply);

        //设置点赞标记
        if(auth != null){   //登录状态
            int isLike = likeService.isLike(Integer.parseInt(auth.get("id").toString()),"comment",comment.getId());
            commentObj.put("isLike", isLike>0);
        } else {    //未登录
            commentObj.put("isLike",false);
        }

        //返回评论列表
        return new ResponseEntity<>(commentObj, HttpStatus.OK);
    }
}