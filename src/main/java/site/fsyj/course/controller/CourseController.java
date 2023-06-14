package site.fsyj.course.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import site.fsyj.course.dto.ReImportDto;
import site.fsyj.course.entity.AjaxResult;
import site.fsyj.course.entity.Term;
import site.fsyj.course.entity.User;
import site.fsyj.course.service.CourseService;
import site.fsyj.course.service.TermService;
import site.fsyj.course.utils.AuthenticateConst;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Api("课程-400")
@RestController
@RequestMapping("/api/course")
public class CourseController {
    @Resource
    private CourseService courseService;

    @Resource
    private TermService termService;

    private AjaxResult verify(User user) {
        String stuNo = user.getStuNo();
        Integer school = user.getSchool();
        if (school != null) {
            if (StringUtils.hasLength(stuNo) && stuNo.length() == 12) {
                return null;
            }
            return AjaxResult.error(-4001, "学号未绑定");
        }
        return AjaxResult.error(4002, "学校未绑定");
    }


    @ApiOperation("当日课表")
    @GetMapping("/day")
    public AjaxResult dayCourse(HttpServletRequest request) {
        User user = (User) request.getAttribute(AuthenticateConst.USER);
        AjaxResult result = verify(user);
        if (result == null) {
            result = AjaxResult.success(courseService.dayCourse(user));
        }
        return result;
    }

    @ApiOperation("week course table")
    @GetMapping("/week")
    public AjaxResult weekCourse(HttpServletRequest request, Integer weekNo, Long termId) {
        User user = (User) request.getAttribute(AuthenticateConst.USER);
        AjaxResult result = verify(user);
        if (result == null) {
            result = AjaxResult.success(courseService.weekCourse(user, weekNo, termId));
        }
        return result;
    }


    @ApiOperation("import course")
    @PostMapping("/import")
    public AjaxResult importCourse(HttpServletRequest request, @RequestBody(required = false) ReImportDto reImportDto) {
        User user = (User) request.getAttribute(AuthenticateConst.USER);
        if (reImportDto == null) {
            reImportDto = new ReImportDto();
        }
        // get term obj
        Term term = termService.getTerm(user, reImportDto.getTermId());
        AjaxResult result = verify(user);
        if (result == null) {
            courseService.importCourse(user, term);
            result = AjaxResult.success("导入成功");
        }
        return result;
    }
}
