package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.entity.CompanyComplaintLetter;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.vo.CompanyAdminPenaltiesVO;
import org.jeecg.modules.business.vo.CompanyComplaintLetterVO;

import java.util.Date;

/**
 * @Description: 信访投诉信息
 * @Author: jeecg-boot
 * @Date:   2020-06-02
 * @Version: V1.0
 */
public interface ICompanyComplaintLetterService extends IService<CompanyComplaintLetter> {
    Integer findCountByCompanyId(String companyId);
    Page<CompanyComplaintLetterVO> getCompanyComplaintLetter(Page<CompanyComplaintLetterVO> page, String companyId, String status, String companyName, Date dateBegin, Date dateEnd);
}
