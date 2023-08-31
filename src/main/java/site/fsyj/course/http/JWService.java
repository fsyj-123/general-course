package site.fsyj.course.http;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import site.fsyj.cdut.service.LoginService;
import site.fsyj.cdut.utils.Pair;
import site.fsyj.course.config.ApiConfig;
import site.fsyj.course.dto.CourseDto;
import site.fsyj.course.entity.Course;
import site.fsyj.course.utils.JWConsts;
import site.fsyj.course.utils.JsonUtil;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;


@Slf4j
@Service("jwService")
public class JWService {
    private static Pair<CloseableHttpClient, BasicCookieStore> clientCookieStore;

    @Resource
    private ApiConfig apiConfig;

    private static final HttpClient parseClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();

    /**
     * 获取课表
     * 1. 登录教务处
     * 2. 向课表接口发请求
     * 3. 解析
     */
    public List<Course> getCourse(String stuNo, String term) throws TimeoutException {
        verifyClient(false);
        try {
            CloseableHttpClient client = clientCookieStore.getFirst();
            BasicCookieStore cookieStore = clientCookieStore.getSecond();
            String html = CompletableFuture.supplyAsync(() -> {
                try {
                    return doGetCourses(client, cookieStore, stuNo, term);
                } catch (IOException e) {
                    log.error("获取课表出错", e);
                }
                return null;
            }).completeOnTimeout(null, 5, TimeUnit.SECONDS).join();
            if (html == null) {
                throw new TimeoutException("任务执行超时");
            }
            return parse(html);
        } catch (IOException | InterruptedException | CompletionException e) {
            log.error("课表获取出错", e);
        } catch (TimeoutException e) {
            verifyClient(true);
            throw e;
        }
        return null;
    }

    /**
     * 返回解析后的课表，需要设置userId和termID
     * @param html
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    private List<Course> parse(String html) throws IOException, InterruptedException {
        Document document = Jsoup.parse(html);
        document.select("style").remove();
        html = document.toString();
        HashMap<String, String> param = new HashMap<>(1);
        param.put("html", html);
        HttpRequest request = HttpRequest.newBuilder(URI.create(apiConfig.getCourse()))
                .POST(HttpRequest.BodyPublishers.ofString(JsonUtil.obj2String(param)))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = parseClient.send(request, HttpResponse.BodyHandlers.ofString());
        List<CourseDto> courseDtos = JsonUtil.jsonToList(response.body(), CourseDto.class);
        List<Course> courses = courseDtos.stream().map((dto) -> {
            List<Integer> weeks = dto.getWeeks();
            List<Integer> sections = dto.getSections();
            int startWeek = -1, endWeek = -1;
            int startSection = -1, endSection = -1;
            if (weeks != null && !weeks.isEmpty()) {
                startWeek = Collections.min(weeks);
                endWeek = Collections.max(weeks);
            }
            if (sections != null && !sections.isEmpty()) {
                startSection = Collections.min(sections);
                endSection = Collections.max(sections);
            }
            int day = dto.getDay();
            return new Course(null, dto.getName(), dto.getTeacher(), dto.getPosition(), null, null,
                    (byte) startWeek, (byte) endWeek, (byte) startSection, (byte) endSection, null, (byte) day);
        }).collect(Collectors.toList());
        return courses;
    }

    private String doGetCourses(CloseableHttpClient client, BasicCookieStore cookieStore, String stuNo, String term) throws IOException {


        // 获取课表
        HttpGet courseGet = new HttpGet(JWConsts.composeCourseUrl(term, stuNo));
        // 设置请求头
        setCourseHeader(courseGet);
        // 执行请求
        CloseableHttpResponse httpResponse = client.execute(courseGet);
        return EntityUtils.toString(httpResponse.getEntity());
    }

    private void verifyClient(boolean forceLogin) {
        if (!forceLogin) {
            if (clientCookieStore == null) {
                LoginService loginService = new LoginService();
                clientCookieStore = loginService.login(JWConsts.username, JWConsts.password);
                // 建立与教务系统的通信信道
                HttpGet get = new HttpGet(JWConsts.EDUCATIONAL_SYSTEM);
                try {
                    clientCookieStore.getFirst().execute(get);
                } catch (IOException e) {
                    log.error("与教务处通信建立失败", e);
                }
            }
        } else {
            LoginService loginService = new LoginService();
            clientCookieStore = loginService.login(JWConsts.username, JWConsts.password);
        }
    }

    private void setCourseHeader(HttpGet get) {
        get.addHeader("Host", "jw.cdut.edu.cn");
        get.addHeader("Connection", "keep-alive");
        get.addHeader("Upgrade-Insecure-Requests", "1");
        get.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.5005.63 Safari/537.36 Edg/102.0.1245.30");
        get.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        get.addHeader("Referer", "https://jw.cdut.edu.cn/jsxsd/xskb/xsqtkb.do");
    }
}
