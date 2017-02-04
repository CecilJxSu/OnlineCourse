package cn.canlnac.course.controller.catalog;

import cn.canlnac.course.entity.Answer;
import cn.canlnac.course.entity.Question;
import cn.canlnac.course.service.AnswerService;
import cn.canlnac.course.service.CatalogService;
import cn.canlnac.course.service.QuestionService;
import cn.canlnac.course.util.JWT;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 章节小测的回答
 */
@Component("CatalogAnswerController")
@RestController
public class AnswerController {
    @Autowired
    CatalogService catalogService;

    @Autowired
    AnswerService answerService;

    @Autowired
    QuestionService questionService;

    @Autowired
    private JWT jwt;

    /**
     * 获取自己的回答
     * @param Authentication    登录信息
     * @param catalogId         章节ID
     * @return                  回答
     */
    @GetMapping("/catalog/{catalogId}/answer")
    public ResponseEntity getAnswer(
            @RequestHeader(value="Authentication", required = false) String Authentication,
            @PathVariable int catalogId
    ) {
        //未登录
        Map auth;
        if (Authentication == null || (auth = jwt.decode(Authentication)) == null) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        Answer answer = answerService.getAnswer(catalogId, Integer.parseInt(auth.get("id").toString()));    //获取回答
        if(answer == null){    //回答不存在
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        Map sendData = new HashMap();   //返回数据
        sendData.put("id",answer.getId());
        sendData.put("date",answer.getDate());
        sendData.put("questionId",answer.getQuestionId());

        //设置用户
        Map user = new HashMap();
        user.put("id",auth.get("id"));
        user.put("name",auth.get("nickname"));
        user.put("iconUrl",auth.get("iconUrl"));
        sendData.put("user",user);
        sendData.put("answer", new JSONArray(answer.getAnswer()).toList());
        sendData.put("total", answer.getTotal());

        return new ResponseEntity(sendData,HttpStatus.OK);
    }

    /**
     * 创建自己的回答
     * @param Authentication    登录信息
     * @param catalogId         章节ID
     * @return                  回答
     */
    @PostMapping("/catalog/{catalogId}/answer")
    public ResponseEntity createAnswer(
            @RequestHeader(value="Authentication", required = false) String Authentication,
            @RequestBody Map body,
            @PathVariable int catalogId
    ) {
        //未登录
        Map auth;
        if (Authentication == null || (auth = jwt.decode(Authentication)) == null) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        Answer answer = answerService.getAnswer(catalogId, Integer.parseInt(auth.get("id").toString()));    //获取回答
        if(answer != null){    //回答存在
            return new ResponseEntity(HttpStatus.CONFLICT);
        }

        //参数检查
        if(body.get("questionId") == null || body.get("answer") == null || body.get("total") == null){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        //小测或章节是否存在
        Question question = questionService.findById(Integer.parseInt(body.get("questionId").toString()));
        if (question == null || question.getCatalogId() != catalogId){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        //设置回答
        answer = new Answer();
        answer.setUserId(Integer.parseInt(auth.get("id").toString()));
        answer.setQuestionId(Integer.parseInt(body.get("questionId").toString()));
        answer.setTotal(Float.parseFloat(body.get("total").toString()));
        answer.setAnswer(new JSONArray(((ArrayList)body.get("answer")).toArray()).toString());

        int createdCount = answerService.create(answer);    //创建回答
        if(createdCount != 1) { //创建失败
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Map sendData = new HashMap();               //创建返回对象
        sendData.put("answerId",answer.getId());    //设置返回的回答ID

        //返回回答ID
        return new ResponseEntity(sendData,HttpStatus.OK);
    }
}