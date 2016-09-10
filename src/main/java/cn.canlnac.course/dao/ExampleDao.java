package cn.canlnac.course.dao;


import cn.canlnac.course.entity.Example;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ExampleDao {
    public Example getByID(int id);
    public List<Example> selectAll();
}