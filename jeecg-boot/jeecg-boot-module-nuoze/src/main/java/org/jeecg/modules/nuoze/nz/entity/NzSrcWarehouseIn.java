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
 * @Description: 原药材出入库记录
 * @Author: jeecg-boot
 * @Date:   2020-12-02
 * @Version: V1.0
 */
@Data
@TableName("nz_src_warehouse_in")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="nz_src_warehouse_in对象", description="原药材出入库记录")
public class NzSrcWarehouseIn implements Serializable {
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
	/**原药材批次*/
	@Excel(name = "原药材批次", width = 15, dictTable = "nz_source_batch", dicText = "name", dicCode = "id")
	@Dict(dictTable = "nz_source_batch", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "原药材批次")
    private java.lang.String sourceBatchIds;
	/**原药材*/
	@Excel(name = "原药材", width = 15, dictTable = "nz_source", dicText = "name", dicCode = "id")
	@Dict(dictTable = "nz_source", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "原药材")
    private java.lang.String sourceIds;
	/**数量*/
	@Excel(name = "数量", width = 15)
    @ApiModelProperty(value = "数量")
    private java.lang.Double amount;
	/**单位*/
	@Excel(name = "单位", width = 15, dicCode = "unit")
	@Dict(dicCode = "unit")
    @ApiModelProperty(value = "单位")
    private java.lang.String unit;
	/**出入*/
	@Excel(name = "出入", width = 15)
    @ApiModelProperty(value = "出入")
    private java.lang.Integer type;

    /**
     * 出入库转为库存
     * @return
     */
	public NzSourceInventory wareHouseToInventory(){
        NzSourceInventory inventory = new NzSourceInventory();
        //原药材ID
        inventory.setSourceId(this.getSourceIds());
        inventory.setUpdateBy(this.getCreateBy());
        inventory.setUpdateTime(this.getCreateTime());
        inventory.setAmount(this.getAmount());
        return inventory;
    }
}
