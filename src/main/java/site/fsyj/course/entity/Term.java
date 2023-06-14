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

import java.util.Date;

@ApiModel(value="site-fsyj-course-entity-Term")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "term")
public class Term {
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value="")
    private Long id;

    @TableField(value = "school_id")
    @ApiModelProperty(value="")
    private Integer schoolId;

    /**
     * 学期描述
     */
    @TableField(value = "term_describe")
    @ApiModelProperty(value="学期描述")
    private String termDescribe;

    /**
     * 开始时间
     */
    @TableField(value = "start_date")
    @ApiModelProperty(value="开始时间")
    private Date startDate;

    /**
     * 结束时间
     */
    @TableField(value = "end_date")
    @ApiModelProperty(value="结束时间")
    private Date endDate;

    public static final String COL_ID = "id";

    public static final String COL_SCHOOL_ID = "school_id";

    public static final String COL_DESCRIBE = "term_describe";

    public static final String COL_START_DATE = "start_date";

    public static final String COL_END_DATE = "end_date";
}
