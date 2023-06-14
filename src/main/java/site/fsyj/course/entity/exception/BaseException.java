package site.fsyj.course.entity.exception;

public abstract class BaseException extends RuntimeException {
    protected Integer code;
    protected String msg;

    public BaseException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
