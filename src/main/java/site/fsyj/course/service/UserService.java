package site.fsyj.course.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import site.fsyj.course.config.WxConfig;
import site.fsyj.course.entity.User;
import site.fsyj.course.mapper.UserMapper;
import site.fsyj.course.utils.RedisCacheUtil;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    @Resource
    private RedisCacheUtil redisCache;

    @Resource
    private WxConfig wxConfig;

    public void setUser(User user, String token) {
        redisCache.setCacheObject(token, user,  wxConfig.getExpire(), TimeUnit.MILLISECONDS);
    }
}
