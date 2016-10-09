package cn.canlnac.course.controller.user;

import cn.canlnac.course.entity.Profile;
import cn.canlnac.course.entity.User;
import cn.canlnac.course.service.impl.ProfileServiceImpl;
import cn.canlnac.course.service.impl.UserServiceImpl;
import cn.canlnac.course.util.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户
 */
@Component("UserRootController")
@RestController
public class RootController {

    @Autowired
    UserServiceImpl userService;
    @Autowired
    ProfileServiceImpl profileService;

    @Autowired
    private JWT jwt;
    /*
    * @author wei
    * 用户注册
    * return  id
    * */

    @PostMapping("/user")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, Object> body){
        HashMap sendData = new HashMap();

        if (body.get("username")==""||body.get("username")==null||body.get("password")==""||body.get("password")==null||body.get("email")==""||body.get("email")==null){
            //参数错误，返回400
            return  new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        //创建用户
        User user = new User();
        user.setUsername(body.get("username").toString());
        user.setPassword(body.get("password").toString());
        user.setUserStatus("student");
        int i= userService.create(user);
        //用户创建成功时，存储其个人信息
        if (i>0){
            Profile p = new Profile();
            p.setUserId(user.getId());
            p.setEmail(body.get("email").toString());
            profileService.create(p);
        }else {
            //返回500，系统错误
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        sendData.put("userId",user.getId());

        //生成JWT
        String auth = jwt.sign(sendData);
        //用户注册，返回成功
        return new ResponseEntity(sendData, HttpStatus.OK);
    }



    /*
    * @author wei
    * 用户修改密码
    * return  {}
    * */
    @PutMapping("/user")
    public ResponseEntity<Map<String, Object>> modify(@RequestBody Map<String, Object> body){

        if (body.get("old")==""||body.get("old")==null||body.get("new")==""||body.get("new")==null){
            //参数错误，返回400
            return  new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        User u = userService.findByID(Integer.valueOf((body.get("id").toString())));
        if (u==null){
            //返回401，用户未登录
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }else {
            User user = new User();
            user.setPassword(body.get("new").toString());
            userService.update(user);
        }

        //修改返回成功
        return new ResponseEntity(HttpStatus.OK);
    }


    /*
   * @author wei
   * 永久封号
   * return  {}
   * */
    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> detele(@RequestBody Map<String,Object> body){

        if (body.get("userId")==""||body.get("userId")==null){
            //参数错误，返回400
            return  new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        User u = userService.findByID(Integer.valueOf((body.get("id").toString())));
        if (u==null){
            //返回401，用户未登录
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        if (u.getUserStatus()!="admin"){
            //返回403，权限不足
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        if (userService.findByID(Integer.valueOf(body.get("userId").toString()))==null){
            //返回404,用户不存在
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        //修改用户为永久封号
        User user = new User();
        user.setId(Integer.valueOf(body.get("userId").toString()));
        user.setStatus("dead");
        int i=userService.update(user);
        if (i<=0){
            //返回500，系统错误，删除失败
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //永久封号返回成功
        return new ResponseEntity(HttpStatus.OK);
    }
}