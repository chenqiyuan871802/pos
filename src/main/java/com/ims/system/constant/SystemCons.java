package com.ims.system.constant;


/**
 * 
 * 类名:com.toonan.constant.SystemCons
 * 描述:系统常量表
 * 编写者:陈骑元
 * 创建时间:2019年4月26日 下午8:47:12
 * 修改说明:
 */
public interface SystemCons {
	
	/**
	 * 通用密码
	 */
	public static String COMMON_PASSWORD_KEY="common_password";
	
	/**
	 * 用户类型：普通用户
	 */
	public static final String USER_TYPE_COMMON = "1";

	/**
	 * 用户类型：超级用户
	 */
	public static final String USER_TYPE_SUPER = "2";

	/**
	 * 用户类型：商户
	 */
	public static final String USER_TYPE_SHOP = "3";
	

	/**
	 * 菜单类型：系统菜单共享
	 */
	public static final String MENU_TYPE_SYSTEM = "1";
	/**
	 * 用户字典编码参数
	 */
	public static String TYPECODE_SYSUSER="SYSUSER";
	/**
	 * 角色菜单开关
	 */
	public static String ROLE_MENU_SWITCH_KEY="role_menu_switch";
	
	/**
	 * 菜单类型：父菜单
	 */
	public static final String MODULE_TYPE_PARENT = "1";
	/**
	 * 菜单类型：子菜单
	 */
	public static final String MODULE_TYPE_SUB = "2";
	/**
	 * 菜单类型：按钮
	 */
	public static final String MODULE_TYPE_BUTTON = "3";
	
	/**
	 * 超级账号
	 */
	public static final String SUPER_ADMIN="super";
	/**
	 * 用户状态 有效
	 */
	public static final String USER_STATE_VALID = "1";

	/**
	 * 用户状态 停用
	 */
	public static final String USER_STATUS_STOP = "2";
	/**
	 * 用户状态 锁定
	 */
	public static final String USER_STATUS_LOCK = "3";
	
	/**
	 * 
	 */
	public static final String TREE_ROOT_ID="0";
	/**
	 * 科目根节点名称
	 */
	public static final String TREE_ROOT_NAME="全部分类";

	/**
	 * 科目根节点语义ID
	 */
	public static final String TREE_ROOT_CASCADE_ID="0";
	/**
	 * 树的节点打开
	 */
	public static final String TREE_STATE_OPEN="open";
	/**
	 * 树的节点关闭
	 */
	public static final String TREE_STATE_CLOSED="closed";
	/**
	 * 菜单的根节点的名称
	 */
	public static final String MENU_ROOT_NAME="功能菜单";
	/**
	 * 组织机构根节点名称
	 */
	public static final String DEPT_ROOT_NAME="组织机构";
	/**
	 * 菜单根节点图标
	 */
	public static final String MENU_ROOT_ICONCLS="book";
	/**
	 * 组织机构根节点图标
	 */
	public static final String DEPT_ROOT_ICONCLS="dept_config";
	

}
