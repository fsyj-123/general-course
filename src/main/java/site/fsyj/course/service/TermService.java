package site.fsyj.course.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import site.fsyj.course.entity.Term;
import site.fsyj.course.entity.User;
import site.fsyj.course.entity.exception.UserInfoException;
import site.fsyj.course.mapper.TermMapper;
import site.fsyj.course.utils.RedisCacheUtil;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

@Service
public class TermService extends ServiceImpl<TermMapper, Term> {
    @Resource
    private TermMapper termMapper;

    @Resource
    private RedisCacheUtil redisCache;
    private static final String TERM_SCHOOL_PREFIX = "term#school-";

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


    public Term getCurrentTerm(User user) {
        Date now = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("Asia/Shanghai"))).getTime();
        Term term = checkRedisNow(user.getSchool(), now);
        if (term == null) {
            termMapper.selectById(1);
        }
        return term;
    }

    public Term getTerm(User user, Long termId) {
        Term term = null;
        if (termId == null) {
            getCurrentTerm(user);
        } else {
            LambdaQueryWrapper<Term> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Term::getId, termId);
            term = termMapper.selectOne(wrapper);
            if (term == null) {
                term = getCurrentTerm(user);
            }
        }
        return term;
    }

    /**
     * return current week number, generate from start week date and end week date
     * a week has 7 days, start with monday
     *
     * @param user
     * @return
     */
    public Integer getCurrentWeek(User user) {
        Integer school = user.getSchool();
        Date now = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("Asia/Shanghai"))).getTime();
        Term term = checkRedisNow(school, now);
        if (term == null) {
            throw new UserInfoException(3001, "用户未绑定学校");
        }
        Date startDate = term.getStartDate();
        // Calculate the number of days between start date and current date
        long days = ChronoUnit.DAYS.between(startDate.toInstant(), now.toInstant());
        // Calculate the current week number
        return (int) (days / 7) + 1;
    }

    public Integer getCurrenDay(User user) {
        Integer school = user.getSchool();
        Date now = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("Asia/Shanghai"))).getTime();
        Term term = checkRedisNow(school, now);
        if (term == null) {
            return null;
        }
        Date startDate = term.getStartDate();
        // Calculate the number of days between start date and current date
        long days = ChronoUnit.DAYS.between(startDate.toInstant(), now.toInstant());
        // Calculate the current week number
        return (int) days % 7 + 1;
    }

    private Term checkRedisNow(Integer school, Date now) {
        List<Term> terms = redisCache.getCacheObject(TERM_SCHOOL_PREFIX + school);
        if (terms == null || terms.size() == 0) {
            // if term is null, query from db
            LambdaQueryWrapper<Term> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Term::getSchoolId, school);
            terms = termMapper.selectList(wrapper);
        }
        if (terms == null) {
            return null;
        } else {
            redisCache.setCacheObject(TERM_SCHOOL_PREFIX + school, terms, 1, TimeUnit.DAYS);
        }
        // 用于存储当前日期所属的学期
        Term currentTerm = null;
        // 用于存储距离当前日期最近的上一个学期
        Term nearestTerm = null;

        for (Term term : terms) {
            if (now.compareTo(term.getStartDate()) >= 0 && now.compareTo(term.getEndDate()) <= 0) {
                // 当前日期在学期的起始日期和结束日期之间，找到当前学期
                currentTerm = term;
                break;
            } else if (now.compareTo(term.getEndDate()) > 0) {
                // 当前日期在学期的结束日期之后，更新最近的学期
                if (nearestTerm == null || term.getEndDate().compareTo(nearestTerm.getEndDate()) > 0) {
                    nearestTerm = term;
                }
            }
        }
        if (currentTerm == null) {
            currentTerm = nearestTerm;
        }
        return currentTerm;
    }
}
