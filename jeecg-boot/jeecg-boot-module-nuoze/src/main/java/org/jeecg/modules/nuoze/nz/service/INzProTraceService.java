package org.jeecg.modules.nuoze.nz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.nuoze.nz.entity.NzProTrace;

/**
 * @Description: 产品溯源
 * @Author: jeecg-boot
 * @Date:   2020-12-02
 * @Version: V1.0
 */
public interface INzProTraceService extends IService<NzProTrace> {
    public IPage<NzProTrace> SelectBycode(Page<NzProTrace> page, String batchid);
}
