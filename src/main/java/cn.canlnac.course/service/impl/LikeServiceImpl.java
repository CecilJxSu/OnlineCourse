package cn.canlnac.course.service.impl;

import cn.canlnac.course.dao.LikeDao;
import cn.canlnac.course.entity.Profile;
import cn.canlnac.course.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 收藏记录数据事务接口实现
 */
@Transactional
@Component(value = "LikeService")
public class LikeServiceImpl implements LikeService {
    @Autowired
    private LikeDao likeDao;

    /**
     * 创建点赞记录
     * @param targetType    点赞类型，课程：course；话题：chat；评论：comment
     * @param targetId      点赞ID
     * @param userId        用户ID
     * @return              创建ID
     */
    @Override
    public int create(String targetType, int targetId, int userId) {
        return likeDao.create(targetType, targetId, userId);
    }

    /**
     * 取消点赞记录
     * @param targetType    点赞类型，课程：course；话题：chat；评论：comment
     * @param targetId      点赞ID
     * @param userId        用户ID
     * @return              删除记录数
     */
    @Override
    public int delete(String targetType, int targetId, int userId) {
        return likeDao.delete(targetType, targetId, userId);
    }

    /**
     * 统计该（课程/话题/评论）下的用户点赞数
     * @param targetType    点赞类型，课程：course；话题：chat；评论：comment
     * @param targetId      点赞目标ID
     * @return              用户点赞数
     */
    @Override
    public int count(String targetType, int targetId) {
        return likeDao.count(targetType, targetId);
    }

    /**
     * 获取该（课程/话题/评论）下的点赞用户资料
     * @param start         分页位置开始
     * @param count         分页返回数目
     * @param targetType    点赞类型，课程：course；话题：chat；评论：comment
     * @param targetId      点赞目标ID
     * @return              用户资料
     */
    @Override
    public List<Profile> getUsers(int start, int count, String targetType, int targetId) {
        return likeDao.getUsers(start, count, targetType, targetId);
    }

    /**
     * 用户是否点赞了该对象
     * @param userId        用户ID
     * @param targetType    点赞类型，课程：course；话题：chat；评论：comment
     * @param targetId      目标ID
     * @return              0：未关注，1：已关注
     */
    @Override
    public int isLike(int userId, String targetType, int targetId){
         return likeDao.isLike(userId, targetType, targetId);
    }
}
