package site.fsyj.course.dto;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class DayCourse {
    private String name;
    private String place;
    private byte start;
    private byte end;
    private String teacher;

    public DayCourse() {
    }

    public DayCourse(String name, String place, byte start, byte end, String teacher) {
        this.name = name;
        this.place = place;
        this.start = start;
        this.end = end;
        this.teacher = teacher;
    }
}
