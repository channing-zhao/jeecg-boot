package org.jeecg.modules.nuoze.nz.service;

import org.jeecg.modules.nuoze.nz.entity.NzSourceInventory;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 原药材库存
 * @Author: jeecg-boot
 * @Date:   2020-12-02
 * @Version: V1.0
 */
public interface INzSourceInventoryService extends IService<NzSourceInventory> {
    public void updateAmount(NzSourceInventory inventory);
}
