package com.ims.buss.print;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import com.ims.core.matatype.Dto;
import com.ims.core.matatype.Dtos;
import com.ims.core.util.WebplusHttp;
/**
 * 
 * 类名:com.ims.buss.lav.LavApi
 * 描述:易联云接口
 * 编写者:陈骑元
 * 创建时间:2019年8月4日 下午12:06:36
 * 修改说明:
 */

public class LavApi {
 
    /**
     * 获取token 开放应用服务模式所需参数
     *
     * @param client_id  易联云颁发给开发者的应用ID 非空值
     * @param grant_type 授与方式（固定为 “authorization_code”）
     * @param sign       签名 详见API文档列表-接口签名
     * @param code       详见商户授权-获取code
     * @param scope      授权权限，传all
     * @param timestamp  当前服务器时间戳(10位)
     * @param id         UUID4 详见API文档列表-uuid4
     * @return
     */
    public static String getToken(String client_id, String grant_type, String sign, String code, String scope, String timestamp, String id) {
       Dto paramDto=Dtos.newDto();
       paramDto.put("client_id", client_id);
       paramDto.put("grant_type", grant_type);
       paramDto.put("sign", sign);
       paramDto.put("code", code);
       paramDto.put("scope", scope);
       paramDto.put("timestamp", timestamp);
       paramDto.put("id", id);
        return WebplusHttp.doPost(LavCons.MAIN_HOST_URL + LavCons.GET_TOKEN, paramDto);
    }

    /**
     * 获取token  自有应用服务模式所需参数
     *
     * @param client_id  平台id 非空值
     * @param grant_type 授与方式（固定为’client_credentials’）
     * @param sign       签名 详见API文档列表-接口签名
     * @param scope      授权权限，传all
     * @param timestamp  当前服务器时间戳(10位)
     * @param id         UUID4 详见API文档列表-uuid4
     * @return
     */
    public static String getToken(String client_id, String grant_type, String sign, String scope, String timestamp, String id) {
       Dto paramDto=Dtos.newDto();
       paramDto.put("client_id", client_id);
       paramDto.put("grant_type", grant_type);
       paramDto.put("sign", sign);
       paramDto.put("scope", scope);
       paramDto.put("timestamp", timestamp);
       paramDto.put("id", id);
        return WebplusHttp.doPost(LavCons.MAIN_HOST_URL + LavCons.GET_TOKEN, paramDto);
    }

    /**
     * 刷新access_token
     *
     * @param client_id     易联云颁发给开发者的应用ID 非空值
     * @param grant_type    授与方式（固定为 “refresh_token”）
     * @param scope         授权权限，传all
     * @param sign          签名 详见API文档列表-接口签名
     * @param refresh_token 更新access_token所需
     * @param id            UUID4 详见API文档列表-uuid4
     * @param timestamp     当前服务器时间戳(10位)
     * @return
     */
    public static String refreshToken(String client_id, String grant_type, String scope, String sign, String refresh_token, String id, String timestamp) {
       Dto paramDto=Dtos.newDto();
       paramDto.put("client_id", client_id);
       paramDto.put("grant_type", grant_type);
       paramDto.put("scope", scope);
       paramDto.put("sign", sign);
       paramDto.put("refresh_token", refresh_token);
       paramDto.put("id", id);
       paramDto.put("timestamp", timestamp);
        return WebplusHttp.doPost(LavCons.MAIN_HOST_URL + LavCons.GET_TOKEN, paramDto);
    }

    /**
     * 极速授权
     *
     * @param client_id    易联云颁发给开发者的应用ID 非空值
     * @param machine_code 易联云打印机终端号
     * @param qr_key       特殊密钥(有效期为300秒)
     * @param scope        授权权限，传all
     * @param sign         签名 详见API文档列表
     * @param id           UUID4 详见API文档列表-uuid4
     * @param timestamp    当前服务器时间戳(10位)
     * @return
     */
    public static String speedAu(String client_id, String machine_code, String qr_key, String scope, String sign, String id, String timestamp) {
       Dto paramDto=Dtos.newDto();
       paramDto.put("client_id", client_id);
       paramDto.put("machine_code", machine_code);
       paramDto.put("qr_key", qr_key);
       paramDto.put("scope", scope);
       paramDto.put("sign", sign);
       paramDto.put("id", id);
       paramDto.put("timestamp", timestamp);
        return WebplusHttp.doPost(LavCons.MAIN_HOST_URL + LavCons.SPEED_AUTHORIZE, paramDto);
    }

    /**
     * 打印
     *
     * @param client_id    易联云颁发给开发者的应用ID 非空值
     * @param access_token 授权的token 必要参数
     * @param machine_code 易联云打印机终端号
     * @param content      打印内容(需要urlencode)
     * @param origin_id    商户系统内部订单号，要求32个字符内，只能是数字、大小写字母 ，且在同一个client_id下唯一。详见商户订单号
     * @param sign         签名 详见API文档列表
     * @param id           UUID4 详见API文档列表-uuid4
     * @param timestamp    当前服务器时间戳(10位)
     * @return
     */
    public static String print(String client_id, String access_token, String machine_code, String content, String origin_id, String sign, String id, String timestamp) {
       Dto paramDto=Dtos.newDto();
       paramDto.put("client_id", client_id);
       paramDto.put("access_token", access_token);
       paramDto.put("machine_code", machine_code);
       paramDto.put("content", content);
       paramDto.put("origin_id", origin_id);
       paramDto.put("sign", sign);
       paramDto.put("id", id);
       paramDto.put("timestamp", timestamp);
        return WebplusHttp.doPost(LavCons.MAIN_HOST_URL + LavCons.API_PRINT, paramDto);
    }

    /**
     * 添加终端授权 开放应用服务模式不需要此接口 ,自有应用服务模式所需参数
     *
     * @param client_id    易联云颁发给开发者的应用ID 非空值
     * @param machine_code 易联云打印机终端号
     * @param msign        易联云终端密钥(如何快速获取终端号和终端秘钥)
     * @param access_token 授权的token 必要参数
     * @param sign         签名 详见API文档列表-接口签名
     * @param id           UUID4 详见API文档列表-uuid4
     * @param timestamp    当前服务器时间戳(10位)
     * @return
     */
    public static String addPrinter(String client_id, String machine_code, String msign, String access_token, String sign, String id, String timestamp) {
       Dto paramDto=Dtos.newDto();
       paramDto.put("client_id", client_id);
       paramDto.put("machine_code", machine_code);
       paramDto.put("msign", msign);
       paramDto.put("access_token", access_token);
       paramDto.put("sign", sign);
       paramDto.put("id", id);
       paramDto.put("timestamp", timestamp);
        return WebplusHttp.doPost(LavCons.MAIN_HOST_URL + LavCons.API_ADD_PRINTER, paramDto);
    }

    /**
     * 删除终端授权 开放应用服务模式、自有应用服务模式所需参数
     * ps 一旦删除，意味着开发者将失去此台打印机的接口权限，请谨慎操作
     *
     * @param client_id    易联云颁发给开发者的应用ID 非空值
     * @param access_token 授权的token 必要参数
     * @param machine_code 易联云打印机终端号
     * @param sign         签名 详见API文档列表-接口签名
     * @param id           UUID4 详见API文档列表-uuid4
     * @param timestamp    当前服务器时间戳(10位)
     * @return
     */
    public static String deletePrinter(String client_id, String access_token, String machine_code, String sign, String id, String timestamp) {
       Dto paramDto=Dtos.newDto();
       paramDto.put("client_id", client_id);
       paramDto.put("access_token", access_token);
       paramDto.put("machine_code", machine_code);
       paramDto.put("sign", sign);
       paramDto.put("id", id);
       paramDto.put("timestamp", timestamp);
        return WebplusHttp.doPost(LavCons.MAIN_HOST_URL + LavCons.API_DELET_PRINTER, paramDto);
    }

    /**
     * 添加应用菜单
     *
     * @param client_id    易联云颁发给开发者的应用ID 非空值
     * @param access_token 授权的token 必要参数
     * @param machine_code 易联云打印机终端号
     * @param content      json格式的应用菜单（其中url和菜单名称需要urlencode)
     * @param sign         签名 详见API文档列表-接口签名
     * @param id           UUID4 详见API文档列表-uuid4
     * @param timestamp    当前服务器时间戳(10位)
     * @return
     */
    public static String addPrintMenu(String client_id, String access_token, String machine_code, String content, String sign, String id, String timestamp) {
       Dto paramDto=Dtos.newDto();
       paramDto.put("client_id", client_id);
       paramDto.put("access_token", access_token);
       paramDto.put("machine_code", machine_code);
       paramDto.put("content", content);
       paramDto.put("sign", sign);
       paramDto.put("id", id);
       paramDto.put("timestamp", timestamp);
        return WebplusHttp.doPost(LavCons.MAIN_HOST_URL + LavCons.API_ADD_PRINT_MENU, paramDto);
    }

    /**
     * 关机重启接口
     *
     * @param client_id     易联云颁发给开发者的应用ID 非空值
     * @param access_token  授权的token 必要参数
     * @param machine_code  易联云打印机终端号
     * @param response_type 重启:restart,关闭:shutdown
     * @param sign          签名 详见API文档列表-接口签名
     * @param id            UUID4 详见API文档列表-uuid4
     * @param timestamp     当前服务器时间戳(10位)
     * @return
     */
    public static String shutDownRestart(String client_id, String access_token, String machine_code, String response_type, String sign, String id, String timestamp) {
       Dto paramDto=Dtos.newDto();
       paramDto.put("client_id", client_id);
       paramDto.put("access_token", access_token);
       paramDto.put("machine_code", machine_code);
       paramDto.put("response_type", response_type);
       paramDto.put("sign", sign);
       paramDto.put("id", id);
       paramDto.put("timestamp", timestamp);
        return WebplusHttp.doPost(LavCons.MAIN_HOST_URL + LavCons.API_SHUTDOWN_RESTART, paramDto);
    }

    /**
     * 声音调节接口
     *
     * @param client_id     易联云颁发给开发者的应用ID 非空值
     * @param access_token  授权的token 必要参数
     * @param machine_code  易联云打印机终端号
     * @param response_type 蜂鸣器:buzzer,喇叭:horn
     * @param voice         [1,2,3] 3种音量设置
     * @param sign          签名 详见API文档列表-接口签名
     * @param id            UUID4 详见API文档列表-uuid4
     * @param timestamp     当前服务器时间戳(10位)
     * @return
     */
    public static String setSound(String client_id, String access_token, String machine_code, String response_type, String voice, String sign, String id, String timestamp) {
       Dto paramDto=Dtos.newDto();
       paramDto.put("client_id", client_id);
       paramDto.put("access_token", access_token);
       paramDto.put("machine_code", machine_code);
       paramDto.put("response_type", response_type);
       paramDto.put("voice", voice);
       paramDto.put("sign", sign);
       paramDto.put("id", id);
       paramDto.put("timestamp", timestamp);
        return WebplusHttp.doPost(LavCons.MAIN_HOST_URL + LavCons.API_SET_SOUND, paramDto);
    }

    /**
     * 获取机型打印宽度接口
     *
     * @param client_id    易联云颁发给开发者的应用ID 非空值
     * @param access_token 授权的token 必要参数
     * @param machine_code 易联云打印机终端号
     * @param sign         签名 详见API文档列表-接口签名
     * @param id           UUID4 详见API文档列表-uuid4
     * @param timestamp    当前服务器时间戳(10位)
     * @return
     */
    public static String getPrintInfo(String client_id, String access_token, String machine_code, String sign, String id, String timestamp) {
       Dto paramDto=Dtos.newDto();
       paramDto.put("client_id", client_id);
       paramDto.put("access_token", access_token);
       paramDto.put("machine_code", machine_code);
       paramDto.put("sign", sign);
       paramDto.put("id", id);
       paramDto.put("timestamp", timestamp);
        return WebplusHttp.doPost(LavCons.MAIN_HOST_URL + LavCons.API_PRINT_INFO, paramDto);
    }

    /**
     * 获取机型软硬件版本接口
     *
     * @param client_id    易联云颁发给开发者的应用ID 非空值
     * @param access_token 授权的token 必要参数
     * @param machine_code 易联云打印机终端号
     * @param sign         签名 详见API文档列表-接口签名
     * @param id           UUID4 详见API文档列表-uuid4
     * @param timestamp    当前服务器时间戳(10位)
     * @return
     */
    public static String getVersion(String client_id, String access_token, String machine_code, String sign, String id, String timestamp) {
       Dto paramDto=Dtos.newDto();
       paramDto.put("client_id", client_id);
       paramDto.put("access_token", access_token);
       paramDto.put("machine_code", machine_code);
       paramDto.put("sign", sign);
       paramDto.put("id", id);
       paramDto.put("timestamp", timestamp);
        return WebplusHttp.doPost(LavCons.MAIN_HOST_URL + LavCons.API_GET_VIERSION, paramDto);
    }

    /**
     * 取消所有未打印订单
     *
     * @param client_id    易联云颁发给开发者的应用ID 非空值
     * @param access_token 授权的token 必要参数
     * @param machine_code 易联云打印机终端号
     * @param sign         签名 详见API文档列表-接口签名
     * @param id           UUID4 详见API文档列表-uuid4
     * @param timestamp    当前服务器时间戳(10位)
     * @return
     */
    public static String cancelAll(String client_id, String access_token, String machine_code, String sign, String id, String timestamp) {
       Dto paramDto=Dtos.newDto();
       paramDto.put("client_id", client_id);
       paramDto.put("access_token", access_token);
       paramDto.put("machine_code", machine_code);
       paramDto.put("sign", sign);
       paramDto.put("id", id);
       paramDto.put("timestamp", timestamp);
        return WebplusHttp.doPost(LavCons.MAIN_HOST_URL + LavCons.API_CANCEL_ALL, paramDto);
    }

    /**
     * 取消单条未打印订单
     *
     * @param client_id    易联云颁发给开发者的应用ID 非空值
     * @param access_token 授权的token 必要参数
     * @param machine_code 易联云打印机终端号
     * @param order_id     通过打印接口返回的订单号 详见API文档列表-打印接口
     * @param sign         签名 详见API文档列表-接口签名
     * @param id           UUID4 详见API文档列表-uuid4
     * @param timestamp    当前服务器时间戳(10位)
     * @return
     */
    public static String cancelOne(String client_id, String access_token, String machine_code, String order_id, String sign, String id, String timestamp) {
       Dto paramDto=Dtos.newDto();
       paramDto.put("client_id", client_id);
       paramDto.put("access_token", access_token);
       paramDto.put("machine_code", machine_code);
       paramDto.put("order_id", order_id);
       paramDto.put("sign", sign);
       paramDto.put("id", id);
       paramDto.put("timestamp", timestamp);
        return WebplusHttp.doPost(LavCons.MAIN_HOST_URL + LavCons.API_CANCEL_ONE, paramDto);
    }

    /**
     * 设置logo接口
     *
     * @param client_id    易联云颁发给开发者的应用ID 非空值
     * @param access_token 授权的token 必要参数
     * @param machine_code 易联云打印机终端号
     * @param img_url      图片地址,图片宽度最大为350px,文件大小不能超过40Kb
     * @param sign         签名 详见API文档列表-接口签名
     * @param id           UUID4 详见API文档列表-uuid4
     * @param timestamp    当前服务器时间戳(10位)
     * @return
     */
    public static String setIcon(String client_id, String access_token, String machine_code, String img_url, String sign, String id, String timestamp) {
       Dto paramDto=Dtos.newDto();
       paramDto.put("client_id", client_id);
       paramDto.put("access_token", access_token);
       paramDto.put("machine_code", machine_code);
       paramDto.put("img_url", img_url);
       paramDto.put("sign", sign);
       paramDto.put("id", id);
       paramDto.put("timestamp", timestamp);
        return WebplusHttp.doPost(LavCons.MAIN_HOST_URL + LavCons.API_SET_ICON, paramDto);
    }

    /**
     * 取消logo接口
     *
     * @param client_id    易联云颁发给开发者的应用ID 非空值
     * @param access_token 授权的token 必要参数
     * @param machine_code 易联云打印机终端号
     * @param sign         签名 详见API文档列表-接口签名
     * @param id           UUID4 详见API文档列表-uuid4
     * @param timestamp    当前服务器时间戳(10位)
     * @return
     */
    public static String deleteIcon(String client_id, String access_token, String machine_code, String sign, String id, String timestamp) {
       Dto paramDto=Dtos.newDto();
       paramDto.put("client_id", client_id);
       paramDto.put("access_token", access_token);
       paramDto.put("machine_code", machine_code);
       paramDto.put("sign", sign);
       paramDto.put("id", id);
       paramDto.put("timestamp", timestamp);
        return WebplusHttp.doPost(LavCons.MAIN_HOST_URL + LavCons.API_DELET_ICON, paramDto);
    }

    /**
     * 接单拒单设置接口
     *
     * @param client_id     易联云颁发给开发者的应用ID 非空值
     * @param access_token  授权的token 必要参数
     * @param machine_code  易联云打印机终端号
     * @param response_type 开启:open,关闭:close
     * @param sign          签名 详见API文档列表-接口签名
     * @param id            UUID4 详见API文档列表-uuid4
     * @param timestamp     当前服务器时间戳(10位)
     * @return
     */
    public static String getOrder(String client_id, String access_token, String machine_code, String response_type, String sign, String id, String timestamp) {
       Dto paramDto=Dtos.newDto();
       paramDto.put("client_id", client_id);
       paramDto.put("access_token", access_token);
       paramDto.put("machine_code", machine_code);
       paramDto.put("response_type", response_type);
       paramDto.put("sign", sign);
       paramDto.put("id", id);
       paramDto.put("timestamp", timestamp);
        return WebplusHttp.doPost(LavCons.MAIN_HOST_URL + LavCons.API_GET_ORDER, paramDto);
    }

    /**
     * 打印方式接口
     *
     * @param client_id     易联云颁发给开发者的应用ID 非空值
     * @param access_token  授权的token 必要参数
     * @param machine_code  易联云打印机终端号
     * @param response_type 开启:btnopen,关闭:btnclose; 按键打印
     * @param sign          签名 详见API文档列表-接口签名
     * @param id            UUID4 详见API文档列表-uuid4
     * @param timestamp     当前服务器时间戳(10位)
     * @return
     */
    public static String btnPrint(String client_id, String access_token, String machine_code, String response_type, String sign, String id, String timestamp) {
       Dto paramDto=Dtos.newDto();
       paramDto.put("client_id", client_id);
       paramDto.put("access_token", access_token);
       paramDto.put("machine_code", machine_code);
       paramDto.put("response_type", response_type);
       paramDto.put("sign", sign);
       paramDto.put("id", id);
       paramDto.put("timestamp", timestamp);
        return WebplusHttp.doPost(LavCons.MAIN_HOST_URL + LavCons.API_BTN_PRINT, paramDto);
    }

    public static String getSin(String timestamp) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(LavUtil.CLIENT_ID);
            stringBuilder.append(timestamp);
            stringBuilder.append(LavUtil.CLIENT_SECRET);
            return getMd5(stringBuilder.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getuuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * @param str
     * @return
     * @Description: 32位小写MD5
     */
    public static String getMd5(String str) {
        String reStr = "";
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(str.getBytes());
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b : bytes) {
                int bt = b & 0xff;
                if (bt < 16) {
                    stringBuffer.append(0);
                }
                stringBuffer.append(Integer.toHexString(bt));
            }
            reStr = stringBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return reStr;
    }
}
