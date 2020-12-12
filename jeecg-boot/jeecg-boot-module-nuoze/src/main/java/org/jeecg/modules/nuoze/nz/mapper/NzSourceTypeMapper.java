package org.jeecg.modules.nuoze.nz.mapper;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.nuoze.nz.entity.NzSourceType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 原药材种类
 * @Author: jeecg-boot
 * @Date:   2020-12-01
 * @Version: V1.0
 */
public interface NzSourceTypeMapper extends BaseMapper<NzSourceType> {

	/**
	 * 编辑节点状态
	 * @param id
	 * @param status
	 */
	void updateTreeNodeStatus(@Param("id") String id,@Param("status") String status);

}
