package org.jeecg.modules.business.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

@Data
public class CompanyBaseInfoVo{
        /**详情表ID*/
        @ApiModelProperty(value = "详情表ID")
        private java.lang.String id;
        /**企业名称*/
        @Excel(name = "企业名称", width = 15)
        @ApiModelProperty(value = "企业名称")
        private java.lang.String companyName;

        /**数据状态*/
        @Excel(name = "数据状态", width = 15)
        @ApiModelProperty(value = "数据状态")
        private java.lang.String status;
        /**企业id*/
        @Excel(name = "企业id", width = 15)
        @ApiModelProperty(value = "企业id")
        private java.lang.String companyId;
        /**企业简称*/
        @Excel(name = "企业简称", width = 15)
        @ApiModelProperty(value = "企业简称")
        private java.lang.String shortName;
        /**统一社会信用代码*/
        @Excel(name = "统一社会信用代码", width = 15)
        @ApiModelProperty(value = "统一社会信用代码")
        private java.lang.String socialCreditCode;
        /**企业类型*/
        @Excel(name = "企业类型", width = 15)
        @ApiModelProperty(value = "企业类型")
        private java.lang.String companyType;
        /**所属行政区*/
        @Excel(name = "所属行政区", width = 15)
        @ApiModelProperty(value = "所属行政区")
        private java.lang.String administrativeRegion;
        /**所属行业*/
        @Excel(name = "所属行业", width = 15)
        @ApiModelProperty(value = "所属行业")
        private java.lang.String industry;
        /**企业地址*/
        @Excel(name = "企业地址", width = 15)
        @ApiModelProperty(value = "企业地址")
        private java.lang.String address;
        /**经度*/
        @Excel(name = "经度", width = 15)
        @ApiModelProperty(value = "经度")
        private java.lang.String longitude;
        /**纬度*/
        @Excel(name = "纬度", width = 15)
        @ApiModelProperty(value = "纬度")
        private java.lang.String dimension;
        /**企业法人*/
        @Excel(name = "企业法人", width = 15)
        @ApiModelProperty(value = "企业法人")
        private java.lang.String corporate;
        /**企业法人电话*/
        @Excel(name = "企业法人电话", width = 15)
        @ApiModelProperty(value = "企业法人电话")
        private java.lang.String corporatePhone;
        /**经济类型*/
        @Excel(name = "经济类型", width = 15)
        @ApiModelProperty(value = "经济类型")
        private java.lang.String economicType;
        /**企业行政隶属关系*/
        @Excel(name = "企业行政隶属关系", width = 15)
        @ApiModelProperty(value = "企业行政隶属关系")
        private java.lang.String affiliation;
        /**环保负责人*/
        @Excel(name = "环保负责人", width = 15)
        @ApiModelProperty(value = "环保负责人")
        private java.lang.String envProtectPrincipal;
        /**环保负责人电话*/
        @Excel(name = "环保负责人电话", width = 15)
        @ApiModelProperty(value = "环保负责人电话")
        private java.lang.String principalPhone;
        /**环保联系人*/
        @Excel(name = "环保联系人", width = 15)
        @ApiModelProperty(value = "环保联系人")
        private java.lang.String envProtectContact;
        /**环保联系人电话*/
        @Excel(name = "环保联系人电话", width = 15)
        @ApiModelProperty(value = "环保联系人电话")
        private java.lang.String contactPhone;
        /**应急负责人*/
        @Excel(name = "应急负责人", width = 15)
        @ApiModelProperty(value = "应急负责人")
        private java.lang.String emergencyLeader;
        /**应急负责人电话*/
        @Excel(name = "应急负责人电话", width = 15)
        @ApiModelProperty(value = "应急负责人电话")
        private java.lang.String leaderPhone;
        /**所属流域*/
        @Excel(name = "所属流域", width = 15)
        @ApiModelProperty(value = "所属流域")
        private java.lang.String drainageArea;
        /**邮政编码*/
        @Excel(name = "邮政编码", width = 15)
        @ApiModelProperty(value = "邮政编码")
        private java.lang.String postalCode;
        /**传真*/
        @Excel(name = "传真", width = 15)
        @ApiModelProperty(value = "传真")
        private java.lang.String fax;
        /**电子邮箱*/
        @Excel(name = "电子邮箱", width = 15)
        @ApiModelProperty(value = "电子邮箱")
        private java.lang.String email;
        /**工业总产值*/
        @Excel(name = "工业总产值", width = 15)
        @ApiModelProperty(value = "工业总产值")
        private java.lang.String industrialOutput;
        /**员工人数（人）*/
        @Excel(name = "员工人数（人）", width = 15)
        @ApiModelProperty(value = "员工人数（人）")
        private java.lang.String staffCount;
        /**企业规模*/
        @Excel(name = "企业规模", width = 15)
        @ApiModelProperty(value = "企业规模")
        private java.lang.String enterpriseSize;
        /**厂区面积（平方米）*/
        @Excel(name = "厂区面积（平方米）", width = 15)
        @ApiModelProperty(value = "厂区面积（平方米）")
        private java.lang.String factoryArea;
        /**是否位于化工集中区*/
        @Excel(name = "是否位于化工集中区", width = 15)
        @ApiModelProperty(value = "是否位于化工集中区")
        private java.lang.String ischemicals;
        /**所属园区*/
        @Excel(name = "所属园区", width = 15)
        @ApiModelProperty(value = "所属园区")
        private java.lang.String attachedPark;
        /**母公司名称*/
        @Excel(name = "母公司名称", width = 15)
        @ApiModelProperty(value = "母公司名称")
        private java.lang.String parentCompany;
        /**集团公司名称*/
        @Excel(name = "集团公司名称", width = 15)
        @ApiModelProperty(value = "集团公司名称")
        private java.lang.String groupCompany;
        /**注册资金（万元）*/
        @Excel(name = "注册资金（万元）", width = 15)
        @ApiModelProperty(value = "注册资金（万元）")
        private java.lang.String registeCapital;
        /**年销售收入(万元)*/
        @Excel(name = "年销售收入(万元)", width = 15)
        @ApiModelProperty(value = "年销售收入(万元)")
        private java.lang.String annualSalesIncome;
        /**年利润(万元)*/
        @Excel(name = "年利润(万元)", width = 15)
        @ApiModelProperty(value = "年利润(万元)")
        private java.lang.String annualProfit;
        /**资产总额（万元）*/
        @Excel(name = "资产总额（万元）", width = 15)
        @ApiModelProperty(value = "资产总额（万元）")
        private java.lang.String totalAssets;
        /**工商注册地址*/
        @Excel(name = "工商注册地址", width = 15)
        @ApiModelProperty(value = "工商注册地址")
        private java.lang.String registeAddress;
        /**经营范围*/
        @Excel(name = "经营范围", width = 15)
        @ApiModelProperty(value = "经营范围")
        private java.lang.String operateScope;
        /**企业简介*/
        @Excel(name = "企业简介", width = 15)
        @ApiModelProperty(value = "企业简介")
        private java.lang.String profile;

}
