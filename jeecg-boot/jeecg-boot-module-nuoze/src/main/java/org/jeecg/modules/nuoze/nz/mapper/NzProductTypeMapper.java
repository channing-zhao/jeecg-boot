package org.jeecg.modules.nuoze.nz.mapper;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.nuoze.nz.entity.NzProductType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 产品类型
 * @Author: jeecg-boot
 * @Date:   2020-12-02
 * @Version: V1.0
 */
public interface NzProductTypeMapper extends BaseMapper<NzProductType> {

	/**
	 * 编辑节点状态
	 * @param id
	 * @param status
	 */
	void updateTreeNodeStatus(@Param("id") String id,@Param("status") String status);

}
