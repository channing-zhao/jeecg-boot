package org.jeecg.modules.nuoze.nz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.nuoze.nz.entity.NzSourceTrace;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 原材料溯源
 * @Author: jeecg-boot
 * @Date:   2020-12-02
 * @Version: V1.0
 */
public interface INzSourceTraceService extends IService<NzSourceTrace> {
    public IPage<NzSourceTrace> SelectBycode(Page<NzSourceTrace> page, String batchid);
    public IPage<NzSourceTrace> SelectByPrdcode(Page<NzSourceTrace> page, String batchid);
}
