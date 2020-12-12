package org.jeecg.modules.nuoze.nz.controller;

import java.io.File;
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
import org.jeecg.common.util.UUIDGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.nuoze.nz.entity.NzProdBatch;
import org.jeecg.modules.nuoze.nz.service.INzProdBatchService;

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
 * @Description: 产品批次
 * @Author: jeecg-boot
 * @Date:   2020-12-02
 * @Version: V1.0
 */
@Api(tags="产品批次")
@RestController
@RequestMapping("/nz/nzProdBatch")
@Slf4j
public class NzProdBatchController extends JeecgController<NzProdBatch, INzProdBatchService> {
	@Autowired
	private INzProdBatchService nzProdBatchService;
	 @Autowired
	 ConstanceBean cb;
	/**
	 * 分页列表查询
	 *
	 * @param nzProdBatch
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "产品批次-分页列表查询")
	@ApiOperation(value="产品批次-分页列表查询", notes="产品批次-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(NzProdBatch nzProdBatch,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<NzProdBatch> queryWrapper = QueryGenerator.initQueryWrapper(nzProdBatch, req.getParameterMap());
		Page<NzProdBatch> page = new Page<NzProdBatch>(pageNo, pageSize);
		IPage<NzProdBatch> pageList = nzProdBatchService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param nzProdBatch
	 * @return
	 */
	@AutoLog(value = "产品批次-添加")
	@ApiOperation(value="产品批次-添加", notes="产品批次-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody NzProdBatch nzProdBatch) {
		StringBuffer sb = new StringBuffer(cb.getFilePath()).append(File.separator).append("barcode");
		File path = new File(sb.toString());
		if (!path.exists()) {
			path.mkdirs();
		}
		//批次号
		String batchCode = UUIDGenerator.generate();
		nzProdBatch.setUpdateBy(batchCode);
		//二维码路径
		String barPath = sb.append(File.separator)
				.append(batchCode).append(".").append(cb.getFormat()).toString();

		nzProdBatch.setBarPath("barcode/" + batchCode + "." + cb.getFormat());
		nzProdBatchService.save(nzProdBatch);
		String bid = nzProdBatch.getId();
		QBarUtil.reateQRCode(300, 300, cb.getFormat(), cb.getDomain() + "?type=2&batchid=" + bid, barPath);

		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param nzProdBatch
	 * @return
	 */
	@AutoLog(value = "产品批次-编辑")
	@ApiOperation(value="产品批次-编辑", notes="产品批次-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody NzProdBatch nzProdBatch) {
		nzProdBatchService.updateById(nzProdBatch);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "产品批次-通过id删除")
	@ApiOperation(value="产品批次-通过id删除", notes="产品批次-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		nzProdBatchService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "产品批次-批量删除")
	@ApiOperation(value="产品批次-批量删除", notes="产品批次-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.nzProdBatchService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "产品批次-通过id查询")
	@ApiOperation(value="产品批次-通过id查询", notes="产品批次-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		NzProdBatch nzProdBatch = nzProdBatchService.getById(id);
		if(nzProdBatch==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(nzProdBatch);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param nzProdBatch
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, NzProdBatch nzProdBatch) {
        return super.exportXls(request, nzProdBatch, NzProdBatch.class, "产品批次");
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
        return super.importExcel(request, response, NzProdBatch.class);
    }

}
