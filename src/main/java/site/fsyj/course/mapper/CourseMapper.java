package site.fsyj.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import site.fsyj.course.entity.Course;

import java.util.List;

@Mapper
public interface CourseMapper extends BaseMapper<Course> {

    int batchInsertWithUserTerm(@Param("courses") List<Course> courses, @Param("user") long userId, @Param("term") long term);

    @Select("SELECT * FROM course WHERE term_id = #{termId} AND start_week <= #{week} AND end_week >= #{week} AND user_id = #{userId}")
    List<Course> getCourseScheduleByWeek(@Param("termId") Long termId, @Param("week") int week, @Param("userId") Long id);
}
