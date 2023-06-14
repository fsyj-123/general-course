package site.fsyj.course.service;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import site.fsyj.course.mapper.SchoolMapper;
import site.fsyj.course.entity.School;
@Service
public class SchoolService extends ServiceImpl<SchoolMapper, School> {

}
