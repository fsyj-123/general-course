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

@ApiModel(value="site-fsyj-course-entity-School")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "school")
public class School {
    @TableId(value = "id", type = IdType.INPUT)
    @ApiModelProperty(value="")
    private Integer id;

    @TableField(value = "name")
    @ApiModelProperty(value="")
    private String name;

    public static final String COL_ID = "id";

    public static final String COL_NAME = "name";
}