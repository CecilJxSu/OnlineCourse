package cn.canlnac.course.controller.catalog;

import cn.canlnac.course.entity.Answer;
import cn.canlnac.course.service.impl.AnswerServiceImpl;
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
import java.util.Map;

/**
 *课程章节下的回答
 */
@Component("CatalogRootController")
@RestController
public class AnswerController {

    @Autowired
    AnswerServiceImpl answerService;

    @Autowired
    private JWT jwt;

    /*
       * @author wei
       * 获取自己在该章节下的回答
       * return id、date、catalogId、userId、answer[]、total
       * */
    @GetMapping("/catalog/{catalogId}/answer")
    public ResponseEntity<Map<String, Object>> getOneselfSectionChapterAnswer(@PathVariable String catalogId, @RequestBody Map<String, Object> body) {
        HashMap sendData = new HashMap();

        if(catalogId==""||catalogId==null){
            //返回400,参数错误
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        if (body.get("userid")==null||body.get("userid")==""){
            //返回401，用户未登录
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        Answer answer = answerService.getAnswer(Integer.valueOf(catalogId),Integer.valueOf(body.get("userid").toString()));

        if (answer==null){
            //返回404,答题为空/章节不存在
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        sendData.put("answer",answer);

        //生成JWT
        String auth = jwt.sign(sendData);
        return new ResponseEntity(sendData,HttpStatus.OK);
    }
}
