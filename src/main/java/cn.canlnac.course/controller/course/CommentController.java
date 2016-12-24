package cn.canlnac.course.controller.course;

import cn.canlnac.course.entity.*;
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
 * 课程评论
 */
@Component("CourseCommentController")
@RestController
public class CommentController {
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
     * 获取课程下的评论
     * @param Authentication    登录信息
     * @param start             分页开始位置
     * @param count             分页返回数目
     * @param sort              按日期排序：date；按热门排序：rank
     * @param courseId          课程ID
     * @return                  评论列表
     */
    @GetMapping("/course/{courseId}/comments")
    public ResponseEntity<Map<String, Object>> getComments(
            @RequestHeader(value="Authentication", required = false) String Authentication,
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "10") int count,
            @RequestParam(defaultValue = "date") String sort,
            @PathVariable int courseId
    ) {
        //处理登录信息
        Map<String, Object> auth = jwt.decode(Authentication);

        //参数验证
        if(start<0 || count<1 || !Arrays.asList("date","rank").contains(sort)){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        //统计评论数目
        int total = commentService.count("course",courseId);
        if(total < 1){  //没有评论
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        //获取评论
        List<Comment> commentList = commentService.getList(start, count, sort, "course", courseId);

        //返回的Object格式评论
        List<Map<String,Object>> comments = new ArrayList();
        //处理评论
        for(Comment comment:commentList){
            Map<String,Object> commentObj = new HashMap();
            commentObj.put("id",comment.getId());
            commentObj.put("date",comment.getDate());

            //设置评论作者
            Profile authorProfile = profileService.findByUserID(comment.getUserId());
            if (authorProfile != null){
                Map<String,Object> author = new HashMap();
                author.put("id",authorProfile.getUserId());
                author.put("name",authorProfile.getNickname());
                author.put("iconUrl",authorProfile.getIconUrl());

                commentObj.put("author",author);
            }

            commentObj.put("content",comment.getContent());

            //评论图片
            List<String> pictureUrls = new ArrayList();
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
                List<Map<String,Object>> replies = new ArrayList();
                //处理回复
                for (Reply reply:replyList){
                    Map<String,Object> replyObj = new HashMap();
                    replyObj.put("id",reply.getId());
                    replyObj.put("date",reply.getDate());

                    //处理回复用户
                    Profile toUserProfile = profileService.findByUserID(reply.getToUserId());
                    if(toUserProfile != null){
                        Map<String,Object> toUser = new HashMap();
                        toUser.put("id",toUserProfile.getUserId());
                        toUser.put("name",toUserProfile.getNickname());
                        toUser.put("iconUrl",toUserProfile.getIconUrl());

                        replyObj.put("toUser",toUser);
                    }

                    //处理回复的作者
                    Profile userProfile = profileService.findByUserID(reply.getUserId());
                    if(userProfile != null){
                        Map<String,Object> author = new HashMap();
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
                commentObj.put("isLike",(isLike>0)?true:false);
            } else {    //未登录
                commentObj.put("isLike",false);
            }

            //添加评论到返回数据中
            comments.add(commentObj);
        }

        //创建返回数据
        HashMap<String,Object> sendData = new HashMap();
        sendData.put("total", total);
        sendData.put("comments", comments);

        //返回评论列表
        return new ResponseEntity(sendData, HttpStatus.OK);
    }

    /**
     * 创建课程下的评论
     * @param Authentication    登录信息
     * @param body              请求数据
     * @param courseId          课程ID
     * @return                  评论ID
     */
    @PostMapping("/course/{courseId}/comment")
    public ResponseEntity<Map<String, Object>> createComment(
            @RequestHeader(value="Authentication", required = false) String Authentication,
            @RequestBody Map<String, Object> body,
            @PathVariable int courseId
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
        if(body.get("content") == null){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        //课程是否存在
        Course course = courseService.findByID(courseId);
        if(course == null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        //评论
        Comment comment = new Comment();
        comment.setContent(body.get("content").toString());
        comment.setPictureUrls(pictureUrls.toString());
        comment.setTargetId(courseId);
        comment.setTargetType("course");
        comment.setUserId(Integer.parseInt(auth.get("id").toString()));

        //创建评论
        int createdId = commentService.create(comment);
        if (createdId != 1){ //创建失败
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //更新课程评论数
        Course updateCourse = new Course();

        updateCourse.setId(course.getId());
        updateCourse.setCommentCount(3);
        courseService.update(updateCourse);

        //创建返回数据
        HashMap<String,Object> sendData = new HashMap();
        sendData.put("commentId",comment.getId());

        //返回评论ID
        return new ResponseEntity(sendData, HttpStatus.OK);
    }

    /**
     * 回复课程下的评论
     * @param Authentication    登录信息
     * @param body              请求数据
     * @param courseId          课程ID
     * @param commentId         评论ID
     * @return                  回复ID
     */
    @PostMapping("/course/{courseId}/comment/{commentId}")
    public ResponseEntity<Map<String, Object>> createReply(
            @RequestHeader(value="Authentication", required = false) String Authentication,
            @RequestBody Map<String, Object> body,
            @PathVariable int courseId,
            @PathVariable int commentId
    ) {
        //未登录
        Map<String, Object> auth;
        if (Authentication == null || (auth = jwt.decode(Authentication)) == null) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        //参数验证
        if(body.get("content") == null){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        //课程是否存在
        Course course = courseService.findByID(courseId);
        if(course == null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        //评论是否存在
        Comment comment = commentService.findByID(commentId);
        if(comment == null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        //回复
        Reply reply = new Reply();
        reply.setCommentId(commentId);
        reply.setContent(body.get("content").toString());
        reply.setUserId(Integer.parseInt(auth.get("id").toString()));

        //回复用户
        if(body.get("toUserId") != null){
            User user = userService.findByID(Integer.parseInt(body.get("toUserId").toString()));
            if(user == null){
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }

            reply.setToUserId(Integer.parseInt(body.get("toUserId").toString()));
        }

        int createdId = replyService.create(reply);   //创建回复

        //创建回复失败
        if(createdId != 1){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Comment updateComment = new Comment();

        updateComment.setId(comment.getId());
        updateComment.setReplyCount(2);
        commentService.update(updateComment); //更新评论的回复数

        //创建返回数据
        HashMap<String,Object> sendData = new HashMap();
        sendData.put("replyId",reply.getId());

        //返回回复ID
        return new ResponseEntity(sendData, HttpStatus.OK);
    }
}