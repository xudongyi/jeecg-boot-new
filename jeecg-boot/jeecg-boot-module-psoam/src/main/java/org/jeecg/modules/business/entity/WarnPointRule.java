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
 * @Description: warn_point_rule
 * @Author: jeecg-boot
 * @Date:   2020-09-21
 * @Version: V1.0
 */
@Data
@TableName("warn_point_rule")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="warn_point_rule对象", description="warn_point_rule")
public class WarnPointRule implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**mn号*/
	@Excel(name = "mn号", width = 15)
    @ApiModelProperty(value = "mn号")
    private java.lang.String mn;
	/**策略id*/
	@Excel(name = "策略id", width = 15)
    @ApiModelProperty(value = "策略id")
    private java.lang.String ruleId;
	/**是否启用*/
	@Excel(name = "是否启用", width = 15)
    @ApiModelProperty(value = "是否启用")
    private java.lang.String isUsed;
}
