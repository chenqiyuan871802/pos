package com.ims.system.service;


import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.ims.core.constant.WebplusCons;
import com.ims.core.matatype.Dto;
import com.ims.core.matatype.Dtos;
import com.ims.core.util.WebplusUtil;
import com.ims.core.vo.Query;
import com.ims.core.vo.TreeModel;
import com.ims.system.constant.SystemCons;
import com.ims.system.mapper.DeptMapper;
import com.ims.system.model.Dept;


/**
 * <p>
 * 组织机构 服务实现类
 * </p>
 *
 * @author 陈骑元
 * @since 2018-05-14
 */
@Service
public class DeptService extends ServiceImpl<DeptMapper, Dept>{

	/**
	 * 根据Dto查询并返回数据持久化对象集合
	 * 
	 * @return List<Dept>
	 */
	public List<Dept> list(Dto pDto) {

		return baseMapper.list(pDto);
	};

	/**
	 * 根据Dto查询并返回分页数据持久化对象集合
	 * 
	 * @return Page<Dept>
	 */
	public Page<Dept> listPage(Dto pDto) {
		Query<Dept> query = new Query<Dept>(pDto);
		query.setRecords(baseMapper.listPage(query, pDto));
		return query;
	};

	/**
	 * 根据Dto模糊查询并返回数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<Dept>
	 */
	public List<Dept> like(Dto pDto) {

		return baseMapper.like(pDto);

	};

	/**
	 * 根据Dto模糊查询并返回分页数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return Page<Dept>
	 */
	public Page<Dept> likePage(Dto pDto) {
		Query<Dept> query = new Query<Dept>(pDto);
		query.setRecords(baseMapper.likePage(query, pDto));
		return query;
	}
    /**
     * 
     * 简要说明：加载组织机构树
     * 编写者：陈骑元
     * 创建时间：2019年1月26日 下午11:07:48
     * @param 说明
     * @return 说明
     */
	public List<TreeModel> loadDeptTree(Dto pDto) {
		// 查询 根节点 dept=0;
		Dept rootDept = baseMapper.selectById(SystemCons.TREE_ROOT_ID);
		// 如果数据库没有根节点 则创建一个根节点,并保存到数据库中
		if (WebplusUtil.isEmpty(rootDept)) {
			rootDept = new Dept();
			rootDept.setDeptId(SystemCons.TREE_ROOT_ID);
			rootDept.setParentId("-1");
			rootDept.setDeptName(SystemCons.DEPT_ROOT_NAME);
			rootDept.setIconName(SystemCons.DEPT_ROOT_ICONCLS);
			rootDept.setCascadeId(SystemCons.TREE_ROOT_CASCADE_ID);
			rootDept.setIsDel(WebplusCons.WHETHER_NO);
			rootDept.setIsAutoExpand(WebplusCons.WHETHER_YES);
			rootDept.setSortNo(1);
			rootDept.setRemark("顶级机构不能进行移动和删除操作，只能进行修改");
			rootDept.setCreateTime(WebplusUtil.getDateTime());
			baseMapper.insert(rootDept);

		}
		pDto.put("isDel", WebplusCons.WHETHER_NO); // 查询有效的组织机构信息
		pDto.setOrder(" LENGTH(cascade_id) ASC,sort_no ASC ");
		List<Dept> deptList = baseMapper.list(pDto);
		List<TreeModel> treeModelList=Lists.newArrayList();
		for (int i = 0; i < deptList.size(); i++) {
			Dept dept = deptList.get(i);
			String parentId = dept.getParentId();
			TreeModel treeModel = new TreeModel();
			treeModel.setId(dept.getDeptId());
			treeModel.setpId(parentId);
			treeModel.setCascadeId(dept.getCascadeId());
			treeModel.setName(dept.getDeptName());
			if(WebplusCons.WHETHER_YES.equals(dept.getIsAutoExpand())){
				treeModel.setOpen(true);//展开节点
			}else{
				treeModel.setOpen(false);//展开节点
			}
			treeModel.setChildren(null);
			treeModel.setChecked(false);
			treeModelList.add(treeModel);
		}
		return treeModelList;

	}

	public String calc(Dto pDto) {
		// TODO Auto-generated method stub
		return baseMapper.calc(pDto);
	}

	/**
	 * 更新处理机构信息
	 */
	@Transactional
	public boolean updateDept(Dept dept) {
		String deptId = dept.getDeptId();
		Dept oldDept=this.selectById(deptId);
		// 根节点的父节点是不存在的，因此默认为-1
		if (SystemCons.TREE_ROOT_ID.equals(deptId)) {
			dept.setParentId("-1");
		}
		String parentId = dept.getParentId();
		String parentIdOld = oldDept.getParentId();
		dept.setUpdateTime(WebplusUtil.getDateTime());
		if (!parentId.equals(parentIdOld)) { // 如果机构发生改变了，则更新子节点的语义ID
			// 查询当前父节点下面是否存在最大的语义ID
			Dto calcDto = Dtos.newCalcDto("MAX(cascade_id)");
			calcDto.put("parentId", parentId);
			String maxCascadeId = this.calc(calcDto);
			// 如果当前父节点不存在最大的语义ID，则初始化生成
			if (WebplusUtil.isEmpty(maxCascadeId)) {
				Dept parentDept = this.selectById(parentId);
				if (WebplusUtil.isEmpty(parentDept)) {
					maxCascadeId = "0.0000";
				} else {
					maxCascadeId = parentDept.getCascadeId() + ".0000";
				}
			}
			// 生成新的语义ID
			String cascadeId = WebplusUtil.createCascadeId(maxCascadeId, 9999);
			dept.setCascadeId(cascadeId);
			// 原始的语义ID
			String cascadeIdOld =oldDept.getCascadeId();
			Dto pDto = Dtos.newDto("cascadeId", cascadeIdOld);
			// 查询所有子节点进行更新
			List<Dept> childDeptList = this.like(pDto);
			for (Dept childDept : childDeptList) {
				String cascade_id_temp = childDept.getCascadeId();
				cascade_id_temp = cascade_id_temp.replace(cascadeIdOld, cascadeId);
				childDept.setCascadeId(cascade_id_temp);
				childDept.setUpdateTime(WebplusUtil.getDateTime());
				baseMapper.updateById(childDept);

			}
		}
		int row = baseMapper.updateById(dept);
		return row > 0;
	};
}
