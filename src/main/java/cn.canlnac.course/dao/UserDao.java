package cn.canlnac.course.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * Created by cecil on 2016/9/20.
 */
@Component
public interface UserDao {
    int create(@Param("username") String username, @Param("password") String password, @Param("userStatus") String userStatus);
}
