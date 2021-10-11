

package com.ims.buss.constant;

import com.ims.core.constant.WebplusCons;

/**
 * 
 * 类名:com.ims.buss.constant.BusCons
 * 描述:业务常量
 * 编写者:陈骑元
 * 创建时间:2019年7月21日 下午11:22:22
 * 修改说明:
 */
public interface BussCons {
	
	public static final String SHOP_STATUS_BUSINESS="1";
	public static final String SHOP_STATUS_STOP="2";
	/**
	 * 订单有效时间
	 */
	public static final int ORDER_LIMIT_TIME=60*60;
	
	/**
	 * 放
	 */
    public static final String PARENT_ORDER_KEY=WebplusCons.CACHE_PREFIX_HEAD+"parent_order";
    public static final String ORDER_INFO_KEY=WebplusCons.CACHE_PREFIX_HEAD+"order_info";
    public static final String ORDER_LINE_KEY=WebplusCons.CACHE_PREFIX_HEAD+"order_line";
	/**
	 * 时间方式1 月份2日期3星期
	 */
	public static String TIME_WAY_MONTH="1";
	public static String TIME_WAY_DATE="2";
	public static String TIME_WAY_WEEK="3";
	/**
	 * 种类方式
	 */
	public static String CATALOG_WAY_LARGE="1";
	public static String CATALOG_WAY_CATALOG="2";
	public static String CATALOG_WAY_DICT="3";
	public static String CATALOG_WAY_BUFFET="4";
	
	public static String APK_TYPE_ANDROID="1";
	public static String APK_TYPE_POS="2";
	
	public static String PRINT_RECEIPT="2";
	public static String PRINT_BILL="1";
	
	/**
	 * 取消折扣方式 0全部取消1订单取消2订单菜品取消
	 * 
	 */
	public static String CANCEL_WAY_FULL="0";
	public static String CANCEL_WAY_ORDER="1";
	public static String CANCEL_WAY_ORDERLINE="2";
	/**
	 * 0 有效 1使用2过期
	 */
	public static final String COLLECT_STATUS_VALID="0";
	public static final String COLLECT_STATUS_USE="1";
	public static final String COLLECT_STATUS_OVERDUE="2";
	/**
	 * 0待发布
	 */
	public static final String COUPON_STATUS_WAIT="0";
	public static final String COUPON_STATUS_PUTAWAY="1";
	public static final String COUPON_STATUS_SOLDOUT="2";
	/**
	 * 类别 1配送2外带
	 */
	public static final String ORDER_CATEGORY_DELIVERY="1";
	public static final String ORDER_CATEGORY_PICKUP="2";
	
	/**
	 * -1：取消
	 * 0:待处理
	 * 1:准备中
	 * 2:配送中
	 * 3:提货
	 * 4:完成
	 */
	public static final String ORDER_STATUS_CANCEL="-1";
	public static final String ORDER_STATUS_WAIT="0";
	public static final String ORDER_STATUS_READY="1";
	public static final String ORDER_STATUS_SEND="2";
	public static final String ORDER_STATUS_PICKUP="3";
	public static final String ORDER_STATUS_FINISH="4";
	
	public static final String POSITION_TOP_CODE="0";
	
	/**
	 * 位置地址
	 */
	public static final String NETWORK_NUM_KEY=WebplusCons.CACHE_PREFIX_HEAD+"networkNum:";
	
	
	/**
	 * 排序操作
	 */
	public static final String SORT_UPDATE_KEY=WebplusCons.CACHE_PREFIX_HEAD+"sort_update:";
	
	
	/**
	 * 自助餐下单键
	 */
	public static final String BUFFET_ORDER_KEY=WebplusCons.CACHE_PREFIX_HEAD+"buffet_order:";
	
	/**
	 * redis缓存键
	 */
	public static final String  ORDER_LIMIT_KEY=WebplusCons.CACHE_PREFIX_HEAD+"order_limit:";
	/**
	 * 1在售2已售
	 */
	public static final String MENU_STATUS_SOLD_OUT="2";
	public static final String MENU_STATUS_IN_SALE="1";
	/**
	 * 打印菜单定时开关
	 */
	public static final String PRINT_MENU_SWITCH="print_menu_switch";
	public static final String MENU_STATUS_SWITCH="menu_status_switch";
	/**
	 * 推送所有0所有1app2打印机
	 */
	public static final String PUSH_TYPE_ALL="0";
	public static final String PUSH_TYPE_APP="1";
	public static final String PUSH_TYPE_PRINT="2";
	/**
	 * 服务菜单
	 */
	public static final String SERVER_MENU_TYPE="2";
	
	/**
	 * 自由点单
	 */
	public static final String CUSTOM_MENU_TYPE="3";
	
	/**
	 *记录同一个手机发送验证码次数
	 */
	public static final String ORDER_LIMIT=WebplusCons.CACHE_PREFIX_HEAD+"order_limit:";
	
	public static final String JP_DATETIME="yyyy-MM-dd'T'HH:mm:ss";
	/**
	 * 积分税率 1% 和0.5%;
	 */
	public static final String POINT_PER_ONE="0001";
	public static final String POINT_PER_FIVE="0000";
	/*
	 * 号码状态
	 */
	public static final String NUMBER_STATUS_USE="1";
	public static final String NUMBER_STATUS_EMPTY="0";
	
	/**
	 * 短信类型 1普通短信2验证码短信
	 */
	public static final String MSG_TYPE_COMMON="1";
	public static final String MSG_TYPE_AUTH="2";
	
	/**
	 * 验证码有效时间键  单位分钟
	 */
    public static final String AUTH_CODE_VALID_TIME_KEY="auth_code_valid_time";
	/**
	 *记录同一个手机发送验证码次数
	 */
	public static final String AUTH_CODE_COUNT=WebplusCons.CACHE_PREFIX_HEAD+"auth_code_count:";
	/**
	 *验证码小时记录次数
	 */
	public static final String AUTH_CODE=WebplusCons.CACHE_PREFIX_HEAD+"auth_code:";
	/**
	 *验证码小时记录次数
	 */
	public static final String POINT_ACCESS_TOKEN=WebplusCons.CACHE_PREFIX_HEAD+"point_access_token";
	/**
	 * 验证码一小时至多发送次数
	 */
	public static final String ONE_HOUR_NUM="one_hour_num";
	/**
	 * 日本国籍区号
	 */
	public final static String AREA_CODE_JP="81";
	/**
	 * 提交成功
	 */
	public final static String SMS_STATUS_SUBMIT="0";
	public final static String SMS_STATUS_SEND_SUCCESS="1";
	public final static String SMS_STATUS_SEND_FAIL="2";
	/**
	 * 默认统计时段
	 */
	public final static String TIME_REPORT_DEFAULT="00:00";
	/**
	 * 1税前 2税后
	 */
	public final static String TAX_TYPE_BEFORE="1";
	public final static String TAX_TYPE_AFTER="2";
	/**
	 * 展示类型 0全部 1菜品2自助餐
	 */
	public final static String DISPLAY_TYPE_FULL="0";
	public final static String DISPLAY_TYPE_MENU="1";
	public final static String DISPLAY_TYPE_BUFFET="2";
	/**
	 * 多选框 0单选 1多选
	 */
	public final static String CHOOSE_TYPE_RADIO="1";
	public final static String CHOOSE_TYPE_MULTI="2";
	
	/**
	 * 
	 * 订单类型 1普通订单 2合并订单2合并子单 4外卖订单5电话订单6个别订单
	 */
	public final static String ORDER_TYPE_COMMON="1";
	public final static String ORDER_TYPE_PARENT="2";
	public final static String ORDER_TYPE_CHILD="3";
	public final static String ORDER_TYPE_NETWORK="4";
	public final static String ORDER_TYPE_MOBILE="5";
	public final static String ORDER_TYPE_SIGNLE="6";
	
	/**
	 * 套餐名称中文键
	 */
	public final static String BUFFET_NAME_KEY="buffet";
	/**
	 * 堂食税率 10% 外带8%
	 */
	public final static Integer EAT_TAXES=10;
	public final static Integer OUT_TAXES=8;
	/**
	 *0 堂食，外带1外卖2
	 */
	public final static String CATALOG_TYPE_EATIN="0";
	public final static String CATALOG_TYPE_BESIDES="1";
	public final static String CATALOG_TYPE_TAKEOUT="2";
	/**
	 * 打印机状态 0离线1在线2缺纸
	 */
	public final static String PRINT_STATUS_OFFLINE="0";
	public final static String PRINT_STATUS_ONLINE="1";
	public final static String PRINT_STATUS_PAPER="2";
	/**
	 * 打印机类型 1 飞猪云 2易联云
	 */
	public final static String PRINT_TYPE_FZY="1";
	public final static String PRINT_TYPE_YLY="2";
	/**
	 * 菜品类型 1通用 2常规 3自助餐
	 */
	public final static String MENU_TYPE_COMMON="1";
	public final static String MENU_TYPE_RULE="2";
	public final static String MENU_TYPE_BUFFET="3";
	/**
	 * 菜品所属 1通用2午餐3晚餐
	 */
	public final static String MEAL_TYPE_COMMON="1";
	public final static String MEAL_TYPE_LUNCH="2";
	public final static String MEAL_TYPE_DINNER="3";
	/**
	 * 规格模板
	 */
	public final static String SPEC_CSV_TEMPLATE="spec_csv_template.xlsx";
	/**
	 * 默认菜单图片
	 */
	public final static String CSV_TEMPLATE="csv_template.xlsx";
	/**
	 * 默认菜单图片
	 */
	public final static String DEFAULT_MENU_DICT_IMAGE="default_dishes.svg";
	/**
	 * redis 语言缓存键
	 */
	public final static String CACHE_LANGUAGE_TYPE_KEY="language_type";
	
	public final static String SOUDN_COMMAND="<MS>0,2</MS>";
	
	public final static String LAV_ACCESS_TOKEN=WebplusCons.CACHE_PREFIX_HEAD+"lav_access_token";
	
	public final static String CACHE_LANGUAGE_KEY=WebplusCons.CACHE_PREFIX_HEAD+"language_";
	/**
	 * 规格类型 0 主规格 1子规格
	 */
	public final static String SPEC_TYPE_MAIN="0";
	public final static String SPEC_TYPE_SUB="1";
	/**
	 * 支付方式 1现金2信用卡 其他10
	 */
	public final static String PAY_WAY_CASH="1";
	public final static String PAY_WAY_CARD="2";
	public final static String PAY_WAY_OFFLINE="9";
	public final static String PAY_WAY_QT="10";

	/**
	 * 语言类型 1日语2中文3繁体字4英文5韩国
	 */
	public final static String LANGUAGE_TYPE_JP="1";
	public final static String LANGUAGE_TYPE_CH="2";
	public final static String LANGUAGE_TYPE_TC="3";
	public final static String LANGUAGE_TYPE_KRO="4";
	public final static String LANGUAGE_TYPE_ENG="5";
	
	
	public final static String DESK_STATUS_EMPTY="0";
	public final static String DESK_STATUS_FULL="1";
	/**
	 * 店铺角色编号
	 */
	public final static String SHOP_ROLE_ID="c406aa3492574763b8d79a1ef84704d8";
	
	/**
	 * 图片名前缀
	 */
	public final static String SHOP_IMAGE_PREFIX="S";
	public final static String MENU_IMAGE_PREFIX="M";
	public final static String QRCODE_IMAGE_PREFIX="Q";
	public final static String COUPON_IMAGE_PREFIX="Q";
	/**
	 * 店铺图片
	 */
	public final static String SHOP_IMAGE="shop_image";
	/**
	 * 二维码图片
	 */
	public final static String QRCODE_IMAGE="qrcode_image";
	/**
	 * 菜单图片
	 */
	public final static String MENU_IMAGE="menu_image";
	
	/**
	 * 菜单图片
	 */
	public final static String COUPON_IMAGE="coupon_image";
	/**
	 * CSV文件
	 */
	public final static String CSV_FILE="csv_file";
	/**
	 * apk文件存储
	 */
	public final static String APK_FILE="apk_file";
	
	/**
	 * 菜单图片
	 */
	public final static String MEMBER_QRCODE="member_qrcode";

}
