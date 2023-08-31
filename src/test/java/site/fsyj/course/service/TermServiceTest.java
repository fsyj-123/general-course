package site.fsyj.course.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import site.fsyj.course.entity.User;

import javax.annotation.Resource;

@SpringBootTest
class TermServiceTest {

    @Resource
    private TermService termService;

    private User user = new User(null, 1, null, null, null);

    @Test
    void getCurrentWeek() {
        System.out.println(termService.getCurrentWeek(user));
    }

    @Test
    void getCurrenDay() {
//        System.out.println(termService.getCurrenDay(user));
    }

    @Test
    void currentTerm() {
//        Assertions.assertNotNull(termService.getCurrentTerm());
    }
}
