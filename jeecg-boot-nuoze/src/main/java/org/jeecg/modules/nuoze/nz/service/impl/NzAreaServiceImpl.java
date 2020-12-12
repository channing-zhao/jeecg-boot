package org.jeecg.modules.nuoze.nz.service.impl;

import org.jeecg.modules.nuoze.nz.entity.NzArea;
import org.jeecg.modules.nuoze.nz.mapper.NzAreaMapper;
import org.jeecg.modules.nuoze.nz.service.INzAreaService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 区块
 * @Author: jeecg-boot
 * @Date:   2020-12-01
 * @Version: V1.0
 */
@Service
public class NzAreaServiceImpl extends ServiceImpl<NzAreaMapper, NzArea> implements INzAreaService {
	
	@Autowired
	private NzAreaMapper nzAreaMapper;
	
	@Override
	public List<NzArea> selectByMainId(String mainId) {
		return nzAreaMapper.selectByMainId(mainId);
	}
}
