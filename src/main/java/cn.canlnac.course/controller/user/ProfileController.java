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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户资料
 */
@Component
@RestController
public class ProfileController {

    @Autowired
    UserServiceImpl userService;
    @Autowired
    ProfileServiceImpl profileService;

    @Autowired
    private JWT jwt;

    /*
   * @author wei
   * 获取别人自己资料
   * return nickname、gender、userStatus、iconUrl、universityId、realname、phone、email、department、major
   * 、dormitoryAddress、isFollowing?
   * */
    @GetMapping("/user/{userId}/profile")
    public ResponseEntity<Map<String, Object>> otherProfile(@RequestBody Map<String, Object> body){
        HashMap sendData = new HashMap();

        if (body.get("id")==null||body.get("id")==""){
            //返回401，用户未登录
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        User u = userService.findByID(Integer.valueOf((body.get("id").toString())));
        if (u==null){
            //返回404,用户不存在
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        User user = userService.findByID(Integer.valueOf((body.get("userId").toString())));
        if (user==null){
            //返回404,用户不存在
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        Profile profile = profileService.findByUserID(user.getId());
        if (profile==null){
            //返回500，系统错误
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        sendData.put("nickname",profile.getNickname());
        sendData.put("gender",profile.getGender());
        sendData.put("userStatus",user.getStatus());
        sendData.put("iconUrl",profile.getIconUrl());
        sendData.put("universityId",profile.getUniversityId());
        sendData.put("realname",profile.getRealname());
        sendData.put("phone",profile.getPhone());
        sendData.put("email",profile.getEmail());
        sendData.put("department",profile.getDepartment());
        sendData.put("major",profile.getMajor());
        sendData.put("dormitoryAddress",profile.getDormitoryAddress());
        //isFollowing，是否关注别人
        sendData.put("isFollowing?","true");

        //生成JWT
        String auth = jwt.sign(sendData);

        //获取别人数据返回成功
        return new ResponseEntity(sendData,HttpStatus.OK);
    }


    /*
  * @author wei
  * 获取自己资料
  * return nickname、gender、userStatus、iconUrl、universityId、realname、phone、email、department、
  * major、dormitoryAddress
  * */
    @GetMapping("/user/profile")
    public ResponseEntity<Map<String, Object>> myselfProfile(@RequestBody Map<String, Object> body){
        HashMap sendData = new HashMap();

        if (body.get("id")==null||body.get("id")==""){
            //返回401，用户未登录
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        User user = userService.findByID(Integer.valueOf((body.get("id").toString())));
        if (user==null){
            //返回404,用户不存在
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        Profile profile = profileService.findByUserID(user.getId());
        if (profile==null){
            //返回500，系统错误
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        sendData.put("nickname",profile.getNickname());
        sendData.put("gender",profile.getGender());
        sendData.put("userStatus",user.getStatus());
        sendData.put("iconUrl",profile.getIconUrl());
        sendData.put("universityId",profile.getUniversityId());
        sendData.put("realname",profile.getRealname());
        sendData.put("phone",profile.getPhone());
        sendData.put("email",profile.getEmail());
        sendData.put("department",profile.getDepartment());
        sendData.put("major",profile.getMajor());
        sendData.put("dormitoryAddress",profile.getDormitoryAddress());

        //生成JWT
        String auth = jwt.sign(sendData);

        //获取自己数据返回成功
        return new ResponseEntity(sendData,HttpStatus.OK);
    }


    /*
    * @author wei
    * 修改自己个人资料
    * return  {}
    * */
    @PutMapping("/user/profile")
    public ResponseEntity<Map<String, Object>> modify(@RequestBody Map<String, Object> body){
        HashMap sendData = new HashMap();

        if (body.get("id")==null||body.get("id")==""){
            //返回401，用户未登录
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        User u = userService.findByID(Integer.valueOf((body.get("id").toString())));
        if (u==null){
            //返回404,用户不存在
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        Profile p = profileService.findByUserID(u.getId());
        if (p==null){
            //返回500，系统错误
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Profile profile = new Profile();
        profile.setUserId(Integer.valueOf((body.get("id").toString())));
        profile.setUniversityId(body.get("universityId?").toString());
        profile.setNickname(body.get("nickname?").toString());
        profile.setRealname(body.get("realname?").toString());
        profile.setGender(body.get("gender?").toString());
        profile.setIconUrl(body.get("iconUrl?").toString());
        profile.setPhone(body.get("phone?").toString());
        profile.setEmail(body.get("email?").toString());
        profile.setDepartment(body.get("department?").toString());
        profile.setMajor(body.get("major?").toString());
        profile.setDormitoryAddress(body.get("dormitoryAddress?").toString());

        int i=profileService.update(profile);
        if (i<=0){
            //返回500，系统错误
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //返回修改成功
        return new ResponseEntity(HttpStatus.OK);
    }
}