package site.fsyj.course.dto;

import lombok.Data;

/**
 * @author fsyj
 */
@Data
public class WeekCourse {
    private String name;
    private String place;
    private int[] sections;
    private int day;
    private int week;
    private String teacher;
    private Double credit;

    public void setSections(int start, int end) {
        sections = new int[]{start, end};
    }

    public WeekCourse() {
    }

    public WeekCourse(String name, String place, int day, int week, String teacher, Double credit, int start, int end) {
        this.name = name;
        this.place = place;
        this.day = day;
        this.week = week;
        this.teacher = teacher;
        this.credit = credit;
        this.sections = new int[]{start, end};
    }
}
