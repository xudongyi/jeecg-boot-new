package org.jeecg.modules.business.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 污染源名录库
 * @Author: jeecg-boot
 * @Date:   2020-09-15
 * @Version: V1.0
 */
@Data
@TableName("source_directory")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="source_directory对象", description="污染源名录库")
public class SourceDirectory implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
	/**企业id*/
	@Excel(name = "企业id", width = 15)
    @ApiModelProperty(value = "企业id")
    private java.lang.String companyId;
	/**污染源类型*/
	@Excel(name = "污染源类型", width = 15)
    @ApiModelProperty(value = "污染源类型")
    private java.lang.String sourceType;
	/**重点排污单位 0:否   1:是*/
	@Excel(name = "重点排污单位 0:否   1:是", width = 15)
    @ApiModelProperty(value = "重点排污单位 0:否   1:是")
    private java.lang.Integer intensiveUnit;
	/**重点监控企业 0:否   1:是*/
	@Excel(name = "重点监控企业 0:否   1:是", width = 15)
    @ApiModelProperty(value = "重点监控企业 0:否   1:是")
    private java.lang.Integer intensiveCompany;
}
