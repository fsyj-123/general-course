package site.fsyj.course.dto;

import lombok.Data;

import java.util.List;

@Data
public class CourseDto {
    private String name;
    private String position;
    private List<Integer> sections;
    private List<Integer> weeks;
    private Integer day;
    private String teacher;
}
