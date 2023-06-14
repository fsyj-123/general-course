package site.fsyj.course.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FailedTask {
    private User user;
    private Term term;
    private LocalDateTime failureTime;
    private Exception failureReason;
}
