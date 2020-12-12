package org.jeecg.modules.nuoze.nz.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.nuoze.nz.entity.NzProTrace;
import org.jeecg.modules.nuoze.nz.entity.NzSourceTrace;
import org.jeecg.modules.nuoze.nz.service.INzProTraceService;
import org.jeecg.modules.nuoze.nz.service.INzSourceTraceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Api(tags="前台溯源")
@RestController
@RequestMapping("/trace")
@Slf4j
public class FrontTraceController  {
   @Autowired
   private INzSourceTraceService nzSourceTraceService;
    @Autowired
    private INzProTraceService nzProTraceService;

   /**
    * 原材料溯源查询
    *
    * @param batchid
    * @return
    */
   @AutoLog(value = "原材料溯源-分页列表查询")
   @ApiOperation(value="原材料溯源-分页列表查询", notes="原材料溯源-分页列表查询")
   @GetMapping(value = "/sourceBatch")
   public Result<?> queryPageList(@RequestParam(name="batchid",required=true) String batchid) {
       Page<NzSourceTrace> page = new Page<NzSourceTrace>(0, 20);
       IPage<NzSourceTrace> pageList = nzSourceTraceService.SelectBycode(page, batchid);
       return Result.OK(pageList);
   }
    /**
     * 产品溯源查询
     *
     * @param batchid
     * @return
     */
    @AutoLog(value = "产品溯源-分页列表查询")
    @ApiOperation(value="产品溯源-分页列表查询", notes="产品溯源-分页列表查询")
    @GetMapping(value = "/productBatch")
    public Result<?> queryProdTracePage(@RequestParam(name="batchid",required=true) String batchid) {
        Page<NzProTrace> page = new Page<NzProTrace>(0, 20);
        IPage<NzProTrace> pageList = nzProTraceService.SelectBycode(page, batchid);
        return Result.OK(pageList);
    }
}
