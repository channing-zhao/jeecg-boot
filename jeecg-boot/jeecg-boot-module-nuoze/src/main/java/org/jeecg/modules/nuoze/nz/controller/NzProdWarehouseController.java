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
import org.jeecg.modules.nuoze.nz.entity.NzProdWarehouse;
import org.jeecg.modules.nuoze.nz.entity.NzProdInventory;
import org.jeecg.modules.nuoze.nz.service.INzProdInventoryService;
import org.jeecg.modules.nuoze.nz.service.INzProdWarehouseService;

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
 * @Description: 产品出入库
 * @Author: jeecg-boot
 * @Date:   2020-12-02
 * @Version: V1.0
 */
@Api(tags="产品出入库")
@RestController
@RequestMapping("/nz/nzProdWarehouse")
@Slf4j
public class NzProdWarehouseController extends JeecgController<NzProdWarehouse, INzProdWarehouseService> {
	@Autowired
	private INzProdWarehouseService nzProdWarehouseService;
	@Autowired
	private INzProdInventoryService nzProdInventoryService;
	
	/**
	 * 分页列表查询
	 *
	 * @param nzProdWarehouse
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "产品出入库-分页列表查询")
	@ApiOperation(value="产品出入库-分页列表查询", notes="产品出入库-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(NzProdWarehouse nzProdWarehouse,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<NzProdWarehouse> queryWrapper = QueryGenerator.initQueryWrapper(nzProdWarehouse, req.getParameterMap());
		Page<NzProdWarehouse> page = new Page<NzProdWarehouse>(pageNo, pageSize);
		IPage<NzProdWarehouse> pageList = nzProdWarehouseService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param nzProdWarehouse
	 * @return
	 */
	@AutoLog(value = "产品出入库-添加")
	@ApiOperation(value="产品出入库-添加", notes="产品出入库-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody NzProdWarehouse nzProdWarehouse) {

		//异步处理库存信息
		NzProdInventory inventory = nzProdWarehouse.wareHouseToInventory();
		//库存有无此原药材库存信息
		QueryWrapper<NzProdInventory> wrapper = new QueryWrapper<>();
		wrapper.eq("product_id",nzProdWarehouse.getProductId());
		List<NzProdInventory> list = nzProdInventoryService.list(wrapper);
		//有此库存
		if(list != null && list.size() > 0){
			NzProdInventory inventory1 = list.get(0);

			//0为出库,减少库存
			if(0 == nzProdWarehouse.getType()) {
				//出库数量大于库存
				if(inventory1.getAmount() < nzProdWarehouse.getAmount()){
					return Result.error("库存不足！");
				}
				inventory.setAmount(-inventory.getAmount());
			}
			nzProdInventoryService.updateAmount(inventory);
			//无库存,且为入库
		}else if(1 == nzProdWarehouse.getType()){
			nzProdInventoryService.save(inventory);
			//无库存时强行出库提示错误
		}else {
			return Result.error("库存不足！");
		}
		nzProdWarehouseService.save(nzProdWarehouse);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param nzProdWarehouse
	 * @return
	 */
	@AutoLog(value = "产品出入库-编辑")
	@ApiOperation(value="产品出入库-编辑", notes="产品出入库-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody NzProdWarehouse nzProdWarehouse) {
		nzProdWarehouseService.updateById(nzProdWarehouse);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "产品出入库-通过id删除")
	@ApiOperation(value="产品出入库-通过id删除", notes="产品出入库-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		nzProdWarehouseService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "产品出入库-批量删除")
	@ApiOperation(value="产品出入库-批量删除", notes="产品出入库-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.nzProdWarehouseService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "产品出入库-通过id查询")
	@ApiOperation(value="产品出入库-通过id查询", notes="产品出入库-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		NzProdWarehouse nzProdWarehouse = nzProdWarehouseService.getById(id);
		if(nzProdWarehouse==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(nzProdWarehouse);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param nzProdWarehouse
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, NzProdWarehouse nzProdWarehouse) {
        return super.exportXls(request, nzProdWarehouse, NzProdWarehouse.class, "产品出入库");
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
        return super.importExcel(request, response, NzProdWarehouse.class);
    }

}
