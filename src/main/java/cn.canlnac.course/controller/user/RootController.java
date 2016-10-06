package cn.canlnac.course.controller.user;

import cn.canlnac.course.entity.User;
import cn.canlnac.course.service.UserService;
import cn.canlnac.course.util.JWT;
import org.apache.commons.validator.routines.EmailValidator;
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
    private UserService userService;

    @Autowired
    private JWT jwt;

    /**
     * 用户注册
     * @param body  请求数据
     * @return      用户ID
     */
    @PostMapping("/user")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, Object> body) {
        //检查参数是否合法
        if(body.get("username") == null || body.get("password") == null ||
                !EmailValidator.getInstance().isValid(body.get("email").toString()))
        {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        //创建用户
        User user = new User();
        //设置用户的信息
        user.setUsername(body.get("username").toString());
        user.setPassword(body.get("password").toString());
        user.setPassword(body.get("email").toString());

        //创建用户
        int createdCount = userService.create(user);

        //创建数目不为1，应该返回500，内部错误
        if (createdCount != 1){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //创建返回数据
        HashMap<String,Object> sendData = new HashMap();
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
    public ResponseEntity<Map<String, Object>> changePwd(
            @RequestHeader(value="Authentication", required = false) String Authentication,
            @RequestBody Map<String, Object> body
    ) {
        //检查参数是否合法
        if(body.get("old") == null || body.get("new") == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        Map<String,Object> auth;
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

        //创建返回数据
        HashMap<String,Object> sendData = new HashMap();

        //返回空对象
        return new ResponseEntity(sendData, HttpStatus.OK);
    }

    /**
     * 永久封号
     * @param Authentication    登录信息
     * @param userId            用户ID
     * @return                  空对象
     */
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> deleteUser(
            @RequestHeader(value="Authentication", required = false) String Authentication,
            @PathVariable int userId
    ) {
        //未登录
        Map<String,Object> auth;
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

        //设置永久封号
        user.setStatus("dead");
        //更新用户状态
        int updatedCount = userService.update(user);

        //更新不成功
        if(updatedCount != 1){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //创建返回数据
        HashMap<String,Object> sendData = new HashMap();

        //返回空对象
        return new ResponseEntity(sendData, HttpStatus.OK);
    }
}