package org.jeecg.modules.nuoze.nz.service.impl;

import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.nuoze.nz.entity.NzProductType;
import org.jeecg.modules.nuoze.nz.mapper.NzProductTypeMapper;
import org.jeecg.modules.nuoze.nz.service.INzProductTypeService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 产品类型
 * @Author: jeecg-boot
 * @Date:   2020-12-02
 * @Version: V1.0
 */
@Service
public class NzProductTypeServiceImpl extends ServiceImpl<NzProductTypeMapper, NzProductType> implements INzProductTypeService {

	@Override
	public void addNzProductType(NzProductType nzProductType) {
		if(oConvertUtils.isEmpty(nzProductType.getPid())){
			nzProductType.setPid(INzProductTypeService.ROOT_PID_VALUE);
		}else{
			//如果当前节点父ID不为空 则设置父节点的hasChildren 为1
			NzProductType parent = baseMapper.selectById(nzProductType.getPid());
			if(parent!=null && !"1".equals(parent.getHasChild())){
				parent.setHasChild("1");
				baseMapper.updateById(parent);
			}
		}
		baseMapper.insert(nzProductType);
	}
	
	@Override
	public void updateNzProductType(NzProductType nzProductType) {
		NzProductType entity = this.getById(nzProductType.getId());
		if(entity==null) {
			throw new JeecgBootException("未找到对应实体");
		}
		String old_pid = entity.getPid();
		String new_pid = nzProductType.getPid();
		if(!old_pid.equals(new_pid)) {
			updateOldParentNode(old_pid);
			if(oConvertUtils.isEmpty(new_pid)){
				nzProductType.setPid(INzProductTypeService.ROOT_PID_VALUE);
			}
			if(!INzProductTypeService.ROOT_PID_VALUE.equals(nzProductType.getPid())) {
				baseMapper.updateTreeNodeStatus(nzProductType.getPid(), INzProductTypeService.HASCHILD);
			}
		}
		baseMapper.updateById(nzProductType);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteNzProductType(String id) throws JeecgBootException {
		//查询选中节点下所有子节点一并删除
        id = this.queryTreeChildIds(id);
        if(id.indexOf(",")>0) {
            StringBuffer sb = new StringBuffer();
            String[] idArr = id.split(",");
            for (String idVal : idArr) {
                if(idVal != null){
                    NzProductType nzProductType = this.getById(idVal);
                    String pidVal = nzProductType.getPid();
                    //查询此节点上一级是否还有其他子节点
                    List<NzProductType> dataList = baseMapper.selectList(new QueryWrapper<NzProductType>().eq("pid", pidVal).notIn("id",Arrays.asList(idArr)));
                    if((dataList == null || dataList.size()==0) && !Arrays.asList(idArr).contains(pidVal)
                            && !sb.toString().contains(pidVal)){
                        //如果当前节点原本有子节点 现在木有了，更新状态
                        sb.append(pidVal).append(",");
                    }
                }
            }
            //批量删除节点
            baseMapper.deleteBatchIds(Arrays.asList(idArr));
            //修改已无子节点的标识
            String[] pidArr = sb.toString().split(",");
            for(String pid : pidArr){
                this.updateOldParentNode(pid);
            }
        }else{
            NzProductType nzProductType = this.getById(id);
            if(nzProductType==null) {
                throw new JeecgBootException("未找到对应实体");
            }
            updateOldParentNode(nzProductType.getPid());
            baseMapper.deleteById(id);
        }
	}
	
	@Override
    public List<NzProductType> queryTreeListNoPage(QueryWrapper<NzProductType> queryWrapper) {
        List<NzProductType> dataList = baseMapper.selectList(queryWrapper);
        List<NzProductType> mapList = new ArrayList<>();
        for(NzProductType data : dataList){
            String pidVal = data.getPid();
            //递归查询子节点的根节点
            if(pidVal != null && !"0".equals(pidVal)){
                NzProductType rootVal = this.getTreeRoot(pidVal);
                if(rootVal != null && !mapList.contains(rootVal)){
                    mapList.add(rootVal);
                }
            }else{
                if(!mapList.contains(data)){
                    mapList.add(data);
                }
            }
        }
        return mapList;
    }
	
	/**
	 * 根据所传pid查询旧的父级节点的子节点并修改相应状态值
	 * @param pid
	 */
	private void updateOldParentNode(String pid) {
		if(!INzProductTypeService.ROOT_PID_VALUE.equals(pid)) {
			Integer count = baseMapper.selectCount(new QueryWrapper<NzProductType>().eq("pid", pid));
			if(count==null || count<=1) {
				baseMapper.updateTreeNodeStatus(pid, INzProductTypeService.NOCHILD);
			}
		}
	}

	/**
     * 递归查询节点的根节点
     * @param pidVal
     * @return
     */
    private NzProductType getTreeRoot(String pidVal){
        NzProductType data =  baseMapper.selectById(pidVal);
        if(data != null && !"0".equals(data.getPid())){
            return this.getTreeRoot(data.getPid());
        }else{
            return data;
        }
    }

    /**
     * 根据id查询所有子节点id
     * @param ids
     * @return
     */
    private String queryTreeChildIds(String ids) {
        //获取id数组
        String[] idArr = ids.split(",");
        StringBuffer sb = new StringBuffer();
        for (String pidVal : idArr) {
            if(pidVal != null){
                if(!sb.toString().contains(pidVal)){
                    if(sb.toString().length() > 0){
                        sb.append(",");
                    }
                    sb.append(pidVal);
                    this.getTreeChildIds(pidVal,sb);
                }
            }
        }
        return sb.toString();
    }

    /**
     * 递归查询所有子节点
     * @param pidVal
     * @param sb
     * @return
     */
    private StringBuffer getTreeChildIds(String pidVal,StringBuffer sb){
        List<NzProductType> dataList = baseMapper.selectList(new QueryWrapper<NzProductType>().eq("pid", pidVal));
        if(dataList != null && dataList.size()>0){
            for(NzProductType tree : dataList) {
                if(!sb.toString().contains(tree.getId())){
                    sb.append(",").append(tree.getId());
                }
                this.getTreeChildIds(tree.getId(),sb);
            }
        }
        return sb;
    }

}
