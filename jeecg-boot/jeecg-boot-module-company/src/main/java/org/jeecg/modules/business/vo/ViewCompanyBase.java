package org.jeecg.modules.business.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.modules.business.annotation.Area;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: company_baseinfo
 * @Author: jeecg-boot
 * @Date:   2020-05-27
 * @Version: V1.0
 */
@Data
@TableName("view_company_base")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="view_company_base对象", description="view_company_base")
public class ViewCompanyBase implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
    /**企业id*/
    @Excel(name = "企业id", width = 15)
    @ApiModelProperty(value = "企业id")
    private String companyId;

	/**所属部门*/
    @Excel(name = "企业名称", width = 15)
    @ApiModelProperty(value = "企业名称")
    private String companyName;
	/**数据状态*/
    @ApiModelProperty(value = "数据状态")
    private String status;

	/**企业简称*/
	@Excel(name = "企业简称", width = 15)
    @ApiModelProperty(value = "企业简称")
    private String shortName;
	/**统一社会信用代码*/
	@Excel(name = "统一社会信用代码", width = 15)
    @ApiModelProperty(value = "统一社会信用代码")
    private String socialCreditCode;
	/**企业类型*/
    @Dict(dicCode = "company_type")
	@Excel(name = "企业类型", width = 15)
    @ApiModelProperty(value = "企业类型")
    private String companyType;
	/**所属行政区*/
    @Area
	@Excel(name = "所属行政区", width = 15)
    @ApiModelProperty(value = "所属行政区")
    private String administrativeRegion;
	/**所属行业*/
    @Dict(dicCode = "industry")
	@Excel(name = "所属行业", width = 15)
    @ApiModelProperty(value = "所属行业")
    private String industry;
	/**企业地址*/
	@Excel(name = "企业地址", width = 15)
    @ApiModelProperty(value = "企业地址")
    private String address;
	/**经度*/
	@Excel(name = "经度", width = 15)
    @ApiModelProperty(value = "经度")
    private String longitude;
	/**纬度*/
	@Excel(name = "纬度", width = 15)
    @ApiModelProperty(value = "纬度")
    private String dimension;
	/**企业法人*/
	@Excel(name = "企业法人", width = 15)
    @ApiModelProperty(value = "企业法人")
    private String corporate;
    /**企业法人电话*/
    @Excel(name = "企业法人电话", width = 15)
    @ApiModelProperty(value = "企业法人电话")
    private String corporatePhone;
	/**经济类型*/
    @Dict(dicCode = "economic_type")
	@Excel(name = "经济类型", width = 15)
    @ApiModelProperty(value = "经济类型")
    private String economicType;
	/**企业行政隶属关系*/
	@Excel(name = "企业行政隶属关系", width = 15)
    @ApiModelProperty(value = "企业行政隶属关系")
    private String affiliation;
	/**环保负责人*/
	@Excel(name = "环保负责人", width = 15)
    @ApiModelProperty(value = "环保负责人")
    private String envProtectPrincipal;
	/**环保负责人电话*/
	@Excel(name = "环保负责人电话", width = 15)
    @ApiModelProperty(value = "环保负责人电话")
    private String principalPhone;
	/**环保联系人*/
	@Excel(name = "环保联系人", width = 15)
    @ApiModelProperty(value = "环保联系人")
    private String envProtectContact;
	/**环保联系人电话*/
	@Excel(name = "环保联系人电话", width = 15)
    @ApiModelProperty(value = "环保联系人电话")
    private String contactPhone;
	/**应急负责人*/
	@Excel(name = "应急负责人", width = 15)
    @ApiModelProperty(value = "应急负责人")
    private String emergencyLeader;
	/**应急负责人电话*/
	@Excel(name = "应急负责人电话", width = 15)
    @ApiModelProperty(value = "应急负责人电话")
    private String leaderPhone;
	/**所属流域*/
    @Dict(dicCode = "watershed")
	@Excel(name = "所属流域", width = 15)
    @ApiModelProperty(value = "所属流域")
    private String drainageArea;
	/**邮政编码*/
	@Excel(name = "邮政编码", width = 15)
    @ApiModelProperty(value = "邮政编码")
    private String postalCode;
	/**传真*/
	@Excel(name = "传真", width = 15)
    @ApiModelProperty(value = "传真")
    private String fax;
	/**电子邮箱*/
	@Excel(name = "电子邮箱", width = 15)
    @ApiModelProperty(value = "电子邮箱")
    private String email;
	/**工业总产值*/
	@Excel(name = "工业总产值", width = 15)
    @ApiModelProperty(value = "工业总产值")
    private String industrialOutput;
	/**员工人数（人）*/
	@Excel(name = "员工人数（人）", width = 15)
    @ApiModelProperty(value = "员工人数（人）")
    private String staffCount;
	/**企业规模*/
    @Dict(dicCode = "company_size")
	@Excel(name = "企业规模", width = 15)
    @ApiModelProperty(value = "企业规模")
    private String enterpriseSize;
	/**厂区面积（平方米）*/
	@Excel(name = "厂区面积（平方米）", width = 15)
    @ApiModelProperty(value = "厂区面积（平方米）")
    private String factoryArea;
	/**是否位于化工集中区*/
    @Dict(dicCode = "yes_or_no")
	@Excel(name = "是否位于化工集中区", width = 15)
    @ApiModelProperty(value = "是否位于化工集中区")
    private String ischemicals;
	/**所属园区*/
	@Excel(name = "所属园区", width = 15)
    @ApiModelProperty(value = "所属园区")
    private String attachedPark;
	/**母公司名称*/
	@Excel(name = "母公司名称", width = 15)
    @ApiModelProperty(value = "母公司名称")
    private String parentCompany;
	/**集团公司名称*/
	@Excel(name = "集团公司名称", width = 15)
    @ApiModelProperty(value = "集团公司名称")
    private String groupCompany;
	/**注册资金（万元）*/
	@Excel(name = "注册资金（万元）", width = 15)
    @ApiModelProperty(value = "注册资金（万元）")
    private String registeCapital;
	/**年销售收入(万元)*/
	@Excel(name = "年销售收入(万元)", width = 15)
    @ApiModelProperty(value = "年销售收入(万元)")
    private String annualSalesIncome;
	/**年利润(万元)*/
	@Excel(name = "年利润(万元)", width = 15)
    @ApiModelProperty(value = "年利润(万元)")
    private String annualProfit;
	/**资产总额（万元）*/
	@Excel(name = "资产总额（万元）", width = 15)
    @ApiModelProperty(value = "资产总额（万元）")
    private String totalAssets;
	/**工商注册地址*/
	@Excel(name = "工商注册地址", width = 15)
    @ApiModelProperty(value = "工商注册地址")
    private String registeAddress;
	/**经营范围*/
	@Excel(name = "经营范围", width = 15)
    @ApiModelProperty(value = "经营范围")
    private String operateScope;
	/**企业简介*/
	@Excel(name = "企业简介", width = 15)
    @ApiModelProperty(value = "企业简介")
    private String profile;
    /**创建日期*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
}
