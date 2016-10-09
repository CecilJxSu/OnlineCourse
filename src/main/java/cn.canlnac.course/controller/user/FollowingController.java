package cn.canlnac.course.controller.user;

import cn.canlnac.course.entity.Profile;
import cn.canlnac.course.service.impl.FollowingServiceImpl;
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
 * 用户关注
 */
@Component
@RestController
public class FollowingController {

    @Autowired
    private FollowingServiceImpl followingService;

    @Autowired
    private JWT jwt;

     /*
       * @author wei
       * 获取自己的关注用户
       * return total:number、users:User[id:、name:、iconUrl:]
       * */
     @GetMapping("/user/following")
    public ResponseEntity<Map<String, Object>> getOneselfAttentionUser(@RequestBody Map<String, Object> body) {
         HashMap sendData = new HashMap();

         if (Integer.valueOf(body.get("start").toString())<0||Integer.valueOf(body.get("count").toString())<10){
             //返回400，参数错误
             return new ResponseEntity(HttpStatus.BAD_REQUEST);
         }

         if (body.get("userid")==null||body.get("userid")==""){
             //返回401，用户未登录
             return new ResponseEntity(HttpStatus.UNAUTHORIZED);
         }

         List<Profile> list = followingService.getFollowingUsers(Integer.valueOf(body.get("start").toString()),Integer.valueOf(body.get("count").toString())
                 , Integer.valueOf(body.get("userid").toString()));

         if (list==null){
             //返回404,没有关注用户
             return new ResponseEntity(HttpStatus.NOT_FOUND);
         }

         int total = followingService.countFollowing(Integer.valueOf(body.get("userid").toString()));

         sendData.put("total",total);
         sendData.put("User",list);


         //生成JWT
         String auth = jwt.sign(sendData);

         return new ResponseEntity(sendData,HttpStatus.OK);
     }


     /*
       * @author wei
       * 用户的粉丝
       * 获取自己的粉丝
       * return total:number、users:User[id:、name:、iconUrl:]
       * */
     @GetMapping("/user/follow")
     public ResponseEntity<Map<String, Object>> getOneselfFans(@RequestBody Map<String, Object> body) {
         HashMap sendData = new HashMap();

         if (Integer.valueOf(body.get("start").toString())<0||Integer.valueOf(body.get("count").toString())<10){
             //返回400，参数错误
             return new ResponseEntity(HttpStatus.BAD_REQUEST);
         }

         if (body.get("userid")==null||body.get("userid")==""){
             //返回401，用户未登录
             return new ResponseEntity(HttpStatus.UNAUTHORIZED);
         }

         List<Profile> list = followingService.getFollowerUsers(Integer.valueOf(body.get("start").toString()),Integer.valueOf(body.get("count").toString())
                 , Integer.valueOf(body.get("userid").toString()));

         if (list==null){
             //返回404,没有关注用户
             return new ResponseEntity(HttpStatus.NOT_FOUND);
         }

         int total = followingService.countFollower(Integer.valueOf(body.get("userid").toString()));

         sendData.put("total",total);
         sendData.put("User",list);

         //生成JWT
         String auth = jwt.sign(sendData);

         return new ResponseEntity(sendData,HttpStatus.OK);
     }

    /*
       * @author wei
       * 用户关注接口
       * 关注该用户
       * return {}
       * */
    @PostMapping("/user/{id}/following")
    public ResponseEntity<Map<String, Object>> attentionUser(@PathVariable String id, @RequestBody Map<String, Object> body) {
        HashMap sendData = new HashMap();

        if(id==""||id==null){
            //返回400,参数错误
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        if (body.get("userid")==null||body.get("userid")==""){
            //返回401，用户未登录
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        int i = followingService.create(Integer.valueOf(id),Integer.valueOf(body.get("userid").toString()));

        if (i<=0){
            //返回404，用户不存在
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    /*
       * @author wei
       * 取消关注
       * return {}
       * */
    @DeleteMapping("/user/{id}/following")
    public ResponseEntity<Map<String, Object>> deteleUser(@PathVariable String id, @RequestBody Map<String, Object> body) {
        HashMap sendData = new HashMap();

        if(id==""||id==null){
            //返回400,参数错误
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        if (body.get("userid")==null||body.get("userid")==""){
            //返回401，用户未登录
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        int i =followingService.delete(Integer.valueOf(id),Integer.valueOf(body.get("userid").toString()));

        if (i<=0){
            //返回404，用户不存在
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(HttpStatus.OK);
    }
}