package cn.canlnac.course.service;

import cn.canlnac.course.dao.ExampleDao;
import cn.canlnac.course.entity.Example;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by can on 2016/9/10.
 */
@Transactional
@Component(value = "exampleService")
public class ExampleServiceImpl implements ExampleService {
    @Autowired
    private ExampleDao exampleDao;

    @Override
    public List<Example> getAll() {
        return exampleDao.selectAll();
    }
}
