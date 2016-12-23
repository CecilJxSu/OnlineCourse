package cn.canlnac.course.dao;


import cn.canlnac.course.entity.Example;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ExampleDao {
    Example getByID(int id);
    List<Example> selectAll();
}