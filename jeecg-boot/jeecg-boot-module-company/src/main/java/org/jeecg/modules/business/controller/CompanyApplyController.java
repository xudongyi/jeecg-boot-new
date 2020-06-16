package org.jeecg.modules.business.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.modules.business.entity.CompanyApply;
import org.jeecg.modules.business.entity.CompanyFile;
import org.jeecg.modules.business.entity.CompanyQualification;
import org.jeecg.modules.business.service.*;
import org.jeecg.modules.business.utils.Constant;
import org.jeecg.modules.business.utils.Equator;
import org.jeecg.modules.business.utils.FieldBaseEquator;
import org.jeecg.modules.business.utils.ServiceUtils;
import org.jeecg.modules.business.vo.CompanyApplyVo;
import org.jeecg.modules.business.vo.CompanyBaseInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @Description: 企业申报基础表
 * @Author: jeecg-boot
 * @Date: 2020-06-02
 * @Version: V1.0
 */
@Api(tags = "企业申报基础表")
@RestController
@RequestMapping("/company/apply")
@Slf4j
public class CompanyApplyController extends JeecgController<CompanyApply, ICompanyApplyService> {
    @Autowired
    private ICompanyApplyService companyApplyService;
    @Autowired
    private ICompanyBaseinfoService companyBaseinfoService;
    @Autowired
    private ICompanyAcceptanceService companyAcceptanceService;
    @Autowired
    private ICompanyQualificationService companyQualificationService;
    @Autowired
    private ICompanyFileService companyFileService;



    /**
     * 分页列表查询
     *
     * @param companyApply
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "企业申报基础表-分页列表查询")
    @ApiOperation(value = "企业申报基础表-分页列表查询", notes = "企业申报基础表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(CompanyApply companyApply,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<CompanyApply> queryWrapper = QueryGenerator.initQueryWrapper(companyApply, req.getParameterMap());
        Page<CompanyApply> page = new Page<CompanyApply>(pageNo, pageSize);
        IPage<CompanyApply> pageList = companyApplyService.page(page, queryWrapper);
        return Result.ok(pageList);
    }
    /**
     * 分页列表查询
     *
     * @param companyIds
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "企业申报基础表-分页列表查询")
    @ApiOperation(value = "企业申报基础表-分页列表查询", notes = "根据用户id查询")
    @GetMapping (value = "/listByUserId")
    public Result<?> listByUserId(@RequestParam(name = "companyIds", required = true) String  companyIds,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                  @RequestParam(name = "status", required = false) String  status,
                                  @RequestParam(name = "fromTable", required = false) String  fromTable,
                                   HttpServletRequest req) {


        Page<CompanyApplyVo> page = new Page<CompanyApplyVo>(pageNo, pageSize);
        IPage<CompanyApplyVo> pageList =  companyApplyService.queryCompanyApplyVo(page,companyIds.split(","),status,fromTable);
        return Result.ok(pageList);
    }
    /**
     * 添加
     *
     * @param companyApply
     * @return
     */
    @AutoLog(value = "企业申报基础表-添加")
    @ApiOperation(value = "企业申报基础表-添加", notes = "企业申报基础表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody CompanyApply companyApply) {
        companyApplyService.save(companyApply);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param companyApply
     * @return
     */
    @AutoLog(value = "企业申报基础表-编辑")
    @ApiOperation(value = "企业申报基础表-编辑", notes = "企业申报基础表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody CompanyApply companyApply) {
        companyApplyService.updateById(companyApply);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "企业申报基础表-通过id删除")
    @ApiOperation(value = "企业申报基础表-通过id删除", notes = "企业申报基础表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        companyApplyService.removeById(id);
        return Result.ok("删除成功!");
    }
    /**
     *   添加
     *
     * @param applyId
     * @return
     */
    @AutoLog(value = "查询待审核数据")
    @ApiOperation(value="查询待审核数据", notes="查询待审核数据")
    @GetMapping(value = "/queryAduitBase")
    public Result<?> queryAduitBase(@RequestParam(required = true) String applyId) {
        Map<String,Object> result = new HashMap<>();
        CompanyApply companyApply = companyApplyService.getById(applyId);

        Object newInfo = getObject(companyApply, companyApply.getNewId());
        //申请人名字
        result.put("info", newInfo);

        if(StrUtil.isEmpty(companyApply.getOldId())){
            //提示
            result.put("cueColor","");
            return  Result.ok(result);
        }

        //获取老数据
        Object oldInfo = getObject(companyApply, companyApply.getOldId());

        //新老数据对比
        FieldBaseEquator fieldBaseEquator = new FieldBaseEquator();
        List<String> list = fieldBaseEquator.getDiffFieldNames(newInfo,oldInfo);
        result.put("cueColor","");
        result.put("cueField",list);
        return  Result.ok(result);
    }

    private Object getObject(CompanyApply companyApply, String newId) {
        Object newInfo;
        if(companyApply.getFromTable().equals(Constant.tables.BASEINFO)) {//获取新数据
            newInfo = companyBaseinfoService.getCompanyBaseInfo(newId);

        }else{
            newInfo = ServiceUtils.getService(companyApply.getFromTable()).getById(newId);
        }
        return newInfo;
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "企业申报基础表-批量删除")
    @ApiOperation(value = "企业申报基础表-批量删除", notes = "企业申报基础表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.companyApplyService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "企业申报基础表-通过id查询")
    @ApiOperation(value = "企业申报基础表-通过id查询", notes = "企业申报基础表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        CompanyApply companyApply = companyApplyService.getById(id);
        if (companyApply == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(companyApply);
    }

    /**
     * 通过id查询
     *
     * @param companyId
     * @return
     */
    @AutoLog(value = "企业申报基础表-通过id查询")
    @ApiOperation(value = "企业申报基础表-通过id查询", notes = "企业申报基础表-通过id查询")
    @GetMapping(value = "/queryLatestArchivedData")
    public Result<?> queryLatestArchivedData(@RequestParam(name = "companyId", required = true) String companyId, @RequestParam(name = "fromTable", required = true) String fromTable) {
        //查询正常状态的  申报信息
        QueryWrapper<CompanyApply> companyWrapper = new QueryWrapper<CompanyApply>();
        companyWrapper.eq("company_id", companyId).eq("from_table", fromTable).
                and(wrapper -> wrapper.eq("status", Constant.status.NORMAL).or().eq("status", Constant.status.PEND));
        int num = companyApplyService.count(companyWrapper);
        Result<Object> ok;
        if (num == 0) {
            ok = Result.ok(false);
        }else {
            ok = Result.ok(true);
        }
        return ok;

    }



    /**
     * 通过id查询
     *
     * @param companyId
     * @return
     */
    @AutoLog(value = "企业申报基础表-通过id查询")
    @ApiOperation(value = "企业申报基础表-通过id查询", notes = "企业申报基础表-通过id查询")
    @GetMapping(value = "/queryQualification")
    public Result<?> queryQualification(@RequestParam(name = "companyId", required = true) String companyId
            , @RequestParam(name = "qualificttionType", required = true) String qualificttionType) {

        int num = companyQualificationService.count(new QueryWrapper<CompanyQualification>().lambda().eq(CompanyQualification::getCompanyId, companyId)
                .and(wrapper -> wrapper.ne(CompanyQualification::getApplyDeleteId, "").or().eq(CompanyQualification::getStatus, Constant.status.PEND))
                .eq(CompanyQualification::getType,qualificttionType));
        Result<Object> ok;
        if (num == 0) {
            ok = Result.ok(false);
        }else {
            ok = Result.ok(true);
        }
        return ok;

    }
    /**
     * 查询申报前后对比信息
     *
     * @param beforeId
     * @param afterId
     * @param fromTable
     */
    @RequestMapping(value = "/queryComparisonData")
    @AutoLog(value = "查询申报前后对比信息")
    @ApiOperation(value = "通过企业申报基础表的前后id", notes = "查询申报前后对比信息")
    public Result<?> queryComparisonData(@RequestParam(name = "beforeId", required = false) String beforeId
            , @RequestParam(name = "afterId", required = true) String afterId,
            @RequestParam(name = "fromTable", required = true) String fromTable) {


        ServiceImpl o = ServiceUtils.getService(fromTable);
        //排除字段
        List<String> excludeFields = Arrays.asList("serialVersionUID", "id", "createBy", "createTime", "updateBy", "updateTime", "sysOrgCode", "status","type");
        Equator fieldBaseEquator = new FieldBaseEquator(null, excludeFields);
        return Result.ok(fieldBaseEquator.getDiffFields(o.getById(beforeId), o.getById(afterId)));
    }



    /**
     * 提交申报考核信息
     *
     * @param jsonObject 提交申报考核信息
     */
    @PostMapping(value = "/submitAudit")
    @AutoLog(value = "提交申报考核信息")
    @ApiOperation(value = "提交申报考核信息", notes = "通用")
    public Result<?> submitAudit(@RequestBody JSONObject jsonObject) {
        String userId = jsonObject.getString("userId");
        //申请表Id
        String applyId = jsonObject.getString("applyId");
        //详情表Id
        String newId = jsonObject.getString("newId");
        //详情表Id
        String oldId = jsonObject.getString("oldId");
        //审批结果
        String result = jsonObject.getString("result");
        //备注信息  和  Constant不同过与正常保持一致
        String message = jsonObject.getString("message");
        //修改申请表
        companyApplyService.submitApply(userId,applyId, message ,result);

        //动态获取
        ServiceImpl o = ServiceUtils.getService(jsonObject.getString("fromTable"));
        UpdateWrapper updateWrapper = new UpdateWrapper<>().eq("id",newId).set("status",result);
        o.update(updateWrapper);
        if(result.equals(Constant.status.NORMAL))
        {
            o.update(new UpdateWrapper<>().eq("id",oldId).set("status",Constant.status.EXPIRED));
        }

        return Result.ok();
    }






    /**
     * 查询申报前后对比信息
     *
     * @param applyId

     */
    @RequestMapping(value = "/comparisonQualification")
    @AutoLog(value = "查询企业资质申报前后对比信息")
    @ApiOperation(value = "通过企业资质申报基础表的id", notes = "查询申报前后资质对比信息")
    public Result<?> comparisonQualification(@RequestParam(name = "applyId", required = true) String applyId) {

        return Result.ok(companyQualificationService.compareQualification(applyId));
    }
    /**
     * 资质信息申报
     * @param applyObj  申报信息
     * @return
     */
    @PostMapping(value = "/qualification")
    public Result<?> qualificationApply(@RequestBody JSONObject applyObj) {
        String companyId = applyObj.getString("companyId");
        //先插入 申请表
        CompanyApply apply = new CompanyApply();
        apply.setStatus(Constant.status.PEND);
        apply.setFromTable(Constant.tables.QUALIFICATION);
        apply.setCompanyId(applyObj.getString("companyId"));
        companyApplyService.save(apply);

        JSONArray deleteImgs = applyObj.getJSONArray("deleteImgs");
        if(!deleteImgs.isEmpty()){
            Map<String,Object> updateparams = new HashMap<>();
            updateparams.put("APPLY_DELETE_ID",apply.getId());//状态更改为过期
           companyQualificationService.updateQualificationFiles(deleteImgs.toJavaList(String.class),updateparams);
        }

        JSONArray addImgs = applyObj.getJSONArray("addImgs");
        List<CompanyFile> companyFiles = new ArrayList<>();
        if(!addImgs.isEmpty()){
            for(int i=0;i<addImgs.size();i++){
                //新增的
                CompanyQualification qualification = new CompanyQualification();
                qualification.setCompanyId(companyId);
                qualification.setApplyAddId(apply.getId());
                qualification.setStatus(Constant.status.PEND);//待审核
                qualification.setType(applyObj.getString("qualificttionType"));//待审核
                companyQualificationService.save(qualification);
                CompanyFile companyFile = new CompanyFile();
                // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                String file = addImgs.getString(i);
                // 得到 每个对象中的属性值
                String[] array =  file.split("/");
                companyFile.setFilename(array[array.length-1]);
                array[array.length-1] = "";
                companyFile.setFilepath(String.join("/",array));
                companyFile.setFiletype(Constant.fileType.IMAGE);//图片类型
                companyFile.setFromTable(Constant.tables.QUALIFICATION);
                companyFile.setTableId(qualification.getId());
                companyFiles.add(companyFile);
            }
            companyFileService.saveBatch(companyFiles);
        }




        return Result.ok();
    }



    /**
     * 导出excel
     *
     * @param request
     * @param companyApply
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CompanyApply companyApply) {
        return super.exportXls(request, companyApply, CompanyApply.class, "企业申报基础表");
    }


    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, CompanyApply.class);
    }

}
