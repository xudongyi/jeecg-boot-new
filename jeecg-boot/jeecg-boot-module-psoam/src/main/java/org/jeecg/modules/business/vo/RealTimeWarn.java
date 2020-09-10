package org.jeecg.modules.business.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;

@Data
public class RealTimeWarn {
    /**id*/
    @TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
    /**companyId*/
    @Excel(name = "companyName", width = 15)
    @ApiModelProperty(value = "companyName")
    private java.lang.String companyName;
    /**mn*/
    @Excel(name = "siteName", width = 15)
    @ApiModelProperty(value = "siteName")
    private java.lang.String siteName;
    /**warnType*/
    @Excel(name = "warnType", width = 15, dicCode = "warnType")
    @Dict(dicCode = "warnType")
    @ApiModelProperty(value = "warnType")
    private java.lang.String warnType;
    /**code*/
    @Excel(name = "meaning", width = 15)
    @ApiModelProperty(value = "meaning")
    private java.lang.String meaning;
    /**warnLevel*/
    @Excel(name = "warnLevel", width = 15,dicCode = "warnLevel")
    @ApiModelProperty(value = "warnLevel")
    private java.lang.String warnLevel;
    /**content*/
    @Excel(name = "content", width = 15)
    @ApiModelProperty(value = "content")
    private java.lang.String content;
    /**warnTime*/
    @Excel(name = "warnTime", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "warnTime")
    Timestamp warnTime;
}
