package site.fsyj.course.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import site.fsyj.course.entity.AjaxResult;
import site.fsyj.course.entity.exception.BaseException;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class BaseExceptionHandler {

    /**
     * 权限校验异常
     */
    @ExceptionHandler(BaseException.class)
    public AjaxResult handleAccessDeniedException(BaseException e, HttpServletRequest request) {
        log.error("请求地址"+request.getRequestURI()+",错误原因'"+e.getMsg()+"'", e);
        return AjaxResult.error(e.getCode(), e.getMsg());
    }
}
