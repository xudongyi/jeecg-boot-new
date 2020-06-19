package org.jeecg.modules.business.vo;

import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;

@Data
public class CompanyApplyVo {
    /**
     * 申请id
     */
    private String id;

    /**
     * 企业名称
     */
    private String companyName;
    /**
     * 审核单元
     */
    @Dict(dicCode = "fromTable")
    private String fromTable;
    /**
     * 申报时间
     */
    private String createTime;
    /**
     * 申报时间
     */
    private String createBy;
    /**
     * 申报状态
     */
    @Dict(dicCode = "statue")
    private String status;
    /**
     * 审核人
     */
    private String updateBy;
    /**
     *  生效时间
     */
    private String updateTime;
    private java.lang.String newId;
    private String oldId;
    private String content;

}
