package org.jeecg.modules.nuoze.nz.service;

import org.jeecg.modules.nuoze.nz.entity.NzProdInventory;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.nuoze.nz.entity.NzSourceInventory;

/**
 * @Description: 产品库存
 * @Author: jeecg-boot
 * @Date:   2020-12-02
 * @Version: V1.0
 */
public interface INzProdInventoryService extends IService<NzProdInventory> {
    public void updateAmount(NzProdInventory inventory);
}
