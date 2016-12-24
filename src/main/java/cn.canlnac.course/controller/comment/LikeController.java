package cn.canlnac.course.controller.comment;

import cn.canlnac.course.entity.Comment;
import cn.canlnac.course.service.CommentService;
import cn.canlnac.course.service.LikeService;
import cn.canlnac.course.util.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 评论点赞
 */
@Component("CommentLikeController")
@RestController
public class LikeController {
    @Autowired
    LikeService likeService;

    @Autowired
    CommentService commentService;

    @Autowired
    JWT jwt;

    /**
     * 点赞评论
     * @param Authentication    登录信息
     * @param commentId         评论ID
     * @return                  空对象
     */
    @PostMapping("/comment/{commentId}/like")
    public ResponseEntity<Map<String, Object>> like(
            @RequestHeader(value="Authentication", required = false) String Authentication,
            @PathVariable int commentId
    ) {
        //未登录
        Map<String, Object> auth;
        if (Authentication == null || (auth = jwt.decode(Authentication)) == null) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        //获取评论信息
        Comment comment = commentService.findByID(commentId);
        if(comment == null){ //评论不存在
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        //是否已经点赞了
        int isLike = likeService.isLike(Integer.parseInt(auth.get("id").toString()), "comment", commentId);
        if(isLike>0){  //点赞了
            return new ResponseEntity(HttpStatus.NOT_MODIFIED);
        }

        //点赞评论
        int likeCount = likeService.create("comment", commentId, Integer.parseInt(auth.get("id").toString()));
        //点赞失败
        if(likeCount != 1){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //增加评论点赞数
        Comment updateComment = new Comment();

        updateComment.setId(comment.getId());
        updateComment.setLikeCount(1);
        commentService.update(updateComment);   //更新评论

        //返回空对象
        return new ResponseEntity(new HashMap(), HttpStatus.OK);
    }

    /**
     * 取消点赞评论
     * @param Authentication    登录信息
     * @param commentId         评论ID
     * @return                  空对象
     */
    @DeleteMapping("/comment/{commentId}/like")
    public ResponseEntity<Map<String, Object>> unLike(
            @RequestHeader(value="Authentication", required = false) String Authentication,
            @PathVariable int commentId
    ) {
        //未登录
        Map<String, Object> auth;
        if (Authentication == null || (auth = jwt.decode(Authentication)) == null) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        //是否已经点赞了
        int isLike = likeService.isLike(Integer.parseInt(auth.get("id").toString()), "comment", commentId);
        if(isLike<1){  //未点赞
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        //取消点赞评论
        int followingCount = likeService.delete("comment", commentId, Integer.parseInt(auth.get("id").toString()));
        //取消点赞失败
        if(followingCount != 1){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //减少评论点赞数
        Comment comment = commentService.findByID(commentId);
        if(comment != null){
            Comment updateComment = new Comment();

            updateComment.setId(comment.getId());
            updateComment.setLikeCount(-1);
            commentService.update(updateComment);   //更新评论
        }

        //返回空对象
        return new ResponseEntity(new HashMap(), HttpStatus.OK);
    }
}