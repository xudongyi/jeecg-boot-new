package org.jeecg.modules.system.vo;

import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

@Data
public class SysCompanyUser {

    @Excel(name = "企业名称", width = 20)
    private String companyName;
    @Excel(name = "用户名", width = 20)
    private String userName;
    @Excel(name = "密码", width = 20)
    private String password;


}
