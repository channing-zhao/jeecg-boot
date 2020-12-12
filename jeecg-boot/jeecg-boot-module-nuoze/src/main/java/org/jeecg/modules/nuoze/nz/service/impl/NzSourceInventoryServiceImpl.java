package org.jeecg.modules.nuoze.nz.service.impl;

import org.jeecg.modules.nuoze.nz.entity.NzSourceInventory;
import org.jeecg.modules.nuoze.nz.mapper.NzSourceInventoryMapper;
import org.jeecg.modules.nuoze.nz.service.INzSourceInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 原药材库存
 * @Author: jeecg-boot
 * @Date:   2020-12-02
 * @Version: V1.0
 */
@Service
public class NzSourceInventoryServiceImpl extends ServiceImpl<NzSourceInventoryMapper, NzSourceInventory> implements INzSourceInventoryService {

    public void updateAmount(NzSourceInventory inventory){
        baseMapper.updateAmount(inventory);
    }
}
