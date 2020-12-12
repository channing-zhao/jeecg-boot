package org.jeecg.modules.nuoze.nz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.nuoze.nz.entity.NzSourceInventory;
import org.jeecg.modules.nuoze.nz.entity.NzSourceTrace;
import org.jeecg.modules.nuoze.nz.mapper.NzSourceTraceMapper;
import org.jeecg.modules.nuoze.nz.service.INzSourceTraceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 原材料溯源
 * @Author: jeecg-boot
 * @Date:   2020-12-02
 * @Version: V1.0
 */
@Service
public class NzSourceTraceServiceImpl extends ServiceImpl<NzSourceTraceMapper, NzSourceTrace> implements INzSourceTraceService {


    /**
     * 根据批次号查询溯源信息,按时间顺序排
     */
    public IPage<NzSourceTrace> SelectBycode(Page<NzSourceTrace> page, String batchid) {
        QueryWrapper<NzSourceTrace> wrapper = new QueryWrapper<>();
        wrapper.eq("source_batch_ids", batchid);
        wrapper.orderByAsc("create_time");

        return  baseMapper.selectPage(page, wrapper);

    }

}
