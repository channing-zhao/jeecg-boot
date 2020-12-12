package org.jeecg.modules.nuoze.nz.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.nuoze.nz.entity.NzSourceInventory;
import org.jeecg.modules.nuoze.nz.service.INzSourceInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

 /**
 * @Description: 原药材库存
 * @Author: jeecg-boot
 * @Date:   2020-12-02
 * @Version: V1.0
 */
@Api(tags="原药材库存")
@RestController
@RequestMapping("/nz/nzSourceInventory")
@Slf4j
public class NzSourceInventoryController extends JeecgController<NzSourceInventory, INzSourceInventoryService> {
	@Autowired
	private INzSourceInventoryService nzSourceInventoryService;

	/**
	 * 分页列表查询
	 *
	 * @param nzSourceInventory
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "原药材库存-分页列表查询")
	@ApiOperation(value="原药材库存-分页列表查询", notes="原药材库存-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(NzSourceInventory nzSourceInventory,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<NzSourceInventory> queryWrapper = QueryGenerator.initQueryWrapper(nzSourceInventory, req.getParameterMap());
		Page<NzSourceInventory> page = new Page<NzSourceInventory>(pageNo, pageSize);
		IPage<NzSourceInventory> pageList = nzSourceInventoryService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param nzSourceInventory
	 * @return
	 */
	@AutoLog(value = "原药材库存-添加")
	@ApiOperation(value="原药材库存-添加", notes="原药材库存-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody NzSourceInventory nzSourceInventory) {
		nzSourceInventoryService.save(nzSourceInventory);

		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param nzSourceInventory
	 * @return
	 */
	@AutoLog(value = "原药材库存-编辑")
	@ApiOperation(value="原药材库存-编辑", notes="原药材库存-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody NzSourceInventory nzSourceInventory) {
		nzSourceInventoryService.updateById(nzSourceInventory);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "原药材库存-通过id删除")
	@ApiOperation(value="原药材库存-通过id删除", notes="原药材库存-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		nzSourceInventoryService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "原药材库存-批量删除")
	@ApiOperation(value="原药材库存-批量删除", notes="原药材库存-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.nzSourceInventoryService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "原药材库存-通过id查询")
	@ApiOperation(value="原药材库存-通过id查询", notes="原药材库存-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		NzSourceInventory nzSourceInventory = nzSourceInventoryService.getById(id);
		if(nzSourceInventory==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(nzSourceInventory);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param nzSourceInventory
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, NzSourceInventory nzSourceInventory) {
        return super.exportXls(request, nzSourceInventory, NzSourceInventory.class, "原药材库存");
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
        return super.importExcel(request, response, NzSourceInventory.class);
    }

}
