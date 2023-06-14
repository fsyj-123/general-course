package site.fsyj.course.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import site.fsyj.course.entity.FailedTask;
import site.fsyj.course.entity.Term;
import site.fsyj.course.entity.User;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class CourseExceptionHandler {

    @Lazy
    @Resource
    private CourseService courseService;

    public void saveFailedTask(User user, Term term, Exception e) {
        // 将任务的相关信息（用户、术语等）保存到持久化存储中，例如数据库或文件系统
        // ...
    }

    public void retryFailedTasks() {
        // 从持久化存储中获取失败的任务列表
        List<FailedTask> failedTasks = getFailedTasks();

        for (FailedTask task : failedTasks) {
            User user = task.getUser();
            Term term = task.getTerm();
            log.info("重新执行失败的任务，用户信息：" + user);
            courseService.importCourse(user, term);
        }
    }

    private List<FailedTask> getFailedTasks() {
        // 从持久化存储中获取所有失败的任务列表
        // ...
        return null;
    }

    private void deleteFailedTask(FailedTask task) {
        // 从持久化存储中删除指定的失败任务
        // ...

    }
}
