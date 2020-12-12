package org.jeecg.modules.nuoze.nz.mapper;

import java.util.List;
import org.jeecg.modules.nuoze.nz.entity.NzArea;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 区块
 * @Author: jeecg-boot
 * @Date:   2020-12-01
 * @Version: V1.0
 */
public interface NzAreaMapper extends BaseMapper<NzArea> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<NzArea> selectByMainId(@Param("mainId") String mainId);
}
