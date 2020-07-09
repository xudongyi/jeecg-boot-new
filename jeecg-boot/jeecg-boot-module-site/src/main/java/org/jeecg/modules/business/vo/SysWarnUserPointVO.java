package org.jeecg.modules.business.vo;

import lombok.Data;
import org.jeecg.modules.business.entity.SysWarnUserPoint;

@Data
public class SysWarnUserPointVO extends SysWarnUserPoint {
    /**站点名称*/
    private String siteName;

    /**联系人姓名*/
    private String name;

    /**手机号码*/
    private String mobile;

    /**所属单位*/
    private String companyName;

    /**所属单位id*/
    private String companyId;
}
