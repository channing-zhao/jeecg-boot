package org.jeecg.modules.nuoze.nz.service;

import org.jeecg.modules.nuoze.nz.entity.NzSourceType;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.exception.JeecgBootException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.util.List;

/**
 * @Description: 原药材种类
 * @Author: jeecg-boot
 * @Date:   2020-12-01
 * @Version: V1.0
 */
public interface INzSourceTypeService extends IService<NzSourceType> {

	/**根节点父ID的值*/
	public static final String ROOT_PID_VALUE = "0";
	
	/**树节点有子节点状态值*/
	public static final String HASCHILD = "1";
	
	/**树节点无子节点状态值*/
	public static final String NOCHILD = "0";

	/**新增节点*/
	void addNzSourceType(NzSourceType nzSourceType);
	
	/**修改节点*/
	void updateNzSourceType(NzSourceType nzSourceType) throws JeecgBootException;
	
	/**删除节点*/
	void deleteNzSourceType(String id) throws JeecgBootException;

	/**查询所有数据，无分页*/
    List<NzSourceType> queryTreeListNoPage(QueryWrapper<NzSourceType> queryWrapper);

}
