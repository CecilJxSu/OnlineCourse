package cn.canlnac.course.controller.user;

import cn.canlnac.course.entity.Reply;
import cn.canlnac.course.service.ReplyService;
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
 * 我的回复
 */
@Component("MyReplyController")
@RestController
public class MyReplyController {

    @Autowired
    private ReplyService replyService;

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
        List<Reply> replies = replyService.getOwnReplies(Integer.parseInt(auth.get("id").toString()));
        if(replies == null || replies.size() <= 0 || start > replies.size()) {//没有我的回复
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (start + count > replies.size()) {
            count = replies.size() - start;
        }

        replies = replies.subList(start, start + count);

        //创建返回数据
        HashMap<String,Object> sendData = new HashMap<>();
        sendData.put("total", replies.size());
        sendData.put("replies", replies);

        //返回我的回复数据
        return new ResponseEntity<>(sendData, HttpStatus.OK);
    }
}