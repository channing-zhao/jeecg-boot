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
import org.jeecg.modules.nuoze.nz.entity.NzSourceTrace;
import org.jeecg.modules.nuoze.nz.service.INzSourceTraceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

 /**
 * @Description: 原材料溯源
 * @Author: jeecg-boot
 * @Date:   2020-12-02
 * @Version: V1.0
 */
@Api(tags="原材料溯源")
@RestController
@RequestMapping("/nz/nzSourceTrace")
@Slf4j
public class NzSourceTraceController extends JeecgController<NzSourceTrace, INzSourceTraceService> {
	@Autowired
	private INzSourceTraceService nzSourceTraceService;

	/**
	 * 分页列表查询
	 *
	 * @param nzSourceTrace
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "原材料溯源-分页列表查询")
	@ApiOperation(value="原材料溯源-分页列表查询", notes="原材料溯源-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(NzSourceTrace nzSourceTrace,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<NzSourceTrace> queryWrapper = QueryGenerator.initQueryWrapper(nzSourceTrace, req.getParameterMap());
		Page<NzSourceTrace> page = new Page<NzSourceTrace>(pageNo, pageSize);
		IPage<NzSourceTrace> pageList = nzSourceTraceService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param nzSourceTrace
	 * @return
	 */
	@AutoLog(value = "原材料溯源-添加")
	@ApiOperation(value="原材料溯源-添加", notes="原材料溯源-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody NzSourceTrace nzSourceTrace) {
		nzSourceTraceService.save(nzSourceTrace);

		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param nzSourceTrace
	 * @return
	 */
	@AutoLog(value = "原材料溯源-编辑")
	@ApiOperation(value="原材料溯源-编辑", notes="原材料溯源-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody NzSourceTrace nzSourceTrace) {
		nzSourceTraceService.updateById(nzSourceTrace);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "原材料溯源-通过id删除")
	@ApiOperation(value="原材料溯源-通过id删除", notes="原材料溯源-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		nzSourceTraceService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "原材料溯源-批量删除")
	@ApiOperation(value="原材料溯源-批量删除", notes="原材料溯源-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.nzSourceTraceService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "原材料溯源-通过id查询")
	@ApiOperation(value="原材料溯源-通过id查询", notes="原材料溯源-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		NzSourceTrace nzSourceTrace = nzSourceTraceService.getById(id);
		if(nzSourceTrace==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(nzSourceTrace);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param nzSourceTrace
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, NzSourceTrace nzSourceTrace) {
        return super.exportXls(request, nzSourceTrace, NzSourceTrace.class, "原材料溯源");
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
        return super.importExcel(request, response, NzSourceTrace.class);
    }

}
