package org.jeecg.modules.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;

/**
 * @Description: 系统区域表
 * @Author: jeecg-boot
 * @Date:   2020-07-03
 * @Version: V1.0
 */
@Data
@TableName("sys_area")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sys_area对象", description="系统区域表")
public class SysArea implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
    private String id;
	/**区域代码*/
	@Excel(name = "区域代码", width = 15)
    @ApiModelProperty(value = "区域代码")
    private String code;
	/**区域名称*/
	@Excel(name = "区域名称", width = 15)
    @ApiModelProperty(value = "区域名称")
    private String name;
	/**层级，1=省，2=市，3=区，4=站点*/
	@Excel(name = "层级，1=省，2=市，3=区，4=站点", width = 15)
    @ApiModelProperty(value = "层级，1=省，2=市，3=区，4=站点")
    private String lvl;
	/**上级代码*/
	@Excel(name = "上级代码", width = 15)
    @ApiModelProperty(value = "上级代码")
    private String parentCode;
	/**启用状态，0：停用，1：启用*/
	@Excel(name = "启用状态，0：停用，1：启用", width = 15)
    @ApiModelProperty(value = "启用状态，0：停用，1：启用")
    private String active;

	public String toString(){
	    return "[区域代码:"+code+",区域名称:"+name+",上级代码:"+parentCode+"]";
    }
}
