package site.fsyj.course.utils;

public class JWConsts {
    public final static String username = "201901130210";

    public final static String password = "WYX#a13mJaSxL";

    public static final String EDUCATIONAL_SYSTEM = "https://cas.paas.cdut.edu.cn/cas/login?service=http://jw.cdut.edu.cn/sso/login.jsp?targetUrl=base64aHR0cDovL2p3LmNkdXQuZWR1LmNuL0xvZ29uLmRvP21ldGhvZD1sb2dvblNTT2NkbGdkeA==";

    //  https://jw.cdut.edu.cn/jsxsd/xskb/viewtable.do?xnxq01id=2022-2023-1&kbjcmsid=7E5976C91D9A4146930951FD11516BCC&xs0101id=201901130214&lx=xs0101id
    public static String composeCourseUrl(String term, String stuNo) {
        return "https://jw.cdut.edu.cn/jsxsd/xskb/viewtable.do?xnxq01id=" +
                term + "&kbjcmsid=7E5976C91D9A4146930951FD11516BCC&xs0101id=" + stuNo + "&lx=xs0101id";
    }

    public static final String CURRENT_TERM = "current_term#";

}
