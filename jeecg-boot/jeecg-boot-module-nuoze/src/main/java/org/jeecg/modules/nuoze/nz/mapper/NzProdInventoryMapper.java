package org.jeecg.modules.nuoze.nz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.nuoze.nz.entity.NzProdInventory;

/**
 * @Description: 产品库存
 * @Author: jeecg-boot
 * @Date:   2020-12-02
 * @Version: V1.0
 */
public interface NzProdInventoryMapper extends BaseMapper<NzProdInventory> {
    public void updateAmount(NzProdInventory inventory);
}
