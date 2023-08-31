package site.fsyj.course.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import site.fsyj.course.dto.DayCourse;
import site.fsyj.course.entity.Term;
import site.fsyj.course.entity.User;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@SpringBootTest
class CourseServiceTest {

    @Resource
    private CourseService courseService;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Test
    void importCourse() throws ParseException {
        courseService.importCourse(new User(1L, 1, null, null, "202013160104"),
                new Term(1L, 1, "2022-2023-2", sdf.parse("2023-02-20"), sdf.parse("2023-07-09")));

    }

    @Test
    public void dayCourse() {
        List<DayCourse> dayCourse = courseService.dayCourse(new User(1L, 1, null, null, "201901130210"));
        for (DayCourse course : dayCourse) {
            System.out.println(dayCourse);
        }
    }
}
