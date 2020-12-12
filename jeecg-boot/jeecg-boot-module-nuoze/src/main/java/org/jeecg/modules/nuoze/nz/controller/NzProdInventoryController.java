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
import org.jeecg.modules.nuoze.nz.entity.NzProdInventory;
import org.jeecg.modules.nuoze.nz.service.INzProdInventoryService;

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
 * @Description: 产品库存
 * @Author: jeecg-boot
 * @Date:   2020-12-02
 * @Version: V1.0
 */
@Api(tags="产品库存")
@RestController
@RequestMapping("/nz/nzProdInventory")
@Slf4j
public class NzProdInventoryController extends JeecgController<NzProdInventory, INzProdInventoryService> {
	@Autowired
	private INzProdInventoryService nzProdInventoryService;
	
	/**
	 * 分页列表查询
	 *
	 * @param nzProdInventory
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "产品库存-分页列表查询")
	@ApiOperation(value="产品库存-分页列表查询", notes="产品库存-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(NzProdInventory nzProdInventory,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<NzProdInventory> queryWrapper = QueryGenerator.initQueryWrapper(nzProdInventory, req.getParameterMap());
		Page<NzProdInventory> page = new Page<NzProdInventory>(pageNo, pageSize);
		IPage<NzProdInventory> pageList = nzProdInventoryService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param nzProdInventory
	 * @return
	 */
	@AutoLog(value = "产品库存-添加")
	@ApiOperation(value="产品库存-添加", notes="产品库存-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody NzProdInventory nzProdInventory) {
		nzProdInventoryService.save(nzProdInventory);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param nzProdInventory
	 * @return
	 */
	@AutoLog(value = "产品库存-编辑")
	@ApiOperation(value="产品库存-编辑", notes="产品库存-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody NzProdInventory nzProdInventory) {
		nzProdInventoryService.updateById(nzProdInventory);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "产品库存-通过id删除")
	@ApiOperation(value="产品库存-通过id删除", notes="产品库存-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		nzProdInventoryService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "产品库存-批量删除")
	@ApiOperation(value="产品库存-批量删除", notes="产品库存-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.nzProdInventoryService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "产品库存-通过id查询")
	@ApiOperation(value="产品库存-通过id查询", notes="产品库存-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		NzProdInventory nzProdInventory = nzProdInventoryService.getById(id);
		if(nzProdInventory==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(nzProdInventory);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param nzProdInventory
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, NzProdInventory nzProdInventory) {
        return super.exportXls(request, nzProdInventory, NzProdInventory.class, "产品库存");
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
        return super.importExcel(request, response, NzProdInventory.class);
    }

}
