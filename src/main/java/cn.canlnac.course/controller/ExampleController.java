package cn.canlnac.course.controller;

import cn.canlnac.course.entity.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@RestController
public class ExampleController {
    /**
     * 基本的例子，通过在query中传递参数，没指定method；
     * 如果发起请求的方法没写，则会执行该方法下的。
     * @param id
     * @return
     */
    @RequestMapping("/example")
    public Example getExample1(@RequestParam(value="id", defaultValue="1") int id) {
        Example example = new Example();
        example.setId(id);
        return example;
    }

    /**
     * 以RESTful的方式，在地址中传递参数id，建议这种方式。
     * @param id
     * @return
     */
    @RequestMapping(path = "/example/{id}", method = RequestMethod.GET)
    public Example getExample2(@PathVariable int id) {
        Example example = new Example();
        example.setId(id);
        return example;
    }

    /**
     * 换一种注解方式，直接以请求方式的注解，可以省了method
     * @param id
     * @return
     */
    @GetMapping("/other_example/{id}")
    public Example getExample3(@PathVariable int id) {
        Example example = new Example();
        example.setId(id);
        return example;
    }

    /**
     * 获取多个资源，分页，排序
     * @param sort      排序
     * @param start     第一个元素位置
     * @param count     返回元素数量
     * @return
     */
    @GetMapping("/examples")
    public ResponseEntity<List<Example>> getExamples(@RequestParam(value="sort", defaultValue="date") String sort,
                                     @RequestParam(value="start", defaultValue="0") int start,
                                     @RequestParam(value="count", defaultValue="10") int count) {
        String[] sortArray = {"date", "id"};
        //如果start 小于0，或者sort的值不在sortArray中，就返回400，参数错误
        if(start < 0 || !Arrays.asList(sortArray).contains(sort)){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        //模拟数据
        Example[] data = { new Example(), new Example()};
        data[0].setId(1);
        data[1].setId(2);

        // 根据id，逆序排序
        if(sort.equals("id")) {
            Arrays.sort(data, new Comparator<Example>() {
                @Override
                public int compare(Example o1, Example o2) {
                    return o2.getId() - o1.getId();
                }
            });
        }

        List<Example> examples = Arrays.asList(data);

        //检查下标数组越界，返回400，参数错误
        if(start + count > examples.size()){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        examples = examples.subList(start, start + count);

        //返回200状态码
        return new ResponseEntity<List<Example>>(examples, HttpStatus.OK);
    }


    /**
     * 创建一个资源，并返回该资源和状态码201，201--资源被创建
     * @param example
     * @return
     */
    @PostMapping("/example")
    public ResponseEntity<Example> createExample(@RequestBody Example example) {
        return new ResponseEntity<Example>(example, HttpStatus.CREATED);
    }
}