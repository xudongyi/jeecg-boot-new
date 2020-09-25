package org.jeecg.modules.business.controller;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.business.entity.CompanyBase;
import org.jeecg.modules.business.entity.SiteDataCollection;
import org.jeecg.modules.business.entity.SiteMonitorPoint;
import org.jeecg.modules.business.service.ICompanyBaseService;
import org.jeecg.modules.business.service.ISiteDataCollectionService;
import org.jeecg.modules.business.service.ISiteMonitorPointService;
import org.jeecg.modules.business.vo.SiteMonitorPointVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
* @Description: 监测站点表
* @Author: jeecg-boot
* @Date:   2020-07-02
* @Version: V1.0
*/
@Api(tags="监测站点表")
@RestController
@RequestMapping("/siteInfo/")
@Slf4j
public class SiteMonitorPointInfoController extends JeecgController<SiteMonitorPoint, ISiteMonitorPointService> {
   @Autowired
   private ISiteMonitorPointService siteMonitorPointService;

   @Autowired
   private ICompanyBaseService companyBaseService;

   @Autowired
   private ISiteDataCollectionService siteDataCollectionService;

    /**
    * 分页列表查询
    *
    * @param pageNo
    * @param pageSize
    * @param req
    * @return
    */
   @AutoLog(value = "监测站点表-分页列表查询")
   @ApiOperation(value="监测站点表-分页列表查询", notes="监测站点表-分页列表查询")
   @GetMapping(value = "/list")
   public Result<?> siteInfoList(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                  HttpServletRequest req) {
       String companyId = req.getParameter("companyId");
       List<String> siteTypes = new ArrayList<String>() {
           {
               add("0");
               add("1");
               add("2");
           }
       };
       Page<SiteMonitorPoint> page = new Page<>(pageNo, pageSize);
       IPage<SiteMonitorPoint> pageList = siteMonitorPointService.page(page,new QueryWrapper<SiteMonitorPoint>().lambda().eq(SiteMonitorPoint::getCompanyId,companyId).in(SiteMonitorPoint::getSiteType,siteTypes));
       return Result.ok(pageList);
   }

    /**
     *   添加
     *
     * @param siteMonitorPoint
     * @return
     */
    @AutoLog(value = "监测站点表-添加")
    @ApiOperation(value="监测站点表-添加", notes="监测站点表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SiteMonitorPoint siteMonitorPoint) {
        siteMonitorPointService.save(siteMonitorPoint);
        //判断是否存在数采仪
        SiteDataCollection siteDataCollection = siteDataCollectionService.findByMnCode(siteMonitorPoint.getMn());
        //没有则新增
        if(siteDataCollection==null){
            siteDataCollection = new SiteDataCollection();
            siteDataCollection.setMn(siteMonitorPoint.getMn());
            siteDataCollection.setMonitorId(siteMonitorPoint.getId());
            siteDataCollectionService.save(siteDataCollection);
        }else{
            //有则更新
            //更新数采仪中的站点id
            siteDataCollection.setMonitorId(siteMonitorPoint.getId());
            siteDataCollectionService.updateById(siteDataCollection);
        }
        return Result.ok("添加成功！");
    }

}
