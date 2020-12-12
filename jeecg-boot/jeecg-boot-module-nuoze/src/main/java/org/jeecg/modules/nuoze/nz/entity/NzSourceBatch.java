package org.jeecg.modules.nuoze.nz.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 原药材批次
 * @Author: jeecg-boot
 * @Date:   2020-12-02
 * @Version: V1.0
 */
@Data
@TableName("nz_source_batch")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="nz_source_batch对象", description="原药材批次")
public class NzSourceBatch implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**创建人*/
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
	/**批次名称*/
	@Excel(name = "批次名称", width = 15)
    @ApiModelProperty(value = "批次名称")
    private java.lang.String name;
    /**原药材ID*/
    @Excel(name = "原药材ID", width = 15)
    @ApiModelProperty(value = "原药材ID")
    private java.lang.String sourceId;
    /**原药材名称*/
    @Excel(name = "原药材名称", width = 15)
    @ApiModelProperty(value = "原药材名称")
    private java.lang.String sourceName;

	/**说明*/
	@Excel(name = "说明", width = 15)
    @ApiModelProperty(value = "说明")
    private java.lang.String remark;
	/**种植基地*/
	@Excel(name = "种植基地", width = 15, dictTable = "nz_area", dicText = "name", dicCode = "id")
	@Dict(dictTable = "nz_base", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "种植基地")
    private java.lang.String areaIds;
	/**当前环节*/
	@Excel(name = "当前环节", width = 15)
    @ApiModelProperty(value = "当前环节")
    private java.lang.Integer status;
    /**二维码*/
    @Excel(name = "二维码", width = 15)
    @ApiModelProperty(value = "二维码")
    private java.lang.String barPath;
}
