package site.fsyj.course.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import site.fsyj.course.config.WxConfig;
import site.fsyj.course.entity.User;
import site.fsyj.course.mapper.UserMapper;
import site.fsyj.course.utils.JwtUtil;
import site.fsyj.course.utils.RedisCacheUtil;
import site.fsyj.course.utils.WxUtil;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author fsyj on 2022/5/29
 */
@Service
public class WxService {

    @Resource
    private WxConfig wxConfig;

    @Resource
    private WxUtil wxUtil;

    @Resource
    private RedisCacheUtil redisCache;

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserService userService;

    private static final String PREFIX = "微信用户";

    /**
     * 通过小程序的code获取Session
     *
     * @param wxCode
     * @return Token
     */
    public String login(String wxCode) {
        // 获取 openId 和 sessionKey
        Map<String, Object> map = wxUtil.getSessionKey(wxCode);
        String openId = (String) map.get(WxUtil.OPENID);
        String sessionKey = (String) map.get(WxUtil.SESSION_KEY);
        // 查看数据库中有没有openID的用户，
        LambdaQueryWrapper<User> query = new LambdaQueryWrapper<>();
        query.eq(User::getOpenid, openId);
        User user = userMapper.selectOne(query);
        // 如果没有，创建用户
        if (user == null) {
            user = new User(null, 1, openId, PREFIX, null);
            userMapper.insert(user);
            // 重新查找
            user = userMapper.selectOne(query);
        }
        // 如果有，创建Token，并返回
        String token = JwtUtil.createJWT(sessionKey, (long) wxConfig.getExpire());
        userService.setUser(user, token);
        return token;
    }

}
