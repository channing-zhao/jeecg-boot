package org.jeecg.modules.nuoze.nz.service;

import org.jeecg.modules.nuoze.nz.entity.NzArea;
import org.jeecg.modules.nuoze.nz.entity.NzBase;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 基地
 * @Author: jeecg-boot
 * @Date:   2020-12-01
 * @Version: V1.0
 */
public interface INzBaseService extends IService<NzBase> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(NzBase nzBase,List<NzArea> nzAreaList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(NzBase nzBase,List<NzArea> nzAreaList);
	
	/**
	 * 删除一对多
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);
	
}
