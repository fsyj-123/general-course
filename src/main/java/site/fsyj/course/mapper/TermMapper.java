package site.fsyj.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import site.fsyj.course.entity.Term;

@Mapper
public interface TermMapper extends BaseMapper<Term> {
}