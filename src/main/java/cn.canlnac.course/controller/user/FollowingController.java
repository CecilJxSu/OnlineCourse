package cn.canlnac.course.controller.user;

import cn.canlnac.course.entity.Profile;
import cn.canlnac.course.entity.User;
import cn.canlnac.course.service.FollowingService;
import cn.canlnac.course.service.UserService;
import cn.canlnac.course.util.JWT;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    UserService userService;

    @Autowired
    FollowingService followingService;

    @Autowired
    JWT jwt;

    /**
     * 获取自己的粉丝
     * @param Authentication    登录信息
     * @param start             分页开始位置
     * @param count             分页返回数目
     * @return                  粉丝用户列表
     */
    @GetMapping("/user/follower")
    public ResponseEntity<Map<String, Object>> getFollowers(
            @RequestHeader(value="Authentication", required = false) String Authentication,
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "10") int count
    ) {
        //未登录
        Map<String, Object> auth;
        if (Authentication == null || (auth = jwt.decode(Authentication)) == null) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        //参数验证
        if(start<0 || count<1){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        //统计粉丝数
        int followerCount = followingService.countFollower(Integer.parseInt(auth.get("id").toString()));
        //没有
        if(followerCount < 1) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        //获取粉丝用户列表
        List<Profile> profiles = followingService.getFollowerUsers(start, count, Integer.parseInt(auth.get("id").toString()));

        //用户信息
        List<Map<String,Object>> users = new ArrayList();
        //返回精简的用户信息
        for (Profile profile: profiles){
            Map<String,Object> user = new HashedMap();
            user.put("id",profile.getUserId());
            user.put("name",profile.getNickname());
            user.put("iconUrl",profile.getIconUrl());

            users.add(user);
        }

        //创建返回数据
        HashMap<String,Object> sendData = new HashMap();
        sendData.put("total",followerCount);
        sendData.put("users",users);

        //返回粉丝用户列表
        return new ResponseEntity(sendData, HttpStatus.OK);
    }

    /**
     * 获取用户的关注
     * @param start     分页开始位置
     * @param count     分页返回数目
     * @param userId    用户ID
     * @return          关注用户列表
     */
    @GetMapping("/user/{userId}/following")
    public ResponseEntity<Map<String, Object>> getFollowings(
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "10") int count,
            @PathVariable int userId
    ) {
        //参数验证
        if(start<0 || count<1){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        //统计关注数
        int followingCount = followingService.countFollowing(userId);
        //没有
        if(followingCount < 1) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        //获取关注用户列表
        List<Profile> profiles = followingService.getFollowingUsers(start, count, userId);

        //用户信息
        List<Map<String,Object>> users = new ArrayList();
        //返回精简的用户信息
        for (Profile profile: profiles){
            Map<String,Object> user = new HashedMap();
            user.put("id",profile.getUserId());
            user.put("name",profile.getNickname());
            user.put("iconUrl",profile.getIconUrl());

            users.add(user);
        }

        //创建返回数据
        HashMap<String,Object> sendData = new HashMap();
        sendData.put("total",followingCount);
        sendData.put("users",users);

        //返回关注用户列表
        return new ResponseEntity(sendData, HttpStatus.OK);
    }

    /**
     * 关注用户
     * @param Authentication    登录信息
     * @param userId            用户ID
     * @return                  空对象
     */
    @PostMapping("/user/{userId}/following")
    public ResponseEntity<Map<String, Object>> following(
            @RequestHeader(value="Authentication", required = false) String Authentication,
            @PathVariable int userId
    ) {
        //未登录
        Map<String, Object> auth;
        if (Authentication == null || (auth = jwt.decode(Authentication)) == null) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        //不能关注自己
        if(userId == Integer.parseInt(auth.get("id").toString())){
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        //用户是否存在
        User user = userService.findByID(userId);
        if(user == null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        //是否已经关注了
        int isFollowing = followingService.isFollowing(Integer.parseInt(auth.get("id").toString()), userId);
        if(isFollowing>0){  //关注了
            return new ResponseEntity(HttpStatus.NOT_MODIFIED);
        }

        //关注用户
        int followingCount = followingService.create(userId, Integer.parseInt(auth.get("id").toString()));
        //关注失败
        if(followingCount != 1){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //创建返回数据
        HashMap<String,Object> sendData = new HashMap();

        //返回空对象
        return new ResponseEntity(sendData, HttpStatus.OK);
    }

    /**
     * 取消关注用户
     * @param Authentication    登录信息
     * @param userId            用户ID
     * @return                  空对象
     */
    @DeleteMapping("/user/{userId}/following")
    public ResponseEntity<Map<String, Object>> unFollowing(
            @RequestHeader(value="Authentication", required = false) String Authentication,
            @PathVariable int userId
    ) {
        //未登录
        Map<String, Object> auth;
        if (Authentication == null || (auth = jwt.decode(Authentication)) == null) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        //是否已经关注了
        int isFollowing = followingService.isFollowing(Integer.parseInt(auth.get("id").toString()), userId);
        if(isFollowing<1){  //未关注
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        //取消关注用户
        int followingCount = followingService.delete(userId, Integer.parseInt(auth.get("id").toString()));
        //取消关注失败
        if(followingCount != 1){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //创建返回数据
        HashMap<String,Object> sendData = new HashMap();

        //返回空对象
        return new ResponseEntity(sendData, HttpStatus.OK);
    }
}