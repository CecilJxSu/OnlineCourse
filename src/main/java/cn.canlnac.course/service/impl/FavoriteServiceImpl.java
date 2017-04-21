package cn.canlnac.course.service.impl;

import cn.canlnac.course.dao.FavoriteDao;
import cn.canlnac.course.entity.Favorite;
import cn.canlnac.course.entity.Profile;
import cn.canlnac.course.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 收藏记录数据事务接口实现
 */
@Transactional
@Component(value = "FavoriteService")
public class FavoriteServiceImpl implements FavoriteService {
    @Autowired
    private FavoriteDao favoriteDao;

    /**
     * 创建收藏记录
     * @param targetType    收藏类型，课程：course；话题：chat
     * @param targetId      收藏ID
     * @param userId        用户ID
     * @return              创建ID
     */
    @Override
    public int create(String targetType, int targetId, int userId) {
        return favoriteDao.create(targetType, targetId, userId);
    }

    /**
     * 取消收藏记录
     * @param targetType    收藏类型，课程：course；话题：chat
     * @param targetId      收藏ID
     * @param userId        用户ID
     * @return              删除记录数
     */
    @Override
    public int delete(String targetType, int targetId, int userId) {
        return favoriteDao.delete(targetType, targetId, userId);
    }

    /**
     * 统计该（课程/话题）下的用户收藏数
     * @param targetType    收藏类型，课程：course；话题：chat
     * @param targetId      收藏目标ID
     * @return              用户收藏数
     */
    @Override
    public int count(String targetType, int targetId) {
        return favoriteDao.count(targetType, targetId);
    }

    /**
     * 获取该（课程/话题）下的收藏用户资料
     * @param start         分页位置开始
     * @param count         分页返回数目
     * @param targetType    收藏类型，课程：course；话题：chat
     * @param targetId      收藏目标ID
     * @return              用户资料
     */
    @Override
    public List<Profile> getUsers(int start, int count, String targetType, int targetId) {
        return favoriteDao.getUsers(start, count, targetType, targetId);
    }

    /**
     * 用户是否收藏了该对象
     * @param userId        用户ID
     * @param targetType    收藏类型，课程：course；话题：chat
     * @param targetId      目标ID
     * @return              0：未关注，1：已关注
     */
    @Override
    public int isFavorite(int userId, String targetType, int targetId){
        return favoriteDao.isFavorite(userId, targetType, targetId);
    }

    /**
     * 获取用户收藏数
     * @param userId        用户id
     * @param targetType    收藏类型，课程：course；话题：chat
     * @return
     */
    @Override
    public int countFavorite(int userId, String targetType) {
        return favoriteDao.countFavorite(userId,targetType);
    }

    /**
     * 获取用户收藏
     * @param start         分页位置开始
     * @param count         分页返回数目
     * @param targetType    收藏类型，课程：course；话题：chat
     * @param userId        用户id
     * @return
     */
    @Override
    public List<Favorite> getFavorite(int start, int count, String targetType, int userId) {
        return favoriteDao.getFavorite(start,count,targetType,userId);
    }
}
