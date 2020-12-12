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

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.nuoze.nz.entity.NzSourceInventory;
import org.jeecg.modules.nuoze.nz.entity.NzSrcWarehouseIn;
import org.jeecg.modules.nuoze.nz.service.INzSourceInventoryService;
import org.jeecg.modules.nuoze.nz.service.INzSrcWarehouseInService;

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
 * @Description: 原药材出入库记录
 * @Author: jeecg-boot
 * @Date:   2020-12-02
 * @Version: V1.0
 */
@Api(tags="原药材出入库记录")
@RestController
@RequestMapping("/nz/nzSrcWarehouseIn")
@Slf4j
public class NzSrcWarehouseInController extends JeecgController<NzSrcWarehouseIn, INzSrcWarehouseInService> {
	@Autowired
	private INzSrcWarehouseInService nzSrcWarehouseInService;
	 @Autowired
	 private INzSourceInventoryService srcInvuetoryService;
	/**
	 * 分页列表查询
	 *
	 * @param nzSrcWarehouseIn
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "原药材出入库记录-分页列表查询")
	@ApiOperation(value="原药材出入库记录-分页列表查询", notes="原药材出入库记录-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(NzSrcWarehouseIn nzSrcWarehouseIn,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<NzSrcWarehouseIn> queryWrapper = QueryGenerator.initQueryWrapper(nzSrcWarehouseIn, req.getParameterMap());
		Page<NzSrcWarehouseIn> page = new Page<NzSrcWarehouseIn>(pageNo, pageSize);
		IPage<NzSrcWarehouseIn> pageList = nzSrcWarehouseInService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param nzSrcWarehouseIn
	 * @return
	 */
	@AutoLog(value = "原药材出入库记录-添加")
	@ApiOperation(value="原药材出入库记录-添加", notes="原药材出入库记录-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody NzSrcWarehouseIn nzSrcWarehouseIn) {

		//异步处理库存信息
		NzSourceInventory inventory = nzSrcWarehouseIn.wareHouseToInventory();
		//库存有无此原药材库存信息
		QueryWrapper<NzSourceInventory> wrapper = new QueryWrapper<>();
		wrapper.eq("source_id",nzSrcWarehouseIn.getSourceIds());
		List<NzSourceInventory> list = srcInvuetoryService.list(wrapper);
		//有此库存
		if(list != null && list.size() > 0){
			NzSourceInventory inventory1 = list.get(0);

			//0为出库,减少库存
			if(0 == nzSrcWarehouseIn.getType()) {
				//出库数量大于库存
				if(inventory1.getAmount() < nzSrcWarehouseIn.getAmount()){
					return Result.error("库存不足！");
				}
				inventory.setAmount(-inventory.getAmount());
			}
			srcInvuetoryService.updateAmount(inventory);
		//无库存,且为入库
		}else if(1 == nzSrcWarehouseIn.getType()){
			srcInvuetoryService.save(inventory);
		//无库存时强行出库提示错误
		}else {
			return Result.error("库存不足！");
		}
		nzSrcWarehouseInService.save(nzSrcWarehouseIn);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param nzSrcWarehouseIn
	 * @return
	 */
	@AutoLog(value = "原药材出入库记录-编辑")
	@ApiOperation(value="原药材出入库记录-编辑", notes="原药材出入库记录-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody NzSrcWarehouseIn nzSrcWarehouseIn) {
		nzSrcWarehouseInService.updateById(nzSrcWarehouseIn);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "原药材出入库记录-通过id删除")
	@ApiOperation(value="原药材出入库记录-通过id删除", notes="原药材出入库记录-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		nzSrcWarehouseInService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "原药材出入库记录-批量删除")
	@ApiOperation(value="原药材出入库记录-批量删除", notes="原药材出入库记录-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.nzSrcWarehouseInService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "原药材出入库记录-通过id查询")
	@ApiOperation(value="原药材出入库记录-通过id查询", notes="原药材出入库记录-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		NzSrcWarehouseIn nzSrcWarehouseIn = nzSrcWarehouseInService.getById(id);
		if(nzSrcWarehouseIn==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(nzSrcWarehouseIn);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param nzSrcWarehouseIn
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, NzSrcWarehouseIn nzSrcWarehouseIn) {
        return super.exportXls(request, nzSrcWarehouseIn, NzSrcWarehouseIn.class, "原药材出入库记录");
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
        return super.importExcel(request, response, NzSrcWarehouseIn.class);
    }

}
