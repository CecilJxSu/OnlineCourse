package cn.canlnac.course.controller.catalog;

import cn.canlnac.course.entity.Question;
import cn.canlnac.course.service.impl.QuestionServiceImpl;
import cn.canlnac.course.util.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 课程章节下的题目
 */
@Component("CatalogQuestionController")
@RestController
public class QuestionController {

    @Autowired
    QuestionServiceImpl questionService;

    @Autowired
    private JWT jwt;


    /*
       * @author wei
       * 获取该章节下的小测
       * return Question：[]
       * */
    @GetMapping("/catalog/{catalogId}/questions")
    public ResponseEntity<Map<String, Object>> getSectionChapterTest(@PathVariable String catalogId, @RequestBody Map<String, Object> body) {
        HashMap sendData = new HashMap();

        if(catalogId==""||catalogId==null){
            //返回400,参数错误
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        if (body.get("userid")==null||body.get("userid")==""){
            //返回401，用户未登录
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        List<Question> list = questionService.getQuestions(Integer.valueOf(catalogId));

        if (list==null){
            //返回404,试题为空
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        sendData.put("Question",list);

        //生成JWT
        String auth = jwt.sign(sendData);
        return new ResponseEntity(sendData,HttpStatus.OK);
    }


    /*
       * @author wei
       * 课程章节下的题目
       * 获取该章节下的小测
       * return Question：[]
       * */
    @GetMapping("/catalog/{catalogId}/question/{questionId}")
    public ResponseEntity<Map<String, Object>> getSectionChapterQuestionTest(@PathVariable String catalogId,@PathVariable String questionId, @RequestBody Map<String, Object> body) {
        HashMap sendData = new HashMap();

        if(catalogId==""||catalogId==null||questionId==""||questionId==null){
            //返回400,参数错误
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        if (body.get("userid")==null||body.get("userid")==""){
            //返回401，用户未登录
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        //获取该章节下的小测，实现功能

        //生成JWT
        String auth = jwt.sign(sendData);
        return new ResponseEntity(sendData,HttpStatus.OK);

    }
}
