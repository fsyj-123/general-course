package site.fsyj.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import site.fsyj.course.entity.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}