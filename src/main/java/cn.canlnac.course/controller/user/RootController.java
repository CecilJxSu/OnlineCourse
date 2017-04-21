package cn.canlnac.course.controller.user;

import cn.canlnac.course.entity.Message;
import cn.canlnac.course.entity.User;
import cn.canlnac.course.service.MessageService;
import cn.canlnac.course.service.UserService;
import cn.canlnac.course.util.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户
 */
@Component("UserRootController")
@RestController
public class RootController {
    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private JWT jwt;

    /**
     * 用户注册
     * @param body  请求数据
     * @return      用户ID
     */
    @PostMapping("/user")
    public ResponseEntity register(@RequestBody Map body) {
        //检查参数是否合法
        if(body.get("username") == null || body.get("password") == null || body.get("userStatus") == null)
        {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        //创建用户
        User user = new User();
        //设置用户的信息
        user.setUsername(body.get("username").toString());
        user.setPassword(body.get("password").toString());
        user.setUserStatus(body.get("userStatus").toString());

        //创建用户
        try {
            userService.create(user);
        } catch (Exception e) {
            //用户名已经被注册
            if(e.getCause().toString().contains("Duplicate")) {
                return new ResponseEntity(HttpStatus.CONFLICT);
            } else {//创建数目不为1，应该返回500，服务器错误
                return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
            }

        }

        try {
            Message message = new Message();

            message.setDate(new Date());
            message.setIsRead('N');
            message.setContent("欢迎你注册！");
            message.setToUserId(user.getId());
            message.setType("system");

            messageService.create(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //创建返回数据
        HashMap sendData = new HashMap();
        //设置返回数据，用户ID
        sendData.put("userID", user.getId());

        //返回注册用户数据
        return new ResponseEntity(sendData, HttpStatus.OK);
    }

    /**
     * 修改密码
     * @param Authentication    登录信息
     * @param body              请求数据
     * @return                  空对象
     */
    @PutMapping("/user")
    public ResponseEntity changePwd(
            @RequestHeader(value="Authentication", required = false) String Authentication,
            @RequestBody Map body
    ) {
        //检查参数是否合法
        if(body.get("old") == null || body.get("new") == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        Map auth;
        //未登录
        if(Authentication == null || (auth = jwt.decode(Authentication)) == null){
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        //获取用户
        User user = userService.findByID(Integer.parseInt(auth.get("id").toString()));
        //验证密码
        if(!user.getPassword().equals(body.get("old").toString())){
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        //修改密码
        user.setPassword(body.get("new").toString());
        //更新密码
        int updatedCount = userService.update(user);

        //更新不成功
        if(updatedCount != 1){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //返回空对象
        return new ResponseEntity(new HashMap(), HttpStatus.OK);
    }

    /**
     * 封号以及永久封号
     * @param Authentication    登录信息
     * @param body              请求数据
     * @param userId            用户ID
     * @return                  空对象
     */
    @DeleteMapping("/user/{userId}")
    public ResponseEntity deleteUser(
            @RequestHeader(value="Authentication", required = false) String Authentication,
            @RequestBody Map body,
            @PathVariable int userId
    ) {
        //未登录
        Map auth;
        if(Authentication == null || (auth = jwt.decode(Authentication)) == null){
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        //不是管理员
        if (!auth.get("userStatus").toString().equals("admin")) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        //获取用户
        User user = userService.findByID(userId);
        //用户不存在
        if (user == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        //检查参数
        if (body.get("infinity") == null || (!(boolean)body.get("infinity") && body.get("lockEndDate") == null)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        //封号
        if ((boolean)body.get("infinity")) {
            //设置永久封号
            user.setStatus("dead");
        } else {
            //临时封号
            user.setStatus("lock");
            user.setLockDate(new Date());
            user.setLockEndDate(new Date(Long.parseLong(body.get("lockEndDate").toString())));
        }

        //更新用户状态
        int updatedCount = userService.update(user);

        //更新不成功
        if(updatedCount != 1){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //返回空对象
        return new ResponseEntity(new HashMap(), HttpStatus.OK);
    }
}