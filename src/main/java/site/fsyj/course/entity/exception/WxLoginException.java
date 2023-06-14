package site.fsyj.course.entity.exception;

public class WxLoginException extends BaseException {
    public WxLoginException(int code, String message) {
        super(code, null);
        switch (code) {
            case 40029:
                msg = "js_code无效";
                break;
            case 45011:
                msg = "API 调用太频繁，请稍候再试";
                break;
            case 40226:
                msg = "高风险等级用户，小程序登录拦截";
                break;
            case -1:
                msg = "系统繁忙";
                break;
            default:
                msg = message;
        }
    }
}
