package site.fsyj.course.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author fsyj on 2021/9/21
 */
@Slf4j
public class JsonUtil {
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";
    static {
        //对象的所有字段全部列入
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        //取消默认转换timestamps形式
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false);
        //忽略空Bean转json的错误
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
        //所有的日期格式都统一为以下的样式，即yyyy-MM-dd HH:mm:ss
        objectMapper.setDateFormat(new SimpleDateFormat(STANDARD_FORMAT));
        //忽略 在json字符串中存在，但是在java对象中不存在对应属性的情况。防止错误
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
    }

    /**
     * 对象转Json格式字符串
     * @param obj 对象
     * @return Json格式字符串
     */
    public static <T> String obj2String(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.warn("Parse Object to String error : {}", e.getMessage());
            return null;
        }
    }

    /**
     * 对象转Json格式字符串(格式化的Json字符串)
     * @param obj 对象
     * @return 美化的Json格式字符串
     */
    public static <T> String obj2StringPretty(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.warn("Parse Object to String error : {}", e.getMessage());
            return null;
        }
    }

    /**
     * 字符串转换为自定义对象
     * @param str 要转换的字符串
     * @param clazz 自定义对象的class对象
     * @return 自定义对象
     */
    public static <T> T string2Obj(String str, Class<T> clazz){
        if(StringUtils.isEmpty(str) || clazz == null){
            return null;
        }
        try {
            return clazz.equals(String.class) ? (T) str : objectMapper.readValue(str, clazz);
        } catch (Exception e) {
            log.warn("Parse String to Object error : {}", e.getMessage());
            return null;
        }
    }


    public static Map<String, Object> json2Map(String json) {
        Map<String, Object> map = null;
        try {
            map = objectMapper.readValue(json, Map.class);
        } catch (IOException e) {
            log.error("JSON格式错误");
        }
        return map;
    }

    public static <T> List<T> jsonToList(String jsonList, Class<T> tClass) {
        ArrayList<T> list = new ArrayList<>();
        try {
            T tNode = null;
            JsonNode jsonNode = objectMapper.readTree(jsonList);
            if (jsonNode.isArray()) {
                for (JsonNode node : jsonNode) {
                    tNode = jsonToObject(node.toString(), tClass);
                    list.add(tNode);
                }
            }
        } catch (IOException e) {
            log.error("JSON格式错误");
        }
        return list;
    }

    public static List<String> json2strList(String jsonList, String name) {
        JsonNode jsonNode = null;
        ArrayList<String> list = new ArrayList<>();
        try {
            jsonNode = objectMapper.readTree(jsonList).get(name);
            if (jsonNode.isArray()) {
                for (JsonNode node : jsonNode) {
                    list.add(node.textValue());
                }
            }
        } catch (IOException e) {
            log.error("JSON格式错误");
        }
        return list;
    }


    public static <T> T jsonToObject(String node, Class<T> tClass) {
        T value = null;
        try {
            value = objectMapper.readValue(node, tClass);
        } catch (IOException e) {
            log.error("JSON格式错误");
        }
        return value;
    }

}
