package org.jeecg.modules.nuoze.nz.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.nuoze.nz.entity.NzSourceTrace;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 原材料溯源
 * @Author: jeecg-boot
 * @Date:   2020-12-02
 * @Version: V1.0
 */
public interface NzSourceTraceMapper extends BaseMapper<NzSourceTrace> {

    List<NzSourceTrace> SelectByPrdcode(@Param("batchid") String batchid);
}
