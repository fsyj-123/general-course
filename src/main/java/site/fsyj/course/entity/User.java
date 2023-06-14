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

@ApiModel(value="site-fsyj-course-entity-User")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "`user`")
public class User {
    @TableId(value = "id", type = IdType.INPUT)
    @ApiModelProperty(value="")
    private Long id;

    @TableField(value = "school")
    @ApiModelProperty(value="")
    private Integer school;

    @TableField(value = "openid")
    @ApiModelProperty(value="")
    private String  openid;

    @TableField(value = "name")
    @ApiModelProperty(value="")
    private String  name;

    @TableField(value = "stu_no")
    @ApiModelProperty(value="")
    private String  stuNo;

    public static final String COL_ID = "id";

    public static final String COL_SCHOOL = "school";

    public static final String COL_OPENID = "openid";

    public static final String COL_NAME = "name";

    public static final String COL_STU_NO = "stu_no";


}
