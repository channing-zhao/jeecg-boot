package org.jeecg.modules.nuoze.nz.controller;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hpbf.model.qcbits.QCBit;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.UUIDGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.nuoze.nz.entity.NzSourceBatch;
import org.jeecg.modules.nuoze.nz.service.INzSourceBatchService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.nuoze.nz.utils.ConstanceBean;
import org.jeecg.modules.nuoze.nz.utils.QBarUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: 原药材批次
 * @Author: jeecg-boot
 * @Date: 2020-12-02
 * @Version: V1.0
 */
@Api(tags = "原药材批次")
@RestController
@RequestMapping("/nz/nzSourceBatch")
@Slf4j
public class NzSourceBatchController extends JeecgController<NzSourceBatch, INzSourceBatchService> {
    @Autowired
    private INzSourceBatchService nzSourceBatchService;
    @Autowired
    ConstanceBean cb;

    /**
     * 分页列表查询
     *
     * @param nzSourceBatch
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "原药材批次-分页列表查询")
    @ApiOperation(value = "原药材批次-分页列表查询", notes = "原药材批次-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(NzSourceBatch nzSourceBatch,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<NzSourceBatch> queryWrapper = QueryGenerator.initQueryWrapper(nzSourceBatch, req.getParameterMap());
        Page<NzSourceBatch> page = new Page<NzSourceBatch>(pageNo, pageSize);
        IPage<NzSourceBatch> pageList = nzSourceBatchService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    @AutoLog(value = "查询批次对应的原药材")
    @ApiOperation(value = "查询批次对应的原药材", notes = "查询批次对应的原药材")
    @GetMapping(value = "/querySourceListByBatchids")
    public Result<?> querySourceListByBatchids(NzSourceBatch nzSourceBatch, HttpServletRequest req) {
        Map<String,String[]> params = req.getParameterMap();
        QueryWrapper<NzSourceBatch> queryWrapper = QueryGenerator.initQueryWrapper(nzSourceBatch, params);
        if(params != null){
        	String ids = params.get("id")[0];
            System.out.println(ids);
            String[] idarray = ids.split(",");
            List<String> idlist = new ArrayList<>();
            for(String id : idarray){
                idlist.add(id);
            }

			queryWrapper.in("id", idlist);
		}

        List<NzSourceBatch> list = nzSourceBatchService.list(queryWrapper);

        Set<String> sourceIds = new HashSet<>();
        if (!list.isEmpty()) {
            for (NzSourceBatch batch: list ) {
				sourceIds.add(batch.getSourceId());
            }
        }
        return Result.OK(sourceIds);
    }

    /**
     * 添加
     *
     * @param nzSourceBatch
     * @return
     */
    @AutoLog(value = "原药材批次-添加")
    @ApiOperation(value = "原药材批次-添加", notes = "原药材批次-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody NzSourceBatch nzSourceBatch) {
        File path = new File(cb.getFilePath());
        if (!path.exists()) {
            path.mkdirs();
        }
        //批次号
        String batchCode = UUIDGenerator.generate();
        nzSourceBatch.setUpdateBy(batchCode);
        //二维码路径
        String barPath = new StringBuffer(cb.getFilePath()).append(File.separator).append("barcode").append(File.separator)
                .append(batchCode).append(".").append(cb.getFormat()).toString();
        //扫码跳转页面URL
        nzSourceBatch.setBarPath("barcode/" + batchCode + "." + cb.getFormat());
        nzSourceBatchService.save(nzSourceBatch);
        String bid = nzSourceBatch.getId();
        QBarUtil.reateQRCode(300, 300, cb.getFormat(), cb.getDomain() + "?type=1&batchid=" + bid, barPath);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param nzSourceBatch
     * @return
     */
    @AutoLog(value = "原药材批次-编辑")
    @ApiOperation(value = "原药材批次-编辑", notes = "原药材批次-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody NzSourceBatch nzSourceBatch) {
        nzSourceBatchService.updateById(nzSourceBatch);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "原药材批次-通过id删除")
    @ApiOperation(value = "原药材批次-通过id删除", notes = "原药材批次-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        nzSourceBatchService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "原药材批次-批量删除")
    @ApiOperation(value = "原药材批次-批量删除", notes = "原药材批次-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.nzSourceBatchService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "原药材批次-通过id查询")
    @ApiOperation(value = "原药材批次-通过id查询", notes = "原药材批次-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        NzSourceBatch nzSourceBatch = nzSourceBatchService.getById(id);
        if (nzSourceBatch == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(nzSourceBatch);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param nzSourceBatch
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, NzSourceBatch nzSourceBatch) {
        return super.exportXls(request, nzSourceBatch, NzSourceBatch.class, "原药材批次");
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
        return super.importExcel(request, response, NzSourceBatch.class);
    }

}
