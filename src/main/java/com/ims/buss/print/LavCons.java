package com.ims.buss.print;
/**
 * 
 * 类名:com.ims.buss.lav.LavCons
 * 描述:易联云参数
 * 编写者:陈骑元
 * 创建时间:2019年8月4日 下午12:06:58
 * 修改说明:
 */
public class LavCons {

    /**
     * 主站域名 1
     */
    public static final String MAIN_HOST_DN_ONE = "open-api.10ss.net";

    /**
     * 主站url
     */
    public static final String MAIN_HOST_URL =LavUtil.MAIN_HOST_URL;

    /**
     * 获取token  and  refresh Token
     */
    public static final String GET_TOKEN = "/oauth/oauth";

    /**
     * 急速授权
     */
    public static final String SPEED_AUTHORIZE = "/oauth/scancodemodel";


    /**
     * api 打印
     */
    public static final String API_PRINT = "/print/index";

    /**
     * api 添加终端授权
     */
    public static final String API_ADD_PRINTER = "/printer/addprinter";

    /**
     * api 删除终端授权
     */
    public static final String API_DELET_PRINTER = "/printer/deleteprinter";

    /**
     * api 添加应用菜单
     */
    public static final String API_ADD_PRINT_MENU = "/printmenu/addprintmenu";

    /**
     * api 关机重启接口
     */
    public static final String API_SHUTDOWN_RESTART = "/printer/shutdownrestart";

    /**
     * api 声音调节接口
     */
    public static final String API_SET_SOUND = "/printer/setsound";

    /**
     * api 获取机型打印宽度接口
     */
    public static final String API_PRINT_INFO = "/printer/printinfo";

    /**
     * api 获取机型软硬件版本接口
     */
    public static final String API_GET_VIERSION = "/printer/getversion";

    /**
     * api 取消所有未打印订单
     */
    public static final String API_CANCEL_ALL = "/printer/cancelall";

    /**
     * api 取消单条未打印订单
     */
    public static final String API_CANCEL_ONE = "/printer/cancelone";

    /**
     * api 设置logo接口
     */
    public static final String API_SET_ICON = "/printer/seticon";

    /**
     * api 取消logo接口
     */
    public static final String API_DELET_ICON = "/printer/deleteicon";

    /**
     * api 接单拒单设置接口
     */
    public static final String API_GET_ORDER = "/printer/getorder";

    /**
     * api 打印方式接口
     */
    public static final String API_BTN_PRINT = "/printer/btnprint";


}
