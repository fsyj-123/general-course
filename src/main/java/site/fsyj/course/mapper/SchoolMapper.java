package site.fsyj.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import site.fsyj.course.entity.School;

@Mapper
public interface SchoolMapper extends BaseMapper<School> {
}