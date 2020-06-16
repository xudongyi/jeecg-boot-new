package org.jeecg.modules.business.vo;

import lombok.Data;
import org.jeecg.modules.business.entity.CompanyComplaintLetter;

@Data
public class CompanyComplaintLetterVO extends CompanyComplaintLetter {
    private String companyName;
    private String createName;
    private String updateName;
}
