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
import org.jeecg.modules.nuoze.nz.entity.NzSourceType;
import org.jeecg.modules.nuoze.nz.service.INzSourceTypeService;

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
 * @Description: 原药材种类
 * @Author: jeecg-boot
 * @Date:   2020-12-01
 * @Version: V1.0
 */
@Api(tags="原药材种类")
@RestController
@RequestMapping("/nz/nzSourceType")
@Slf4j
public class NzSourceTypeController extends JeecgController<NzSourceType, INzSourceTypeService>{
	@Autowired
	private INzSourceTypeService nzSourceTypeService;
	
	/**
	 * 分页列表查询
	 *
	 * @param nzSourceType
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "原药材种类-分页列表查询")
	@ApiOperation(value="原药材种类-分页列表查询", notes="原药材种类-分页列表查询")
	@GetMapping(value = "/rootList")
	public Result<?> queryPageList(NzSourceType nzSourceType,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		String hasQuery = req.getParameter("hasQuery");
        if(hasQuery != null && "true".equals(hasQuery)){
            QueryWrapper<NzSourceType> queryWrapper =  QueryGenerator.initQueryWrapper(nzSourceType, req.getParameterMap());
            List<NzSourceType> list = nzSourceTypeService.queryTreeListNoPage(queryWrapper);
            IPage<NzSourceType> pageList = new Page<>(1, 10, list.size());
            pageList.setRecords(list);
            return Result.OK(pageList);
        }else{
            String parentId = nzSourceType.getPid();
            if (oConvertUtils.isEmpty(parentId)) {
                parentId = "0";
            }
            nzSourceType.setPid(null);
            QueryWrapper<NzSourceType> queryWrapper = QueryGenerator.initQueryWrapper(nzSourceType, req.getParameterMap());
            // 使用 eq 防止模糊查询
            queryWrapper.eq("pid", parentId);
            Page<NzSourceType> page = new Page<NzSourceType>(pageNo, pageSize);
            IPage<NzSourceType> pageList = nzSourceTypeService.page(page, queryWrapper);
            return Result.OK(pageList);
        }
	}

	 /**
      * 获取子数据
      * @param nzSourceType
      * @param req
      * @return
      */
	@AutoLog(value = "原药材种类-获取子数据")
	@ApiOperation(value="原药材种类-获取子数据", notes="原药材种类-获取子数据")
	@GetMapping(value = "/childList")
	public Result<?> queryPageList(NzSourceType nzSourceType,HttpServletRequest req) {
		QueryWrapper<NzSourceType> queryWrapper = QueryGenerator.initQueryWrapper(nzSourceType, req.getParameterMap());
		List<NzSourceType> list = nzSourceTypeService.list(queryWrapper);
		IPage<NzSourceType> pageList = new Page<>(1, 10, list.size());
        pageList.setRecords(list);
		return Result.OK(pageList);
	}

    /**
      * 批量查询子节点
      * @param parentIds 父ID（多个采用半角逗号分割）
      * @return 返回 IPage
      * @param parentIds
      * @return
      */
	@AutoLog(value = "原药材种类-批量获取子数据")
    @ApiOperation(value="原药材种类-批量获取子数据", notes="原药材种类-批量获取子数据")
    @GetMapping("/getChildListBatch")
    public Result getChildListBatch(@RequestParam("parentIds") String parentIds) {
        try {
            QueryWrapper<NzSourceType> queryWrapper = new QueryWrapper<>();
            List<String> parentIdList = Arrays.asList(parentIds.split(","));
            queryWrapper.in("pid", parentIdList);
            List<NzSourceType> list = nzSourceTypeService.list(queryWrapper);
            IPage<NzSourceType> pageList = new Page<>(1, 10, list.size());
            pageList.setRecords(list);
            return Result.OK(pageList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error("批量查询子节点失败：" + e.getMessage());
        }
    }
	
	/**
	 *   添加
	 *
	 * @param nzSourceType
	 * @return
	 */
	@AutoLog(value = "原药材种类-添加")
	@ApiOperation(value="原药材种类-添加", notes="原药材种类-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody NzSourceType nzSourceType) {
		nzSourceTypeService.addNzSourceType(nzSourceType);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param nzSourceType
	 * @return
	 */
	@AutoLog(value = "原药材种类-编辑")
	@ApiOperation(value="原药材种类-编辑", notes="原药材种类-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody NzSourceType nzSourceType) {
		nzSourceTypeService.updateNzSourceType(nzSourceType);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "原药材种类-通过id删除")
	@ApiOperation(value="原药材种类-通过id删除", notes="原药材种类-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		nzSourceTypeService.deleteNzSourceType(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "原药材种类-批量删除")
	@ApiOperation(value="原药材种类-批量删除", notes="原药材种类-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.nzSourceTypeService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "原药材种类-通过id查询")
	@ApiOperation(value="原药材种类-通过id查询", notes="原药材种类-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		NzSourceType nzSourceType = nzSourceTypeService.getById(id);
		if(nzSourceType==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(nzSourceType);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param nzSourceType
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, NzSourceType nzSourceType) {
		return super.exportXls(request, nzSourceType, NzSourceType.class, "原药材种类");
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
		return super.importExcel(request, response, NzSourceType.class);
    }

}
