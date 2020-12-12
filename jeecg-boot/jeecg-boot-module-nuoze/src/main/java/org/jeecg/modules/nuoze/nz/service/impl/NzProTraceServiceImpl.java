package org.jeecg.modules.nuoze.nz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.nuoze.nz.entity.NzProTrace;
import org.jeecg.modules.nuoze.nz.entity.NzProTrace;
import org.jeecg.modules.nuoze.nz.entity.NzProdBatch;
import org.jeecg.modules.nuoze.nz.mapper.NzProTraceMapper;
import org.jeecg.modules.nuoze.nz.service.INzProTraceService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 产品溯源
 * @Author: jeecg-boot
 * @Date:   2020-12-02
 * @Version: V1.0
 */
@Service
public class NzProTraceServiceImpl extends ServiceImpl<NzProTraceMapper, NzProTrace> implements INzProTraceService {
    /**
     * 根据批次号查询溯源信息,按时间顺序排
     */
    public IPage<NzProTrace> SelectBycode(Page<NzProTrace> page, String batchid) {
        QueryWrapper<NzProTrace> wrapper = new QueryWrapper<>();
        wrapper.eq("product_batch_id", batchid);
        wrapper.orderByAsc("create_time");

        return  baseMapper.selectPage(page, wrapper);

    }
}
