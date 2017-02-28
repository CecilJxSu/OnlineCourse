package cn.canlnac.course.controller.user;

import cn.canlnac.course.entity.*;
import cn.canlnac.course.service.*;
import cn.canlnac.course.util.JWT;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 用户消息
 */
@Component
@RestController
public class MessageController {
    @Autowired
    MessageService messageService;
    @Autowired
    ProfileService profileService;

    @Autowired
    CourseService courseService;
    @Autowired
    ChatService chatService;
    @Autowired
    CommentService commentService;
    @Autowired
    ReplyService replyService;

    @Autowired
    JWT jwt;

    /**
     * 获取用户的消息
     * @param Authentication    登录信息
     * @param start             分页开始位置
     * @param count             分页返回数目
     * @param isRead            已读和未读：Y | N
     * @return                  用户消息
     */
    @GetMapping("/user/messages")
    public ResponseEntity<Map<String, Object>> getMessages(
            @RequestHeader(value="Authentication", required = false) String Authentication,
            @RequestParam(value="start", defaultValue="0") int start,
            @RequestParam(value="count", defaultValue="10") int count,
            @RequestParam(value="isRead", defaultValue="N") String isRead
    ) {
        //未登录
        Map<String,Object> auth;
        if(Authentication == null || (auth = jwt.decode(Authentication)) == null){
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        //验证参数
        if(start < 0 || count < 1 || !Arrays.asList("Y","N").contains(isRead)){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        //统计消息数目
        int total = messageService.count(Integer.parseInt(auth.get("id").toString()), isRead);
        if(total<1){//没有消息
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        //获取消息
        List<Message> messages = messageService.getMessages(
                start,
                count,
                Integer.parseInt(auth.get("id").toString()),
                isRead
        );

        //返回的消息
        List<Map<String,Object>> msgs = new ArrayList();
        //处理消息，扩展toUser，fromUser和position的值
        for (Message message: messages){
            //重新构造消息格式
            Map<String,Object> msg = new HashedMap();

            //设置基本的字段
            msg.put("id",message.getId());
            msg.put("date",message.getDate());
            msg.put("type",message.getType());

            //扩展toUser的信息
            Map<String,Object> toUser = new HashedMap();
            toUser.put("id",auth.get("id"));
            toUser.put("name",auth.get("nickname"));
            toUser.put("iconUrl",auth.get("iconUrl"));
            msg.put("toUser",toUser);

            //扩展fromUser的信息
            if (message.getFromUserId() > 0){
                Profile profile = profileService.findByUserID(message.getFromUserId());
                if(profile != null){
                    Map<String,Object> fromUser = new HashedMap();
                    fromUser.put("id",message.getFromUserId());
                    fromUser.put("name",profile.getNickname());
                    fromUser.put("iconUrl",profile.getIconUrl());
                    msg.put("fromUser",fromUser);
                }
            }

            //设置基本的字段
            msg.put("actionType",message.getActionType());

            //扩展position的信息
            if (message.getPositionId() > 0){
                switch (message.getType()){
                    case "course":
                        Course course = courseService.findByID(message.getPositionId());
                        if(course != null){
                            Map<String,Object> position = new HashedMap();
                            position.put("id",message.getPositionId());
                            position.put("name",course.getName());
                            msg.put("position",position);
                        }
                        break;
                    case "chat":
                        Chat chat = chatService.findByID(message.getPositionId());
                        if(chat != null){
                            Map<String,Object> position = new HashedMap();
                            position.put("id",message.getPositionId());
                            position.put("name",chat.getTitle());
                            msg.put("position",position);
                        }
                        break;
                    case "comment":
                        Comment comment = commentService.findByID(message.getPositionId());
                        if(comment != null){
                            Map<String,Object> position = new HashedMap();
                            position.put("id",message.getPositionId());
                            position.put("name",comment.getContent());
                            msg.put("position",position);
                        }
                        break;
                }
            }

            //设置基本的字段
            msg.put("content",message.getContent());

            //添加到返回的数据中
            msgs.add(msg);
        }

        //创建返回数据
        HashMap<String,Object> sendData = new HashMap();
        sendData.put("total", total);
        sendData.put("messages", msgs);

        //返回用户的消息
        return new ResponseEntity(sendData, HttpStatus.OK);
    }

    /**
     * 获取用户的、指定的消息
     * @param Authentication    登录信息
     * @param id                消息ID
     * @return                  用户消息
     */
    @GetMapping("/user/message/{id}")
    public ResponseEntity<Map<String, Object>> getMessage(
            @RequestHeader(value="Authentication", required = false) String Authentication,
            @PathVariable int id
    ) {
        //未登录
        Map<String, Object> auth;
        if (Authentication == null || (auth = jwt.decode(Authentication)) == null) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        Message message = messageService.findByID(id);
        //没有该消息或者消息不属于自己的
        if(message == null || message.getToUserId() != Integer.parseInt(auth.get("id").toString())){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        //设置消息的状态为已读
        messageService.setRead(id);

        //创建返回数据
        HashMap<String,Object> sendData = new HashMap();

        //设置基本的字段
        sendData.put("id",message.getId());
        sendData.put("date",message.getDate());
        sendData.put("type",message.getType());

        //扩展toUser的信息
        Map<String,Object> toUser = new HashedMap();
        toUser.put("id",auth.get("id"));
        toUser.put("name",auth.get("nickname"));
        toUser.put("iconUrl",auth.get("iconUrl"));
        sendData.put("toUser",toUser);

        //扩展fromUser的信息
        if (message.getFromUserId() > 0){
            Profile profile = profileService.findByUserID(message.getFromUserId());
            if(profile != null){
                Map<String,Object> fromUser = new HashedMap();
                fromUser.put("id",message.getFromUserId());
                fromUser.put("name",profile.getNickname());
                fromUser.put("iconUrl",profile.getIconUrl());
                sendData.put("fromUser",fromUser);
            }
        }

        //设置基本的字段
        sendData.put("actionType",message.getActionType());

        //扩展position的信息
        if (message.getPositionId() > 0){
            switch (message.getType()){
                case "course":
                    Course course = courseService.findByID(message.getPositionId());
                    if(course != null){
                        Map<String,Object> position = new HashedMap();
                        position.put("id",message.getPositionId());
                        position.put("name",course.getName());
                        sendData.put("position",position);
                    }
                    break;
                case "chat":
                    Chat chat = chatService.findByID(message.getPositionId());
                    if(chat != null){
                        Map<String,Object> position = new HashedMap();
                        position.put("id",message.getPositionId());
                        position.put("name",chat.getTitle());
                        sendData.put("position",position);
                    }
                    break;
                case "comment":
                    Comment comment = commentService.findByID(message.getPositionId());
                    if(comment != null){
                        Map<String,Object> position = new HashedMap();
                        position.put("id",message.getPositionId());
                        position.put("name",comment.getContent());
                        sendData.put("position",position);
                    }
                    break;
            }
        }

        //设置基本的字段
        sendData.put("content",message.getContent());

        //返回用户的消息
        return new ResponseEntity(sendData, HttpStatus.OK);
    }

    /**
     * 删除用户的、指定的消息
     * @param Authentication    登录信息
     * @param id                消息ID
     * @return                  空对象
     */
    @DeleteMapping("/user/message/{id}")
    public ResponseEntity<Map<String, Object>> deleteMessage(
            @RequestHeader(value="Authentication", required = false) String Authentication,
            @PathVariable int id
    ) {
        //未登录
        Map<String, Object> auth;
        if (Authentication == null || (auth = jwt.decode(Authentication)) == null) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        Message message = messageService.findByID(id);
        //没有该消息或者消息不属于自己的
        if(message == null || message.getToUserId() != Integer.parseInt(auth.get("id").toString())){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        //删除该消息
        int deletedCount = messageService.delete(id);

        //删除失败
        if(deletedCount != 1) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //创建返回数据
        HashMap<String,Object> sendData = new HashMap();

        //返回空对象
        return new ResponseEntity(sendData, HttpStatus.OK);
    }
}