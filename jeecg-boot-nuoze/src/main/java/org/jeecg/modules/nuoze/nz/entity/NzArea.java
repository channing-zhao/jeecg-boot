package org.jeecg.modules.nuoze.nz.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 区块
 * @Author: jeecg-boot
 * @Date:   2020-12-01
 * @Version: V1.0
 */
@ApiModel(value="nz_base对象", description="基地")
@Data
@TableName("nz_area")
public class NzArea implements Serializable {
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
	/**名称*/
	@Excel(name = "名称", width = 15)
	@ApiModelProperty(value = "名称")
	private java.lang.String name;
	/**区块编号*/
	@Excel(name = "区块编号", width = 15)
	@ApiModelProperty(value = "区块编号")
	private java.lang.String code;
	/**区块面积*/
	@Excel(name = "区块面积", width = 15)
	@ApiModelProperty(value = "区块面积")
	private java.lang.Double areaArea;
	/**区块描述*/
	@Excel(name = "区块描述", width = 15)
	@ApiModelProperty(value = "区块描述")
	private java.lang.String areaDesc;
	/**基地ID*/
	@ApiModelProperty(value = "基地ID")
	private java.lang.String baseId;
}
