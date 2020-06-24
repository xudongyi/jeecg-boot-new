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
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
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
    @Autowired
    FieldBaseEquator fieldBaseEquator;


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


        Page<CompanyApplyVo> page = new Page<>(pageNo, pageSize);
        IPage<CompanyApplyVo> pageList =  companyApplyService.queryCompanyApplyVo(page,req.getParameter("companyId").split(",")
                ,"",req.getParameter("fromTable"));

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


        Page<CompanyApplyVo> page = new Page<>(pageNo, pageSize);
        IPage<CompanyApplyVo> pageList =  companyApplyService.queryCompanyApplyVo(page,companyIds.split(","),status,fromTable);
        return Result.ok(pageList);
    }
    /**
     * 添加
     *
     * @param companyApply 企业申报基础表
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
        //获取老数据
        Object oldInfo = getObject(companyApply, companyApply.getOldId());

        List<String> excludeFields = Arrays.asList("serialVersionUID", "id", "createBy", "createTime", "updateBy", "updateTime", "sysOrgCode", "status","type","companyId");
        fieldBaseEquator.setExcludeFields(excludeFields);
        List<String> list = fieldBaseEquator.getDiffFieldNames(newInfo,oldInfo);
        result.put("cueColor","PART");
        result.put("cueField",list);
        if(StrUtil.isEmpty(companyApply.getOldId())){
            //提示
            result.put("cueColor","ALL");

        }

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
       Map<String,Integer> result = new HashMap<>(2);
        result.put("total",companyApplyService.count(new QueryWrapper<CompanyApply>().eq("company_id", companyId).eq("from_table", fromTable).eq("status", Constant.status.NORMAL)));
        result.put("pend",companyApplyService.count(new QueryWrapper<CompanyApply>().eq("company_id", companyId).eq("from_table", fromTable).eq("status", Constant.status.PEND)));
        result.put("total",result.get("pend")+result.get("total"));
        return Result.ok(result);

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
        List<String> excludeFields = Arrays.asList("serialVersionUID", "id", "createBy", "createTime", "updateBy", "updateTime", "sysOrgCode", "status","type","companyId");
        fieldBaseEquator.setExcludeFields(excludeFields);
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
        CompanyApply companyApply = companyApplyService.getById(applyId);

        //审批结果
        String result = jsonObject.getString("result");
        //备注信息  和  Constant不同过与正常保持一致
        String message = jsonObject.getString("message");
        //修改申请表
        companyApplyService.submitApply(userId,applyId, message ,result);

        //动态获取
        ServiceImpl o = ServiceUtils.getService(jsonObject.getString("fromTable"));
        UpdateWrapper updateWrapper = new UpdateWrapper<>().eq("id",companyApply.getNewId()).set("status",result);
        o.update(updateWrapper);
        if(result.equals(Constant.status.NORMAL))
        {
            o.update(new UpdateWrapper<>().eq("id",companyApply.getOldId()).set("status",Constant.status.EXPIRED));
        }

        return Result.ok();
    }

    /**
     * 提交申报考核信息
     *
     * @param jsonObject 提交申报考核信息
     */
    @PostMapping(value = "/batchFail")
    @AutoLog(value = "批量不批准考核信息")
    @ApiOperation(value = "批量不批准考核信息", notes = "通用")
    public Result<?> batchFail(@RequestBody JSONObject jsonObject) {
        String[]  ids = jsonObject.getString("ids").split(",");
        if(ids.length>0){

            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            //修改申请表
            companyApplyService.submitApply(sysUser.getId(),jsonObject.getString("ids"), "" ,Constant.status.NOPASS);
            Collection<CompanyApply> companyApplys = companyApplyService.listByIds(Arrays.asList(ids));
            //多个
            Map<String,List<String>> services = new HashMap<>();
            for(CompanyApply companyApply:companyApplys){

                if(companyApply.getFromTable().equals(Constant.tables.QUALIFICATION)){
                    companyQualificationService.update(new UpdateWrapper<CompanyQualification>().lambda()
                            .eq(CompanyQualification::getApplyAddId,companyApply.getId()).set(CompanyQualification::getStatus,Constant.status.NOPASS));
                    continue;
                }


                services.putIfAbsent(companyApply.getFromTable(),new ArrayList<>());

                services.get(companyApply.getFromTable()).add(companyApply.getNewId());

            }

            for(Map.Entry<String,List<String>> entry : services.entrySet()){

                ServiceImpl o = ServiceUtils.getService(entry.getKey());
                UpdateWrapper updateWrapper = new UpdateWrapper<>().in("id",entry.getValue()).set("status",Constant.status.NOPASS);
                o.update(updateWrapper);

            }

        }

        return Result.ok();
    }

    /**
     * 提交申报考核信息
     *
     * @param jsonObject 提交申报考核信息
     */
    @PostMapping(value = "/batchPass")
    @AutoLog(value = "批量批准考核信息")
    @ApiOperation(value = "批量批准考核信息", notes = "通用")
    public Result<?> batchPass(@RequestBody JSONObject jsonObject) {
        String[]  ids = jsonObject.getString("ids").split(",");
        if(ids.length>0){
            //修改申请表
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            //修改申请表
            companyApplyService.submitApply(sysUser.getId(),jsonObject.getString("ids"), "" ,Constant.status.NORMAL);
            Collection<CompanyApply> companyApplys = companyApplyService.listByIds(Arrays.asList(ids));
            //多个
            Map<String,Map<String,List<String>>> services = new HashMap<>();
            for(CompanyApply companyApply:companyApplys){


                if(companyApply.getFromTable().equals(Constant.tables.QUALIFICATION)){
                    companyQualificationService.update(new UpdateWrapper<CompanyQualification>().lambda()
                            .eq(CompanyQualification::getApplyAddId,companyApply.getId()).set(CompanyQualification::getStatus,Constant.status.NORMAL));
                    companyQualificationService.update(new UpdateWrapper<CompanyQualification>().lambda().eq(CompanyQualification::getApplyDeleteId,companyApply.getId())
                            .set(CompanyQualification::getStatus,Constant.status.EXPIRED));

                    continue;
                }

                services.putIfAbsent(companyApply.getFromTable(),new HashMap<>());

                services.get(companyApply.getFromTable()).putIfAbsent("old",new ArrayList<>());
                services.get(companyApply.getFromTable()).putIfAbsent("new",new ArrayList<>());
                services.get(companyApply.getFromTable()).get("new").add(companyApply.getNewId());
                services.get(companyApply.getFromTable()).get("old").add(companyApply.getOldId());

            }

            for(Map.Entry<String,Map<String,List<String>>> entry : services.entrySet()){

                ServiceImpl o = ServiceUtils.getService(entry.getKey());
                UpdateWrapper updateWrapper = new UpdateWrapper<>().in("id",entry.getValue().get("new")).set("status",Constant.status.NORMAL);
                o.update(updateWrapper);
                o.update(new UpdateWrapper<>().in("id",entry.getValue().get("old")).set("status",Constant.status.EXPIRED));
            }

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
