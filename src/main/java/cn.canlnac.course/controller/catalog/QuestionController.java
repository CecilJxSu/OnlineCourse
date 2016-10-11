package cn.canlnac.course.controller.catalog;

import cn.canlnac.course.util.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

/**
 * 章节下的小测
 */
@Component
@RestController
public class QuestionController {
    @Autowired
    private JWT jwt;
}