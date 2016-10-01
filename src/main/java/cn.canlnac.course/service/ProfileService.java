package cn.canlnac.course.service;

import cn.canlnac.course.entity.Profile;

/**
 * 用户资料数据事务接口.
 */
public interface ProfileService {
    /**
     * 创建用户资料
     * @param profile   用户资料
     * @return          用户资料ID
     */
    int create(Profile profile);

    /**
     * 更新用户资料
     * @param profile   用户资料
     * @return          更新数目
     */
    int update(Profile profile);

    /**
     * 根据用户ID获取用户资料
     * @param userID    用户ID
     * @return          用户资料
     */
    Profile findByUserID(int userID);
}
