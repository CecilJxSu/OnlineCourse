package cn.canlnac.course.controller.user;

import cn.canlnac.course.entity.Profile;
import cn.canlnac.course.entity.User;
import cn.canlnac.course.service.FollowingService;
import cn.canlnac.course.service.ProfileService;
import cn.canlnac.course.service.UserService;
import cn.canlnac.course.util.JWT;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 用户资料
 */
@Component
@RestController
public class ProfileController {
    @Autowired
    ProfileService profileService;

    @Autowired
    UserService userService;

    @Autowired
    FollowingService followingService;

    @Autowired
    JWT jwt;

    /**
     * 获取用户的资料
     * @param Authentication    登录信息
     * @param userId            用户ID
     * @return                  用户资料
     */
    @GetMapping("/user/{userId}/profile")
    public ResponseEntity<Map<String, Object>> getUserProfile(
            @RequestHeader(value="Authentication", required = false) String Authentication,
            @PathVariable int userId
    ) {
        //未登录
        Map<String,Object> auth;
        if(Authentication == null || (auth = jwt.decode(Authentication)) == null){
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        //获取用户
        User user = userService.findByID(userId);
        //用户不存在
        if (user == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        //获取用户资料
        Profile profile = profileService.findByUserID(userId);

        //创建返回数据
        HashMap<String,Object> sendData = new HashMap();
        //设置返回数据
        if(profile != null){
            sendData.put("nickname",profile.getNickname());
            sendData.put("gender",profile.getGender());
            sendData.put("iconUrl",profile.getIconUrl());
            sendData.put("universityId",profile.getUniversityId());
            sendData.put("realname",profile.getRealname());
            sendData.put("phone",profile.getPhone());
            sendData.put("email",profile.getEmail());
            sendData.put("department",profile.getDepartment());
            sendData.put("major",profile.getMajor());
            sendData.put("dormitoryAddress",profile.getDormitoryAddress());
        }
        sendData.put("userStatus",user.getUserStatus());

        //登录用户id != userId
        if(Integer.parseInt(auth.get("id").toString()) != userId){
            //登录用户是否关注了该用户
            int followingCount = followingService.isFollowing(Integer.parseInt(auth.get("id").toString()),userId);
            sendData.put("isFollowing",(followingCount>0)?true:false);
        }

        //返回用户资料
        return new ResponseEntity(sendData, HttpStatus.OK);
    }

    /**
     * 修改个人资料
     * @param Authentication    登录信息
     * @param body              请求数据
     * @return                  空对象
     */
    @PutMapping("/user/profile")
    public ResponseEntity<Map<String, Object>> updateProfile(
            @RequestHeader(value="Authentication", required = false) String Authentication,
            @RequestBody Map<String, Object> body
    ) {
        //未登录
        Map<String,Object> auth;
        if(Authentication == null || (auth = jwt.decode(Authentication)) == null){
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        //请求参数为空
        if(body.size() == 0){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        //更新资料
        Profile profile = profileService.findByUserID(Integer.parseInt(auth.get("id").toString()));
        if(profile == null){
            profile = new Profile();
        }

        //验证每个请求参数是否合法
        Iterator<String> keys = body.keySet().iterator();
        String key;
        Object value;
        while (keys.hasNext()){
            key = keys.next();
            value = body.get(key);
            switch (key){
                case "universityId":
                    if(value == null || value.toString().isEmpty()){
                        return new ResponseEntity(HttpStatus.BAD_REQUEST);
                    }
                    profile.setUniversityId(value.toString());
                    break;
                case "nickname":
                    if(value == null || value.toString().isEmpty()){
                        return new ResponseEntity(HttpStatus.BAD_REQUEST);
                    }
                    profile.setNickname(value.toString());
                    break;
                case "realname":
                    if(value == null || value.toString().isEmpty()){
                        return new ResponseEntity(HttpStatus.BAD_REQUEST);
                    }
                    profile.setRealname(value.toString());
                    break;
                case "iconUrl":
                    if(value == null || value.toString().isEmpty()){
                        return new ResponseEntity(HttpStatus.BAD_REQUEST);
                    }
                    profile.setIconUrl(value.toString());
                    break;
                case "phone":
                    if(value == null || value.toString().isEmpty()){
                        return new ResponseEntity(HttpStatus.BAD_REQUEST);
                    }
                    profile.setPhone(value.toString());
                    break;
                case "department":
                    if(value == null || value.toString().isEmpty()){
                        return new ResponseEntity(HttpStatus.BAD_REQUEST);
                    }
                    profile.setDepartment(value.toString());
                    break;
                case "major":
                    if(value == null || value.toString().isEmpty()){
                        return new ResponseEntity(HttpStatus.BAD_REQUEST);
                    }
                    profile.setMajor(value.toString());
                    break;
                case "dormitoryAddress":
                    if(value == null || value.toString().isEmpty()){
                        return new ResponseEntity(HttpStatus.BAD_REQUEST);
                    }
                    profile.setDormitoryAddress(value.toString());
                    break;
                case "gender":
                    if (!Arrays.asList("male","female").contains(value)){
                        return new ResponseEntity(HttpStatus.BAD_REQUEST);
                    }
                    profile.setGender(value.toString());
                    break;
                case "email":
                    if (!EmailValidator.getInstance().isValid(value.toString())){
                        return new ResponseEntity(HttpStatus.BAD_REQUEST);
                    }
                    profile.setEmail(value.toString());
                    break;
                default:
                    keys.remove();
            }
        }

        //更新或创建用户资料
        int count;
        if(profile.getId() > 0){
            count = profileService.update(profile);
        } else {
            count = profileService.create(profile);
        }

        //更新或创建不成功
        if(count != 1){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //创建返回数据
        HashMap<String,Object> sendData = new HashMap();

        //返回空对象
        return new ResponseEntity(sendData, HttpStatus.OK);
    }
}