package site.fsyj.course.utils;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import site.fsyj.cdut.utils.HttpClientHelper;
import site.fsyj.course.config.WxConfig;
import site.fsyj.course.entity.exception.WxLoginException;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author fsyj
 */
@Component
public class WxUtil {

    @Resource(type = WxConfig.class)
    private WxConfig wxConfig;

    public final static String GRANT_TYPE = "authorization_code";

    public static final String WX_LOGIN_API = "https://api.weixin.qq.com/sns/jscode2session";

    public static final String SESSION_KEY = "session_key";

    public static final String UNION_ID = "unionid";
    public static final String ERR_MSG = "errmsg";
    public static final String OPENID = "openid";
    public static final String ERR_CODE = "errcode";
    public static final String VERIFY_SUCCESS = "0";


    private HashMap<String, Object> generateMap(String wxCode) {
        HashMap<String, Object> map = new HashMap<>(4);
        map.put("appid", wxConfig.getAppId());
        map.put("secret", wxConfig.getSecret());
        map.put("grant_type", WxUtil.GRANT_TYPE);
        map.put("js_code", wxCode);
        return map;
    }

    public Map<String, Object> getSessionKey(String wxCode) {
        String result = HttpClientHelper.get(WX_LOGIN_API, generateMap(wxCode), null);
        if (StringUtils.hasLength(result)) {
            Map<String, Object> map = JsonUtil.json2Map(result);
            if (map.containsKey(ERR_CODE)) {
                String err = String.valueOf(map.get(ERR_CODE));
                // 请求接口错误
                if (StringUtils.hasLength(err)) {
                    if (!VERIFY_SUCCESS.equals(err)) {
                        throw new WxLoginException(Integer.parseInt(err), null);
                    }
                }
            }
            return map;
        }
        throw new WxLoginException(400, "请求接口错误");
    }
}
