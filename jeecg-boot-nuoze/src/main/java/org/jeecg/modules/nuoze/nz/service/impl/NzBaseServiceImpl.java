package org.jeecg.modules.nuoze.nz.service.impl;

import org.jeecg.modules.nuoze.nz.entity.NzBase;
import org.jeecg.modules.nuoze.nz.entity.NzArea;
import org.jeecg.modules.nuoze.nz.mapper.NzAreaMapper;
import org.jeecg.modules.nuoze.nz.mapper.NzBaseMapper;
import org.jeecg.modules.nuoze.nz.service.INzBaseService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 基地
 * @Author: jeecg-boot
 * @Date:   2020-12-01
 * @Version: V1.0
 */
@Service
public class NzBaseServiceImpl extends ServiceImpl<NzBaseMapper, NzBase> implements INzBaseService {

	@Autowired
	private NzBaseMapper nzBaseMapper;
	@Autowired
	private NzAreaMapper nzAreaMapper;
	
	@Override
	@Transactional
	public void saveMain(NzBase nzBase, List<NzArea> nzAreaList) {
		nzBaseMapper.insert(nzBase);
		if(nzAreaList!=null && nzAreaList.size()>0) {
			for(NzArea entity:nzAreaList) {
				//外键设置
				entity.setBaseId(nzBase.getId());
				nzAreaMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(NzBase nzBase,List<NzArea> nzAreaList) {
		nzBaseMapper.updateById(nzBase);
		
		//1.先删除子表数据
		nzAreaMapper.deleteByMainId(nzBase.getId());
		
		//2.子表数据重新插入
		if(nzAreaList!=null && nzAreaList.size()>0) {
			for(NzArea entity:nzAreaList) {
				//外键设置
				entity.setBaseId(nzBase.getId());
				nzAreaMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		nzAreaMapper.deleteByMainId(id);
		nzBaseMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			nzAreaMapper.deleteByMainId(id.toString());
			nzBaseMapper.deleteById(id);
		}
	}
	
}
