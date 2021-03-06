package org.jeecg.modules.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.CompanyQualification;
import org.jeecg.modules.business.vo.QualificationBaseInfo;

import java.util.List;

/**
 * @Description: 企业资质
 * @Author: jeecg-boot
 * @Date:   2020-06-01
 * @Version: V1.0
 */
public interface CompanyQualificationMapper extends BaseMapper<CompanyQualification> {


    /**
     * 通过公司id获取所有资质信息
     * @param companyId 公司id
     * @return
     */
    public List<QualificationBaseInfo> queryQualificationBaseInfo(@Param("companyId") String companyId, @Param("status") String status);
    /**
     * 通过申请id获取所有删减资质信息
     * @param deleteId 申请id
     * @return
     */
    public List<QualificationBaseInfo> queryDeleteQualification(@Param("deleteId") String deleteId);
    /**
     * 通过申请id获取所有新增资质信息
     * @param addId 申请id
     * @return
     */
    public List<QualificationBaseInfo> queryAddQualification(@Param("addId") String addId);

}
