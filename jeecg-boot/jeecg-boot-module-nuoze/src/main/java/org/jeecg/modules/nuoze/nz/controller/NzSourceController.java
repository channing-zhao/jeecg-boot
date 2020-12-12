package org.jeecg.modules.nuoze.nz.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.nuoze.nz.entity.NzSource;
import org.jeecg.modules.nuoze.nz.service.INzSourceService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

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
 * @Description: 原材料
 * @Author: jeecg-boot
 * @Date:   2020-12-02
 * @Version: V1.0
 */
@Api(tags="原材料")
@RestController
@RequestMapping("/nz/nzSource")
@Slf4j
public class NzSourceController extends JeecgController<NzSource, INzSourceService> {
	@Autowired
	private INzSourceService nzSourceService;
	
	/**
	 * 分页列表查询
	 *
	 * @param nzSource
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "原材料-分页列表查询")
	@ApiOperation(value="原材料-分页列表查询", notes="原材料-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(NzSource nzSource,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<NzSource> queryWrapper = QueryGenerator.initQueryWrapper(nzSource, req.getParameterMap());
		Page<NzSource> page = new Page<NzSource>(pageNo, pageSize);
		IPage<NzSource> pageList = nzSourceService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param nzSource
	 * @return
	 */
	@AutoLog(value = "原材料-添加")
	@ApiOperation(value="原材料-添加", notes="原材料-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody NzSource nzSource) {
		nzSourceService.save(nzSource);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param nzSource
	 * @return
	 */
	@AutoLog(value = "原材料-编辑")
	@ApiOperation(value="原材料-编辑", notes="原材料-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody NzSource nzSource) {
		nzSourceService.updateById(nzSource);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "原材料-通过id删除")
	@ApiOperation(value="原材料-通过id删除", notes="原材料-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		nzSourceService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "原材料-批量删除")
	@ApiOperation(value="原材料-批量删除", notes="原材料-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.nzSourceService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "原材料-通过id查询")
	@ApiOperation(value="原材料-通过id查询", notes="原材料-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		NzSource nzSource = nzSourceService.getById(id);
		if(nzSource==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(nzSource);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param nzSource
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, NzSource nzSource) {
        return super.exportXls(request, nzSource, NzSource.class, "原材料");
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
        return super.importExcel(request, response, NzSource.class);
    }

}
