package cn.canlnac.course.service.impl;

import cn.canlnac.course.dao.CourseDao;
import cn.canlnac.course.entity.Course;
import cn.canlnac.course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 课程事务接口实现
 */
@Transactional
@Component(value = "CourseService")
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseDao courseDao;

    /**
     * 创建课程
     * @param course    课程
     * @return          创建数目
     */
    @Override
    public int create(Course course) {
        return courseDao.create(course);
    }

    /**
     * 更新课程
     * @param course    课程
     * @return          更新成功数目
     */
    @Override
    public int update(Course course) {
        return courseDao.update(course);
    }

    /**
     * 获取指定课程
     * @param id    课程ID
     * @return      课程
     */
    @Override
    public Course findByID(int id) {
        return courseDao.findByID(id);
    }

    /**
     * 获取作者的课程列表
     * @param userId    作者ID
     * @return          课程列表
     */
    @Override
    public List<Course> findByUserId(int userId) {
        return courseDao.findByUserId(userId);
    }

    /**
     * 统计课程
     * @param conditions    条件：
     *                          status: ("public" | "draft")[]
     *                          userId?：作者ID，可为空
     *                          department：String[]
     *                          search:String 搜索条件
     * @return              课程数目
     */
    @Override
    public int count(Map<String, Object> conditions) {
        return courseDao.count(conditions);
    }

    /**
     * 获取课程列表
     * @param start         分页开始位置
     * @param count         分页返回数目
     * @param sort          排序：日期date，热度rank
     * @param conditions    条件：
     *                          status: ("public" | "draft")[]
     *                          userId?：作者ID，可为空
     *                          department：String[]
     *                          search:String 搜索条件
     * @return              课程列表
     */
    @Override
    public List<Course> getList(
            int start,
            int count,
            String sort,
            Map<String, Object> conditions
    ) {
        return courseDao.getList(start, count, sort, conditions);
    }

    /**
     * 删除课程
     * @param id    课程ID
     * @return      删除成功数目
     */
    @Override
    public int delete(int id) {
        return courseDao.delete(id);
    }

    /**
     * 获取某一门课程的学习总人数
     * @param id    课程ID
     * @return      某一门课程的学习总人数
     */
    @Override
    public int getNumOfPeople(int id) {
        return courseDao.getNumOfPeople(id);
    }

    /**
     * 获取用户已选课程总数
     * @param userId    用户id
     * @return
     */
    @Override
    public int countChooseCourse(int userId) {
        return courseDao.countChooseCourse(userId);
    }

    /**
     * 获取用户已选课程
     * @param start     分页开始位置
     * @param count     分页返回数目
     * @param userId    用户id
     * @return
     */
    @Override
    public List<Course> getChooseCourse(int start, int count, int userId) {
        return courseDao.getChooseCourse(start,count,userId);
    }
}