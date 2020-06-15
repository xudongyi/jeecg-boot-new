package org.jeecg.modules.business.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.entity.CompanyComplaintLetter;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.business.vo.CompanyAdminPenaltiesVO;
import org.jeecg.modules.business.vo.CompanyComplaintLetterVO;

import java.util.Date;
import java.util.List;

/**
 * @Description: 信访投诉信息
 * @Author: jeecg-boot
 * @Date:   2020-06-02
 * @Version: V1.0
 */
public interface CompanyComplaintLetterMapper extends BaseMapper<CompanyComplaintLetter> {
    List<CompanyComplaintLetterVO> getCompanyComplaintLetter(Page page, String companyId, String status, String companyName, Date dateBegin, Date dateEnd);
}
