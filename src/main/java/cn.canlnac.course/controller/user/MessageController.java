package cn.canlnac.course.controller.user;

import cn.canlnac.course.entity.Message;
import cn.canlnac.course.service.impl.MessageServiceImpl;
import cn.canlnac.course.util.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户消息
 */
@Component
@RestController
public class MessageController {

    @Autowired
    MessageServiceImpl messageService;

    @Autowired
    private JWT jwt;

    /*
   * @author wei
   * 获取多条记录
   * return total、messages、id、date、isRead、type、toUser{}、fromUser?{}、actionType?{}、position?{}、content?{}
   * */
    @GetMapping("/user/messages")
    public ResponseEntity<Map<String, Object>> getManyRecord(@RequestBody Map<String, Object> body) {
        HashMap sendData = new HashMap();

        if (Integer.valueOf(body.get("start").toString())<0||Integer.valueOf(body.get("count").toString())<10
                ||(!body.get("isRead").equals("Y")&&!body.get("isRead").equals("N"))){
            //返回400，参数错误
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        if (body.get("userid")==null||body.get("userid")==""){
            //返回401，用户未登录
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

       /* String course="course",chat="chat",comment="comment",system="system",user="user";

        List<Message> list = messageService.getMessages(Integer.valueOf(body.get("start").toString()),Integer.valueOf(body.get("count").toString()),Integer.valueOf(body.get("userid").toString()),course);
        */

        return null;
    }

    /*
   * @author wei
   * 获取指定消息
   * return total、messages、id、date、isRead、type、toUser{}、fromUser?{}、actionType?{}、position?{}、content?{}
   * */
    @GetMapping("/user/message/{id}")
    public ResponseEntity<Map<String, Object>> getAssignRecord(@PathVariable String id,@RequestBody Map<String, Object> body) {
        HashMap sendData = new HashMap();

        if(id==""||id==null){
            //返回400,参数错误
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        if (body.get("userid")==null||body.get("userid")==""){
            //返回401，用户未登录
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }



        return null;
    }

    /*
   * @author wei
   * 删除自己的通知
   * return {}
   * */
    @DeleteMapping("/user/message/{id}")
    public ResponseEntity<Map<String, Object>> deleteMesses(@PathVariable String id, @RequestBody Map<String, Object> body) {

        if(id==""||id==null){
            //返回400,参数错误
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        if (body.get("userid")==null||body.get("userid")==""){
            //返回401，用户未登录
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        int i=messageService.delete(Integer.valueOf(id));

        if (i==0){
            //返回404,没有该消息
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }


        return new ResponseEntity(HttpStatus.OK);
    }

}