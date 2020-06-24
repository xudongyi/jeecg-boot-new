package org.jeecg.modules.business.vo;

import lombok.Data;

@Data
public class QualificationBaseInfo {
    /**
     * 资质表id
     */
    private String id;
    /**
     * 资质类型
     */
    private String type;
    /**
     * 资质文件路径
     */
    private String filepath;
    /**
     * 资质文件名称
     */
    private String filename;
    /**
     * 删除对应的申报
     */
    private String applyDeleteId;
    /**
     * 新增对应的申报
     */
    private String applyAddId;
}
