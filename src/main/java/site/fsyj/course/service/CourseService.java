package site.fsyj.course.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.fsyj.course.dto.DayCourse;
import site.fsyj.course.dto.WeekCourse;
import site.fsyj.course.entity.Course;
import site.fsyj.course.entity.Term;
import site.fsyj.course.entity.User;
import site.fsyj.course.http.JWService;
import site.fsyj.course.mapper.CourseMapper;
import site.fsyj.course.utils.RedisCacheUtil;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CourseService extends ServiceImpl<CourseMapper, Course> {

    @Resource
    private JWService jwService;

    @Resource
    private TermService termService;

    @Resource
    private CourseMapper courseMapper;

    @Resource
    private RedisCacheUtil redisCache;

    @Resource
    private CourseExceptionHandler courseExceptionHandler;

    private static final ExecutorService coursePool = new ThreadPoolExecutor(10, Integer.MAX_VALUE, 3,
            TimeUnit.DAYS, new ArrayBlockingQueue<>(100));

    /**
     * import course
     *
     * @param user with value of stuNo and schoolId
     * @param term term to query
     */
    public void importCourse(User user, Term term) {
        if (term == null) {
            term = termService.getCurrentTerm(user);
        }
        Term finalTerm = term;
        log.info("导入课程任务提交成功，用户信息" + user);
        Future<?> task = coursePool.submit(() -> {
            log.info("用户" + user + "任务开始");
            try {
                List<Course> course = jwService.getCourse(user.getStuNo(), finalTerm.getTermDescribe());
                // delete termId and user id rows
                deleteAndInsertCourses(course, user, finalTerm);
                log.info("任务" + user + "执行成功");
            } catch (Exception e) {
                log.error("任务执行失败，", e);
                // 任务执行失败，保存任务信息到持久化存储中
                courseExceptionHandler.saveFailedTask(user, finalTerm, e);
            }
        });
        try {
            task.get(3000, TimeUnit.SECONDS);
        } catch (InterruptedException | TimeoutException | ExecutionException e) {
            log.error("任务" + user + "执行失败", e);
        }
    }

    @Transactional(rollbackFor = {SqlSessionException.class})
    public void deleteAndInsertCourses(List<Course> course, User user, Term term) {
        if (course == null || course.size() == 0) {
            return;
        }
        LambdaQueryWrapper<Course> wrapper = new LambdaQueryWrapper<>();
        courseMapper.delete(wrapper.eq(Course::getUserId, user.getId()).eq(Course::getTermId, term.getId()));
        courseMapper.batchInsertWithUserTerm(course, user.getId(), term.getId());
    }


    public List<DayCourse> dayCourse(User user) {
        // 获取当前的星期和天数
        Integer week = termService.getCurrentWeek(user);
        Integer day = termService.getCurrenDay(user);
        if (week == null || day == null) {
            return null;
        }
        LambdaQueryWrapper<Course> wrapper = new LambdaQueryWrapper<>();
        wrapper.le(Course::getStartWeek, week).ge(Course::getEndWeek, week).eq(Course::getDay, day).
                eq(Course::getUserId, user.getId());
        List<Course> courses = courseMapper.selectList(wrapper);
        if (courses != null && courses.size() != 0) {
            return courses.stream().map(course -> new DayCourse(course.getName(), course.getPlace(),
                    course.getStartSection(), course.getEndSection(), course.getTeacher())).collect(Collectors.toList());
        }
        return null;
    }

    public Map<String, Object> weekCourse(User user, Integer week, Long termId) {
        if (week == null) {
            week = termService.getCurrentWeek(user);
        }
        // if term is null or term dose not exists, then query currentTerm
        if (termId == null || termService.getTerm(user, termId) == null) {
            termId = termService.getCurrentTerm(user).getId();
        }
        List<Course> weekCourses = courseMapper.getCourseScheduleByWeek(termId, week, user.getId());
        Integer finalWeek = week;
        List<WeekCourse> courseList = weekCourses.stream().map(course ->
                new WeekCourse(course.getName(), course.getPlace(), course.getDay(), finalWeek,
                        course.getTeacher(), course.getCredit(), course.getStartSection(), course.getEndSection())
        ).collect(Collectors.toList());
        HashMap<String, Object> map = new HashMap<>(2);
        map.put("courses", courseList);
        map.put("currentWeek", week);
        return map;
    }

}
