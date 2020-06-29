package org.jeecg.modules.business.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CompanySysUserVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**部门id*/
    private String companyId;
    /**对应的用户id集合*/
    private List<String> userIdList;

    public CompanySysUserVo() {
        super();
    }

    public CompanySysUserVo(String companyId, List<String> userIdList) {
        super();
        this.companyId = companyId;
        this.userIdList = userIdList;
    }
}
