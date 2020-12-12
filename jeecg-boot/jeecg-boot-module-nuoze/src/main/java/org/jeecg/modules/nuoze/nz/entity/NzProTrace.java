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
 * @Description: 产品溯源
 * @Author: jeecg-boot
 * @Date:   2020-12-02
 * @Version: V1.0
 */
@Data
@TableName("nz_pro_trace")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="nz_pro_trace对象", description="产品溯源")
public class NzProTrace implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**负责人*/
	@Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    @ApiModelProperty(value = "负责人")
    private java.lang.String createBy;
	/**操作日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "操作日期")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**产品批次*/
	@Excel(name = "产品批次", width = 15,dictTable = "nz_prod_batch", dicText = "name", dicCode = "id")
    @Dict(dictTable = "nz_prod_batch", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "产品批次")
    private java.lang.String productBatchId;
	/**产品*/
	@Excel(name = "产品", width = 15, dictTable = "nz_product", dicText = "name", dicCode = "id")
	@Dict(dictTable = "nz_product", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "产品")
    private java.lang.String productId;
	/**当前环节*/
	@Excel(name = "当前环节", width = 15, dicCode = "cplsygc")
	@Dict(dicCode = "cplsygc")
    @ApiModelProperty(value = "当前环节")
    private java.lang.String node;
	/**图片*/
	@Excel(name = "图片", width = 15)
    @ApiModelProperty(value = "图片")
    private java.lang.String pic;
    /**批次流水号*/
    @Excel(name = "批次流水号", width = 15)
    @ApiModelProperty(value = "批次流水号")
    private java.lang.String optnum;
}
