package site.fsyj.course.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value="site-fsyj-course-entity-Course")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "course")
public class Course {
    @TableId(value = "id", type = IdType.INPUT)
    @ApiModelProperty(value="")
    private Long id;

    /**
     * 课程名
     */
    @TableField(value = "name")
    @ApiModelProperty(value="课程名")
    private String name;

    @TableField(value = "teacher")
    @ApiModelProperty(value="")
    private String teacher;

    /**
     * 上课位置
     */
    @TableField(value = "place")
    @ApiModelProperty(value="上课位置")
    private String place;

    @TableField(value = "credit")
    @ApiModelProperty(value="")
    private Double credit;

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    @ApiModelProperty(value="用户ID")
    private Long userId;

    /**
     * 开始周
     */
    @TableField(value = "start_week")
    @ApiModelProperty(value="开始周")
    private Byte startWeek;

    /**
     * 结束周
     */
    @TableField(value = "end_week")
    @ApiModelProperty(value="结束周")
    private Byte endWeek;

    @TableField(value = "start_section")
    @ApiModelProperty(value="")
    private Byte startSection;

    @TableField(value = "end_section")
    @ApiModelProperty(value="")
    private Byte endSection;

    /**
     * 学期ID
     */
    @TableField(value = "term_id")
    @ApiModelProperty(value="学期ID")
    private Long termId;

    /**
     * 星期数[1,7]
     */
    @TableField(value = "day")
    @ApiModelProperty(value="星期数[1,7]")
    private Byte day;

    public static final String COL_ID = "id";

    public static final String COL_NAME = "name";

    public static final String COL_TEACHER = "teacher";

    public static final String COL_PLACE = "place";

    public static final String COL_CREDIT = "credit";

    public static final String COL_USER_ID = "user_id";

    public static final String COL_START_WEEK = "start_week";

    public static final String COL_END_WEEK = "end_week";

    public static final String COL_START_SECTION = "start_section";

    public static final String COL_END_SECTION = "end_section";

    public static final String COL_TERM_ID = "term_id";

    public static final String COL_DAY = "day";
}
