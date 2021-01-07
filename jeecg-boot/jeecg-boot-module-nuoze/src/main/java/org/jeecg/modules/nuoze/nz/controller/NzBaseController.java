package org.jeecg.modules.nuoze.nz.controller;

import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.util.RedisUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.vo.LoginUser;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.nuoze.nz.entity.NzArea;
import org.jeecg.modules.nuoze.nz.entity.NzBase;
import org.jeecg.modules.nuoze.nz.vo.NzBasePage;
import org.jeecg.modules.nuoze.nz.service.INzBaseService;
import org.jeecg.modules.nuoze.nz.service.INzAreaService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 基地
 * @Author: jeecg-boot
 * @Date:   2020-12-01
 * @Version: V1.0
 */
@Api(tags="基地")
@RestController
@RequestMapping("/nz/nzBase")
@Slf4j
public class NzBaseController {
	@Autowired
	private INzBaseService nzBaseService;
	@Autowired
	private INzAreaService nzAreaService;
	 @Autowired
	 private RedisUtil redisUtil;
	
	/**
	 * 分页列表查询
	 *
	 * @param nzBase
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "基地-分页列表查询")
	@ApiOperation(value="基地-分页列表查询", notes="基地-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(NzBase nzBase,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<NzBase> queryWrapper = QueryGenerator.initQueryWrapper(nzBase, req.getParameterMap());
		Page<NzBase> page = new Page<NzBase>(pageNo, pageSize);
		IPage<NzBase> pageList = nzBaseService.page(page, queryWrapper);

		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param nzBasePage
	 * @return
	 */
	@AutoLog(value = "基地-添加")
	@ApiOperation(value="基地-添加", notes="基地-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody NzBasePage nzBasePage) {
		NzBase nzBase = new NzBase();
		BeanUtils.copyProperties(nzBasePage, nzBase);
		nzBaseService.saveMain(nzBase, nzBasePage.getNzAreaList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param nzBasePage
	 * @return
	 */
	@AutoLog(value = "基地-编辑")
	@ApiOperation(value="基地-编辑", notes="基地-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody NzBasePage nzBasePage) {
		NzBase nzBase = new NzBase();
		BeanUtils.copyProperties(nzBasePage, nzBase);
		NzBase nzBaseEntity = nzBaseService.getById(nzBase.getId());
		if(nzBaseEntity==null) {
			return Result.error("未找到对应数据");
		}
		nzBaseService.updateMain(nzBase, nzBasePage.getNzAreaList());

		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "基地-通过id删除")
	@ApiOperation(value="基地-通过id删除", notes="基地-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		nzBaseService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "基地-批量删除")
	@ApiOperation(value="基地-批量删除", notes="基地-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.nzBaseService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "基地-通过id查询")
	@ApiOperation(value="基地-通过id查询", notes="基地-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		NzBase nzBase = nzBaseService.getById(id);
		if(nzBase==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(nzBase);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "区块通过主表ID查询")
	@ApiOperation(value="区块主表ID查询", notes="区块-通主表ID查询")
	@GetMapping(value = "/queryNzAreaByMainId")
	public Result<?> queryNzAreaListByMainId(@RequestParam(name="id",required=true) String id) {
		List<NzArea> nzAreaList = nzAreaService.selectByMainId(id);
		return Result.OK(nzAreaList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param nzBase
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, NzBase nzBase) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<NzBase> queryWrapper = QueryGenerator.initQueryWrapper(nzBase, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<NzBase> queryList = nzBaseService.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
      List<NzBase> nzBaseList = new ArrayList<NzBase>();
      if(oConvertUtils.isEmpty(selections)) {
          nzBaseList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          nzBaseList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<NzBasePage> pageList = new ArrayList<NzBasePage>();
      for (NzBase main : nzBaseList) {
          NzBasePage vo = new NzBasePage();
          BeanUtils.copyProperties(main, vo);
          List<NzArea> nzAreaList = nzAreaService.selectByMainId(main.getId());
          vo.setNzAreaList(nzAreaList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "基地列表");
      mv.addObject(NormalExcelConstants.CLASS, NzBasePage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("基地数据", "导出人:"+sysUser.getRealname(), "基地"));
      mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
      return mv;
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
      MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
      Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
      for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
          MultipartFile file = entity.getValue();// 获取上传文件对象
          ImportParams params = new ImportParams();
          params.setTitleRows(2);
          params.setHeadRows(1);
          params.setNeedSave(true);
          try {
              List<NzBasePage> list = ExcelImportUtil.importExcel(file.getInputStream(), NzBasePage.class, params);
              for (NzBasePage page : list) {
                  NzBase po = new NzBase();
                  BeanUtils.copyProperties(page, po);
                  nzBaseService.saveMain(po, page.getNzAreaList());
              }
              return Result.OK("文件导入成功！数据行数:" + list.size());
          } catch (Exception e) {
              log.error(e.getMessage(),e);
              return Result.error("文件导入失败:"+e.getMessage());
          } finally {
              try {
                  file.getInputStream().close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
      return Result.OK("文件导入失败！");
    }

}
