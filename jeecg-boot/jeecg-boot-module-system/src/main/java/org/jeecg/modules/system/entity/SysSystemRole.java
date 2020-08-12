package org.jeecg.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class SysSystemRole {
    /**
     * id
     */
    @TableId(type = IdType.ID_WORKER_STR)
    private String id;

    String systemId;
    String roleId;
}
