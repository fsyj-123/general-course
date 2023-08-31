package site.fsyj.course.http;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.concurrent.TimeoutException;

@SpringBootTest
class JWServiceTest {

    @Resource
    public JWService jwService;

    @Test
    void getCourse() throws InterruptedException  {
        for (int i = 0; i < 10; i++) {
            try {
                System.out.println(jwService.getCourse("201901130210", "2022-2023-2"));
            } catch (TimeoutException e) {
                System.err.println("任务 " + i +"执行超时");
            }
//            Thread.sleep(10000);
        }
    }


}
