package cn.canlnac.course.dao;

import cn.canlnac.course.entity.Course;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 课程数据接口
 */
@Component
public interface CourseDao {
    /**
     * 创建课程
     * @param course    课程
     * @return          创建数目
     */
    int create(Course course);

    /**
     * 更新课程
     * @param course    课程
     * @return          更新成功数目
     */
    int update(Course course);

    /**
     * 获取指定课程
     * @param id    课程ID
     * @return      课程
     */
    Course findByID(int id);

    /**
     * 获取作者的课程列表
     * @param userId    作者ID
     * @return          课程列表
     */
    List<Course> findByUserId(int userId);

    /**
     * 统计课程
     * @param conditions    条件：
     *                          status: ("public" | "draft")[]
     *                          userId?：作者ID，可为空
     *                          department：String[]
     * @return              课程数目
     */
    int count(@Param("conditions") Map<String, Object> conditions);

    /**
     * 获取课程列表
     * @param start         分页开始位置
     * @param count         分页返回数目
     * @param sort          排序：日期date，热度rank
     * @param conditions    条件：
     *                          status: ("public" | "draft")[]
     *                          userId?：作者ID，可为空
     *                          department：String[]
     * @return              课程列表
     */
    List<Course> getList(
            @Param("start") int start,
            @Param("count") int count,
            @Param("sort") String sort,
            @Param("conditions") Map<String, Object> conditions
    );

    /**
     * 删除课程
     * @param id    课程ID
     * @return      删除成功数目
     */
    int delete(int id);
}