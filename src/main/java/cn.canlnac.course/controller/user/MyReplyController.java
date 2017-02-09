package cn.canlnac.course.controller.user;

import cn.canlnac.course.entity.Comment;
import cn.canlnac.course.entity.Profile;
import cn.canlnac.course.entity.Reply;
import cn.canlnac.course.service.CommentService;
import cn.canlnac.course.service.LikeService;
import cn.canlnac.course.service.ProfileService;
import cn.canlnac.course.service.ReplyService;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的回复
 */
@Component("MyReplyController")
@RestController
public class MyReplyController {

    @Autowired
    private ReplyService replyService;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private LikeService likeService;

    @Autowired
    private JWT jwt;

    /**
     * 获取我的回复
     * @param Authentication    登陆信息
     * @param start             分页开始位置
     * @param count             分页返回数目
     * @return
     */
    @GetMapping("/user/replies")
    public ResponseEntity<Map<String, Object>> getReplies(
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
        if(start < 0 || count < 1 || start > count){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        //统计我的回复数目
        List<Reply> replyList = replyService.getOwnReplies(Integer.parseInt(auth.get("id").toString()));
        if(replyList == null || replyList.size() <= 0 || start > replyList.size()) {//没有我的回复
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (start + count > replyList.size()) {
            count = replyList.size() - start;
        }

        replyList = replyList.subList(start, start + count);

        //返回的Object格式评论
        List<Map<String,Object>> comments = new ArrayList<>();

        for (Reply reply:replyList) {
            //返回的Object格式回复
            List<Map<String,Object>> replies = new ArrayList<>();

            //处理回复
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
            Profile userProfile = profileService.findByUserID(reply.getToUserId());
            if(userProfile != null){
                Map<String,Object> author = new HashMap<>();
                author.put("id",userProfile.getUserId());
                author.put("name",userProfile.getNickname());
                author.put("iconUrl",userProfile.getIconUrl());

                replyObj.put("author",author);
            }

            replyObj.put("content",reply.getContent());

            replies.add(replyObj);

            //处理评论
            Comment comment = commentService.findByID(reply.getCommentId());

            if (comment == null) {
                comment = new Comment();
            }

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
            if (comment.getPictureUrls() != null){
                JSONArray array = new JSONArray(comment.getPictureUrls());
                for (int i = 0; i < array.length(); i++){
                    pictureUrls.add(array.get(i).toString());
                }
            }
            commentObj.put("pictureUrls", pictureUrls);

            commentObj.put("likeCount",comment.getLikeCount());
            commentObj.put("replyCount",comment.getReplyCount()/2); //回复时，每次+2，所以要除2

            //设置点赞标记
            int isLike = likeService.isLike(Integer.parseInt(auth.get("id").toString()),"comment",comment.getId());
            commentObj.put("isLike", isLike > 0);

            //添加回复到评论中
            commentObj.put("replies",replies);

            //添加评论到返回数据中
            comments.add(commentObj);
        }

        //创建返回数据
        HashMap<String,Object> sendData = new HashMap<>();
        sendData.put("total", replyList.size());
        sendData.put("comments", comments);

        //返回我的回复数据
        return new ResponseEntity<>(sendData, HttpStatus.OK);
    }
}