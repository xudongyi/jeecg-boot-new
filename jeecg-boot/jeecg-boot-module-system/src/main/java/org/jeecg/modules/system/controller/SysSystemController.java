package org.jeecg.modules.system.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.MD5Util;
import org.jeecg.common.util.PasswordUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.business.entity.CompanyApply;
import org.jeecg.modules.business.entity.CompanyBase;
import org.jeecg.modules.business.service.ICompanyBaseService;
import org.jeecg.modules.business.service.ICompanyBasicService;
import org.jeecg.modules.business.service.ICompanySysuserService;
import org.jeecg.modules.system.entity.*;
import org.jeecg.modules.system.model.SysPermissionTree;
import org.jeecg.modules.system.model.TreeModel;
import org.jeecg.modules.system.service.*;
import org.jeecg.modules.system.util.PermissionDataUtil;
import org.jeecg.modules.system.vo.SysCompanyUser;
import org.jeecg.modules.system.vo.ViewSysPermission;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单权限表 前端控制器
 * </p>
 *
 * @Author scott
 * @since 2018-12-21
 */
@Slf4j
@RestController
@RequestMapping("/sys/system")
public class SysSystemController {

	@Autowired
	private ISysSystemService sysSystemService;

	@Autowired
	private ICompanyBaseService companyBaseService;

	@Autowired
	private ISysUserService sysUserService;
	@Autowired
	private ICompanySysuserService companySysuserService;
	/**
	 *
	 * 
	 * @return  企业列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public Result<List<SysSystem>> list() {
		Result<List<SysSystem>> result = new Result<>();
		result.setResult(sysSystemService.list());
		result.setSuccess(true);
		return result;
	}

	/**
	 * 通过excel导入数据
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/importExcel", method = RequestMethod.POST)
	public Result<?> importExcel(HttpServletRequest request) {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			MultipartFile file = entity.getValue();// 获取上传文件对象
			ImportParams params = new ImportParams();
			params.setTitleRows(1);
			params.setHeadRows(1);
			params.setNeedSave(true);
			try {
				List<SysCompanyUser> list = ExcelImportUtil.importExcel(file.getInputStream(), SysCompanyUser.class, params);

				//update-begin-author:taoyan date:20190528 for:批量插入数据
				long start = System.currentTimeMillis();
				for(SysCompanyUser sysCompanyUser:list){
					CompanyBase companyBase = new CompanyBase();
					companyBase.setCompanyName(sysCompanyUser.getCompanyName());
					companyBaseService.save(companyBase);
					SysUser user = new SysUser();
					user.setUsername(sysCompanyUser.getUserName());
					user.setRealname(sysCompanyUser.getUserName());
					user.setCreateTime(new Date());//设置创建时间
					String salt = oConvertUtils.randomGen(8);
					user.setSalt(salt);
					String passwordEncode = PasswordUtil.encrypt(user.getUsername(), user.getPassword(), salt);
					user.setPassword(passwordEncode);
					user.setStatus(1);
					user.setDelFlag(CommonConstant.DEL_FLAG_0);
					sysUserService.addUserWithRole(user, "1265250751097147394");//企业用户的id
					companySysuserService.save(user.getId(), companyBase.getId());
				}
				log.info("消耗时间" + (System.currentTimeMillis() - start) + "毫秒");
				//update-end-author:taoyan date:20190528 for:批量插入数据
				return Result.ok("文件导入成功！数据行数：" + list.size());
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				return Result.error("文件导入失败:" + e.getMessage());
			} finally {
				try {
					file.getInputStream().close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return Result.error("文件导入失败！");
	}

	/**
	 * 导出excel
	 *
	 * @param request
	 */
	@RequestMapping(value = "/exportXls")
	public ModelAndView exportXls(HttpServletRequest request) {

		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		mv.addObject(NormalExcelConstants.FILE_NAME, "企业用户批量导入模板"); //此处设置的filename无效 ,前端会重更新设置一下
		mv.addObject(NormalExcelConstants.CLASS, SysCompanyUser.class);
		mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("企业用户批量导入模板",  "企业用户批量导入"));
		mv.addObject(NormalExcelConstants.DATA_LIST, new ArrayList<>());
		return mv;
	}

}
