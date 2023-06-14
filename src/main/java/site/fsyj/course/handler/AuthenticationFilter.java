package site.fsyj.course.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import site.fsyj.course.annotation.NoAuth;
import site.fsyj.course.entity.AjaxResult;
import site.fsyj.course.entity.User;
import site.fsyj.course.utils.AuthenticateConst;
import site.fsyj.course.utils.JsonUtil;
import site.fsyj.course.utils.RedisCacheUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author fsyj
 */
@Component
public class AuthenticationFilter implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);

    @Resource
    private RedisCacheUtil redisCache;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            NoAuth auth = handlerMethod.getMethodAnnotation(NoAuth.class);
            if (auth == null) {
                String token = request.getHeader("TOKEN");
                if (!StringUtils.hasLength(token)) {
                    returnJson(response, new AjaxResult(-2, "Token为空"));
                    return false;
                }
                User user = redisCache.getCacheObject(token);
                if (user != null) {
                    request.setAttribute(AuthenticateConst.USER, user);
                    return true;
                } else {
                    returnJson(response, new AjaxResult(-2, "Token过期或错误"));
                    return false;
                }
            }
        }
        return true;
    }

    private void returnJson(HttpServletResponse response, AjaxResult result){
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.setStatus(200);
        try {
            writer = response.getWriter();
            writer.print(JsonUtil.obj2String(result));
        } catch (IOException e){
            log.error("响应输出失败");
        } finally {
            if(writer != null){
                writer.flush();
                writer.close();
            }
        }
    }
}
