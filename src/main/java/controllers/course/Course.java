package controllers.course;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Course {

    @RequestMapping("/course")
    public Course course(@RequestParam(value="name", defaultValue="World") String name) {
        return new Course();
    }
}