package cn.canlnac.course.controller;

import cn.canlnac.course.entity.Profile;
import cn.canlnac.course.entity.User;
import cn.canlnac.course.service.ProfileService;
import cn.canlnac.course.service.UserService;
import cn.canlnac.course.util.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录
 */
@Component
@RestController
public class LoginController {
    @Autowired
    private UserService userService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private JWT jwt;

    /**
     * 登录用户
     * @param body  请求数据
     * @return      用户资料
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, Object> body) {
        //检查参数是否合法
        if(body.get("username") == null || body.get("password") == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        //获取登录用户
        User user = userService.findByUsername(body.get("username").toString());
        //用户不存在
        if(user == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        //用户密码验证错误
        if(!user.getPassword().equals(body.get("password"))){
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        //获取用户资料
        Profile profile = profileService.findByUserID(user.getId());
        //创建返回数据
        HashMap<String,Object> sendData = new HashMap();
        //设置返回数据
        sendData.put("id", user.getId());
        sendData.put("userStatus", user.getUserStatus());
        //设置用户资料
        if(profile != null) {
            sendData.put("nickname", profile.getNickname());
            sendData.put("gender", profile.getGender());
            sendData.put("iconUrl", profile.getIconUrl());
        }

        //生成JWT
        String auth = jwt.sign(sendData);

        //设置headers
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authentication", auth);
        headers.add("Content-Type", "application/json; charset=UTF-8");
        //返回登录用户数据
        return new ResponseEntity(sendData, headers, HttpStatus.OK);
    }
}