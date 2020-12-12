package org.jeecg.modules.nuoze.nz.vo;

import java.util.List;
import org.jeecg.modules.nuoze.nz.entity.NzBase;
import org.jeecg.modules.nuoze.nz.entity.NzArea;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelEntity;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 基地
 * @Author: jeecg-boot
 * @Date:   2020-12-01
 * @Version: V1.0
 */
@Data
@ApiModel(value="nz_basePage对象", description="基地")
public class NzBasePage {

	/**主键*/
	@ApiModelProperty(value = "主键")
	private java.lang.String id;
	/**创建人*/
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
	@ApiModelProperty(value = "创建人")
	private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "创建日期")
	private java.util.Date createTime;
	/**更新人*/
	@ApiModelProperty(value = "更新人")
	private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "更新日期")
	private java.util.Date updateTime;
	/**所属部门*/
	@ApiModelProperty(value = "所属部门")
	private java.lang.String sysOrgCode;
	/**基地名*/
	@Excel(name = "基地名", width = 15)
	@ApiModelProperty(value = "基地名")
	private java.lang.String name;
	/**负责人*/
	@Excel(name = "负责人", width = 15, dictTable = "sys_user", dicText = "realname", dicCode = "username")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
	@ApiModelProperty(value = "负责人")
	private java.lang.String head;
	/**电话*/
	@Excel(name = "电话", width = 15)
	@ApiModelProperty(value = "电话")
	private java.lang.String tel;
	/**地址*/
	@Excel(name = "地址", width = 15)
	@ApiModelProperty(value = "地址")
	private java.lang.String address;
	/**面积*/
	@Excel(name = "面积", width = 15)
	@ApiModelProperty(value = "面积")
	private java.lang.Double area;
	/**占比*/
	@Excel(name = "占比", width = 15)
	@ApiModelProperty(value = "占比")
	private java.lang.Double percentage;
	/**描述*/
	@Excel(name = "描述", width = 15)
	@ApiModelProperty(value = "描述")
	private java.lang.String desconte;
	/**图片*/
	@Excel(name = "图片", width = 15)
	@ApiModelProperty(value = "图片")
	private java.lang.String pic;

	@ExcelCollection(name="区块")
	@ApiModelProperty(value = "区块")
	private List<NzArea> nzAreaList;

}
