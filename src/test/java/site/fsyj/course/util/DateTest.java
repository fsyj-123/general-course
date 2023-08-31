package site.fsyj.course.util;

import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateTest {
    @Test
    public void timeTest() {
        Date now = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("Asia/Shanghai"))).getTime();
        System.out.println(now);
    }
}
