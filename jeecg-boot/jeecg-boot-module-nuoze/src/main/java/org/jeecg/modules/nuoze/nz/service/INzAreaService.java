package org.jeecg.modules.nuoze.nz.service;

import org.jeecg.modules.nuoze.nz.entity.NzArea;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 区块
 * @Author: jeecg-boot
 * @Date:   2020-12-01
 * @Version: V1.0
 */
public interface INzAreaService extends IService<NzArea> {

	public List<NzArea> selectByMainId(String mainId);
}
