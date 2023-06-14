package site.fsyj.course.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import site.fsyj.course.annotation.NoAuth;
import site.fsyj.course.entity.AjaxResult;
import site.fsyj.course.entity.User;
import site.fsyj.course.service.CourseService;
import site.fsyj.course.service.UserService;
import site.fsyj.course.service.WxService;
import site.fsyj.course.utils.AuthenticateConst;
import site.fsyj.course.utils.RedisCacheUtil;
import site.fsyj.course.utils.UserConst;

import javax.annotation.Resource;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;

@Api
@RequestMapping("/api/user")
@RestController
public class UserController {

    @Resource
    private RedisCacheUtil redisCache;

    @Resource
    private UserService userService;

    @Resource
    private WxService wxService;

    @Resource
    private CourseService courseService;


    @NoAuth
    @ApiOperation("登录")
    @PostMapping("/login")
    public AjaxResult login(String code) throws LoginException {
        String token = wxService.login(code);
        return AjaxResult.success("success", token);
    }

    @ApiOperation("更新用户信息")
    @PostMapping("/info")
    public AjaxResult updateInfo(@RequestBody User info, HttpServletRequest request) {
        User user = (User) request.getAttribute(AuthenticateConst.USER);
        AjaxResult ok = AjaxResult.success("ok");
        if (info == null) {
            return ok;
        }
        String stuNo = info.getStuNo().trim();
        if (StringUtils.hasLength(stuNo) && stuNo.length() == 12) {
            user.setStuNo(stuNo);
        }
        String name = info.getName();
        if (StringUtils.hasLength(name)) {
            user.setName(name);
        }
        Integer integer = info.getSchool();
        if (redisCache.getCacheObject(UserConst.SCHOOL_PREFIX + integer) != null) {
            user.setSchool(integer);
        }
        userService.updateById(user);
        user = userService.getById(user.getId());
        // 异步更新课表和用户信息
        userService.setUser(user, request.getHeader("TOKEN"));
        courseService.importCourse(user, null);
        return ok;
    }


    @ApiOperation("获取用户信息")
    @GetMapping("/info")
    public AjaxResult info(HttpServletRequest request) {
        return AjaxResult.success(request.getAttribute(AuthenticateConst.USER));
    }
}
