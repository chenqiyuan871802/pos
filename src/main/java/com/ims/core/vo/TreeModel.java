package com.ims.core.vo;

import java.util.List;

import com.google.common.collect.Lists;




/**
 * 
 * 类名:com.toonan.common.vo.Z_TreeModel
 * 描述:Z_Tree节点模型类
 * 创建时间:2018年12月20日 下午4:51:06
 * 修改说明:
 */
public class TreeModel {
	/**
	 * 
	 */
	public static final String TREE_ROOT_ID="0";
	/*
	 * 树的节点ID
	 */
	private String id;
	/*
	 * 树节点名称
	 */
	private String name;
	/*
	 * 树的父节点ID
	 */
	private String pId;
	/*
	 * 是否展开（true:展开、false:不展开）
	 */
	private boolean open;
	/*
	 * 节点图片：("/img/open.gif")
	 */
	private String  icon;
	/*
	 * 节点展开时的图片("/img/open.gif")
	 */
	private String iconOpen;
	/*
	 * 节点关闭时的图片(/img/close.gif)
	 */
	private String iconClose;	 
	/*
	 * 树节点点击 请求的url
	 */
	private String url;
	/*
	 * 是否是父节点
	 */
	private boolean isParent;
	/*
	 * 是否选中（true:选中、false:不选中）
	 */
	private boolean checked;
	
	/*
	 * 子节点
	 */
	private List<TreeModel> children=Lists.newArrayList();
	/**
	 * 递归语义编号
	 */
	private String cascadeId;
	
	
	/**
	 * 
	 * 简要说明：
	 * 编写者：陈骑元
	 * 创建时间：2019年1月7日 下午8:31:52
	 * @param 说明
	 * @return 说明
	 */
	public void add(TreeModel treeModel){
    	if(TREE_ROOT_ID.equals(treeModel.getpId())){
    		children.add( treeModel);
    	}else if(id.equals(treeModel.getpId())){
    		children.add(treeModel);
    	}else{
    		for(TreeModel tmp_treeModel:children){
    			tmp_treeModel.add( treeModel);
    		}
    	}
    }
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getpId() {
		return pId;
	}
	public void setpId(String pId) {
		this.pId = pId;
	}
	public boolean isOpen() {
		return open;
	}
	public void setOpen(boolean open) {
		this.open = open;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public boolean isParent() {
		return isParent;
	}
	public void setParent(boolean isParent) {
		this.isParent = isParent;
	}
	public List<TreeModel> getChildren() {
		return children;
	}
	public void setChildren(List<TreeModel> children) {
		this.children = children;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getIconOpen() {
		return iconOpen;
	}
	public void setIconOpen(String iconOpen) {
		this.iconOpen = iconOpen;
	}
	public String getIconClose() {
		return iconClose;
	}
	public void setIconClose(String iconClose) {
		this.iconClose = iconClose;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public String getCascadeId() {
		return cascadeId;
	}
	public void setCascadeId(String cascadeId) {
		this.cascadeId = cascadeId;
	}
	
}
