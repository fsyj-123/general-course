package site.fsyj.course.utils;

import org.springframework.data.util.Pair;


/**
 * @author fsyj on 2022/5/30
 */
public class SessionKeyUtil {
    public static final String SEPARATOR = "###";

    public static String generateSessionKey(String wxSessionKey, String wxOpenId) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(wxOpenId);
        buffer.append(SEPARATOR);
        buffer.append(wxSessionKey);
        return buffer.toString();
    }

    /**
     * 返回OpenId：index of 0, wxSessionKey：index of 1
     * @param decodeSessionKey
     * @return
     */
    public static Pair<String, String> getSessionInfo(String decodeSessionKey) {
        String[] split = decodeSessionKey.split(SEPARATOR);
        return Pair.of(split[0], split[1]);
    }
}
