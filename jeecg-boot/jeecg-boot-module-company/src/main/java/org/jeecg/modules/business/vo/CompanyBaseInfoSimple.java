package org.jeecg.modules.business.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

@Data
public class CompanyBaseInfoSimple {
    /**companybase id*/
    @Excel(name = "企业id", width = 15)
    @ApiModelProperty(value = "企业id")
    private java.lang.String id;

    /**企业名称*/
    @Excel(name = "企业名称", width = 15)
    @ApiModelProperty(value = "企业名称")
    private java.lang.String companyName;



    /**统一社会信用代码*/
    @Excel(name = "统一社会信用代码", width = 15)
    @ApiModelProperty(value = "统一社会信用代码")
    private java.lang.String socialCreditCode;

    /**企业法人*/
    @Excel(name = "企业法人", width = 15)
    @ApiModelProperty(value = "企业法人")
    private java.lang.String corporate;
}
