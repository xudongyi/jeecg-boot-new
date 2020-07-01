package org.jeecg.modules.system.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.modules.system.entity.SysPermission;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ViewSysPermission extends SysPermission {

    private String systemId;
    private String systemName;

}
