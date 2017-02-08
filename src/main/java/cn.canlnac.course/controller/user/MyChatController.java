package cn.canlnac.course.controller.user;

import cn.canlnac.course.entity.Chat;
import cn.canlnac.course.service.ChatService;
import cn.canlnac.course.util.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的话题
 */
@Component("MyChatController")
@RestController
public class MyChatController {

    @Autowired
    private ChatService chatService;

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
        List<Chat> chats = chatService.getList(
            start,
            count,
            "date",
            conditions
        );

        //创建返回数据
        HashMap<String,Object> sendData = new HashMap<>();
        sendData.put("total", total);
        sendData.put("chats", chats);

        //返回我的话题数据
        return new ResponseEntity<>(sendData, HttpStatus.OK);
    }
}