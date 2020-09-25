package org.jeecg.modules.business.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: warn_user_rule
 * @Author: jeecg-boot
 * @Date:   2020-09-25
 * @Version: V1.0
 */
@Data
@TableName("warn_user_rule")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="warn_user_rule对象", description="warn_user_rule")
public class WarnUserRule implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
	/**用户id*/
	@Excel(name = "用户id", width = 15)
    @ApiModelProperty(value = "用户id")
    private java.lang.String userId;
	/**责任单位*/
	@Excel(name = "责任单位", width = 15)
    @ApiModelProperty(value = "责任单位")
    private java.lang.String zrCompanyId;
	/**责任站点*/
	@Excel(name = "责任站点", width = 15)
    @ApiModelProperty(value = "责任站点")
    private java.lang.String mn;
	/**短信接收策略*/
	@Excel(name = "短信接收策略", width = 15)
    @ApiModelProperty(value = "短信接收策略")
    private java.lang.String ruleType;
}
