package com.ims.core.constant;


/**
 * 
 * 类名:com.toonan.constant.WebplusCons
 * 描述:webplus全局常量配置表
 * 编写者:陈骑元
 * 创建时间:2019年4月25日 上午11:50:14
 * 修改说明:
 */
public interface WebplusCons {
	
	
	/**
	 * 文件保存跟路径
	 */
	public static final String SAVE_ROOT_PATH_KEY="save_root_path";
	
	  public  final static String EXCEL_PATH="excel";
	/**
	 * 请求地址
	 */
	public static final String REQUEST_URL_KEY="request_url";
	/**
	 * 请求地址
	 */
	public static final String TAKEOUT_URL_KEY="take_out_url";
	
	/**
	 * 支付开关
	 */
	public static final String PAY_SWITCH_KEY="pay_switch";
	
	/**
	 * token默认超时时间2小时
	 */
	public static int DEFAULT_TIMECOUT=8*3600;
	
	public static final String CACHE_PREFIX_HEAD=System.getProperty("appId")+":";
	/**
	 * 当前状态：启用
	 */
	public static final String ENABLED_YES = "1";
	
	/**
	 * 当前状态：停用
	 */
	public static final String ENABLED_NO = "0";
	/**
	 * 编辑模式：只读
	 */
	public static final String EDITMODE_READ="0";
	/**
	 * 编辑模式：可编辑
	 */
	public static final String EDITMODE_EDIT="1";
	
	/**
	 * redis模式单机0单机1集群
	 */
	public static final String REDIS_MODE_SINGLE="0";
	public static final String REDIS_MODE_CLUSTER="1";
	/**
	 * token参数
	 */
	public static String TOKEN_PARAM="token";
	/**
     * 图南webplus
     */
	public static final String PASSWORD_KEY = "TOONANWEBPLUS";
	/**
	 * 是否
	 */
	public static final String WHETHER_YES="1";
	public static final String WHETHER_NO="0";

	/**
	 * 开关 类型 on开off关闭
	 */
	public static final String SWITCH_ON="on";
	public static final String SWITCH_OFF="off";
	/**
	 * 分页
	 */
	public static final String DEFAULT_PAGE="1";
	public static final String PAGE_LAYUI="1";
	public static final String PAGE_EASYUI="2";
	/**
	 * 分页相关的变量
	 */
	public static final String DEFAULT_CURRENTPAGE="1";
	public static final String DEFAULT_PAGESIZE="20";
	public static final String PAGE="page";
	public static final String LIMIT="limit";
	/**
	 * 日期格式变量
	 */
	public static final String DATE = "yyyy-MM-dd";
	/**
	 * 日期时间格式
	 */
	public static final String DATETIME = "yyyy-MM-dd HH:mm:ss";
	/**
	 * 日期时间格式 精确到分
	 */
	public static final String DATETIMEMIN = "yyyy-MM-dd HH:mm";
	/**
	 * 日期数字格式化
	 */
	public static final String DATENUMBER = "yyyyMMdd";
	/**
	 * 日期时间数字格式化
	 */
	public static final String DATETIMENUMBER = "yyyyMMddHHmmss";
	
	/**
	 * 业务状态码：成功
	 */
	public static final int SUCCESS = 1;
	
	/**
	 * 业务状态吗：警告
	 */
	public static final int WARN = 0;

	/**
	 * 业务状态码：失败
	 */
	public static final int ERROR = -1;
	/**
	 * 业务状态码：TOKEN为空
	 */
	public static final int EMPTY_TOKEN =-2;
	/**
	 * 业务状态码：token校验失败
	 */
	public static final int ERROR_TOKEN =-3;
	/**
	 * Dto对象中的内部变量：交易状态码
	 */
	public static final String APPCODE_KEY = "appCode";

	/**
	 * Dto对象中的内部变量：交易状态信息
	 */
	public static final String APPMSG_KEY = "appMsg";
	/**
	 * 数序运算SQL的参数Dto中的运算表达式Key。
	 */
	public static final String CALCEXPR = "_expr";


	/**
	 * DTO缺省字符串Key
	 */
	public static final String DEFAULT_STRING_KEY = "_default_string_a";

	/**
	 * DTO缺省List Key
	 */
	public static final String DEFAULT_LIST_KEY = "_default_list_a";

	/**
	 * DTO缺省BigDecimal Key
	 */
	public static final String DEFAULT_BIGDECIMAL_KEY = "_default_bigdecimal_a";

	/**
	 * DTO缺省Integer Key
	 */
	public static final String DEFAULT_INTEGER_KEY = "_default_integer_a";

	/**
	 * DTO缺省Boolean Key
	 */
	public static final String DEFAULT_BOOLEAN_KEY = "_default_boolean_a";

	/**
	 * 逗号分隔符
	 */
	public static final String MARK_CSV=",";
	/**
	 * 顿号分隔符
	 */
	public static final String MARK_PAUSE="、";
	/**
	 * 排序器在参数对象中的Key
	 */
	public static final String ORDER_KEY = "_order";
	/**
	 * 控制台醒目标记1
	 */
	public static final String CONSOLE_FLAG1 = "● ";
	/**
	 * 大写英文字母
	 */
	public static final String UPPER_LETTER = "ABCDEFGHIJKLMNOPKRSTUVWXYZ";
	/**
	 * 小写的英文字母
	 */
	public static final String LOWCASE_LETTER = "abcdefghijklmnopqrstuvwxyz";
	/**
	 * 数字字母
	 */
	public static final String NUMBER_LETTER= "0123456789";
	
	/**
	 * 位置地址
	 */
	public static final String POSITION_KEY=WebplusCons.CACHE_PREFIX_HEAD+"position:";
	
	public static final String POSITION_DICT="position";
	/**
	 * 随机混合的类型
	 *
	 */
	public static  final class RANDOM_TYPE{
		//全部参与混合
		public static final String  FULL= "1";
		//大写
		public static final String UPPER = "2";
		//小写
		public static final String LOWCASE = "3";
		//数字
		public static final String NUMBER = "4";
		//大写与数字混合
		public static final String UPPER_NUMBER = "5";
		//大写与小写混合
		public static final String  UPPER_LOWCASE = "6";
		//小写与数字混合
		public static final String LOWCASE_NUMBER = "7";
		
	}
	/**
	 * Cache对象前缀
	 *
	 */
	public static  final class CACHE_PREFIX{
		// 全局参数
		public static final String PARAM = WebplusCons.CACHE_PREFIX_HEAD + "param:";
		// 字典
		public static final String DICT = WebplusCons.CACHE_PREFIX_HEAD + "dict:";
		// token
		public static final String TOKEN = WebplusCons.CACHE_PREFIX_HEAD + "token:";
		// 序列
		public static final String SEQ = WebplusCons.CACHE_PREFIX_HEAD + "seq:";
		// 用户
		public static final String USER = WebplusCons.CACHE_PREFIX_HEAD + "user:";
		// 缓存菜单
		public static final String MENU = WebplusCons.CACHE_PREFIX_HEAD + "menu";
		// 用户授权菜单
		public static final String ROLEMENU = WebplusCons.CACHE_PREFIX_HEAD + "roleMenu:";
		
		
	}

}
