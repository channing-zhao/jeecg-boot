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
 * @Description: 产品出入库
 * @Author: jeecg-boot
 * @Date:   2020-12-02
 * @Version: V1.0
 */
@Data
@TableName("nz_prod_warehouse")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="nz_prod_warehouse对象", description="产品出入库")
public class NzProdWarehouse implements Serializable {
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
	/**产品批次*/
	@Excel(name = "产品批次", width = 15, dictTable = "nz_prod_batch", dicText = "name", dicCode = "id")
	@Dict(dictTable = "nz_prod_batch", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "产品批次")
    private java.lang.String productBatchIds;
    /**产品*/
    @Excel(name = "产品", width = 15, dictTable = "nz_product", dicText = "name", dicCode = "id")
    @Dict(dictTable = "nz_product", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "产品")
    private java.lang.String productId;
	/**数量*/
	@Excel(name = "数量", width = 15)
    @ApiModelProperty(value = "数量")
    private java.lang.Double amount;
	/**流水号*/
	@Excel(name = "流水号", width = 15 )
    @ApiModelProperty(value = "流水号")
    private java.lang.String unit;
	/**出库入库标志*/
	@Excel(name = "单位", width = 15)
    @ApiModelProperty(value = "出库入库标志")
    private java.lang.Integer type;

    /**运输方式*/
    @Excel(name = "运输方式", width = 15 )
    @ApiModelProperty(value = "运输方式")
    private java.lang.String transport;
    /**
     * 出入库转为库存
     * @return
     */
    public NzProdInventory wareHouseToInventory(){
        NzProdInventory inventory = new NzProdInventory();
        //产品ID
        inventory.setProductId(this.getProductId());
        inventory.setUpdateBy(this.getCreateBy());
        inventory.setUpdateTime(this.getCreateTime());
        inventory.setAmount(this.getAmount());
        return inventory;
    }
}
