package org.jeecg.modules.nuoze.nz.mapper;


import org.jeecg.modules.nuoze.nz.entity.NzSourceInventory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 原药材库存
 * @Author: jeecg-boot
 * @Date:   2020-12-02
 * @Version: V1.0
 */
public interface NzSourceInventoryMapper extends BaseMapper<NzSourceInventory> {

    public void updateAmount(NzSourceInventory inventory);
}
