package org.jeecg.modules.nuoze.nz.service.impl;

import org.jeecg.modules.nuoze.nz.entity.NzProdInventory;
import org.jeecg.modules.nuoze.nz.entity.NzSourceInventory;
import org.jeecg.modules.nuoze.nz.mapper.NzProdInventoryMapper;
import org.jeecg.modules.nuoze.nz.service.INzProdInventoryService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 产品库存
 * @Author: jeecg-boot
 * @Date:   2020-12-02
 * @Version: V1.0
 */
@Service
public class NzProdInventoryServiceImpl extends ServiceImpl<NzProdInventoryMapper, NzProdInventory> implements INzProdInventoryService {
    public void updateAmount(NzProdInventory inventory){
        baseMapper.updateAmount(inventory);
    }
}
