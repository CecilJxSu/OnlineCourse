package cn.canlnac.course.controller.catalog;

import cn.canlnac.course.util.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

/**
 * 章节下的学习记录
 */
@Component("CatalogLearnRecordController")
@RestController
public class LearnRecordController {
    @Autowired
    private JWT jwt;
}