package org.jeecg.modules.business.service;

import org.jeecg.modules.business.entity.CompanySysuser;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @Description: 企业人员信息
 * @Author: jeecg-boot
 * @Date:   2020-05-27
 * @Version: V1.0
 */
public interface ICompanySysuserService extends IService<CompanySysuser> {
    List<CompanySysuser> list(String userid);
    boolean removeByUserId(String userid);
    void save(String userid, String companys);
    boolean queryCompanyIds(String userId, List<String> companyIds);
}
