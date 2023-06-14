package site.fsyj.course.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class UserInfoDto {

    @ApiModelProperty("学号")
    private String stuNo;
}
