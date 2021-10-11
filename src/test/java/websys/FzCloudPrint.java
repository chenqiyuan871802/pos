package websys;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 飞猪云打印平台通用接口类
 * @author Veris
 * @date 2018-10-23
 * @version 1.1
 * @update 2018-11-12
 */
public class FzCloudPrint{
    // API通信基础地址
    public static String API_BASE_URL = "https://api.feizhucloud.net/";
    // 用户ID
    public static String USER_ID = "1088";
    // API_KEY
    public static String API_KEY = "4c0f27b06a27ed144dee5378ed9b9406";

    public static void main(String[] args){
        // 添加设备
        // System.out.println(addDevice("*****", "********", "测试打印机"));

        // 获取设备状态
        System.out.println(getDeviceStatus("12342955"));

        // 删除设备
        // System.out.println(delDevice("*****"));

        // 发送打印
        System.out.println(sendPrint("12342955", "测试打印"));

        // 取消未打印订单
        // System.out.println(cancelNotprint("*****"));

        // 取消所有未打印订单
        // System.out.println(cancelNotprintAll("*****"));

        // 获取订单打印状态
        // System.out.println(getPrintStatus("*****"));
    }

    /**
     * 发送打印
     * @param  device_number 设备编号
     * @param  content       打印内容
     * @return               发送状态
     */
    public static String sendPrint(String device_number, String content){
        Map<String,String> hashMap=new HashMap<>();
        hashMap.put("user_id", USER_ID);
        hashMap.put("device_number", device_number);
        hashMap.put("content", content);
        hashMap.put("sign", getSign(hashMap, API_KEY));
        return HttpUtil.sendPost(API_BASE_URL + "Task/add",hashMap,false);
    }

    /**
     * 添加终端设备
     * @param  device_number 设备编号
     * @param  device_secret 设备秘钥
     * @param  device_name   设备名称（自定义）
     * @return               执行状态
     */
    public static String addDevice(String device_number, String device_secret, String device_name){
        Map<String,String> hashMap=new HashMap<>();
        hashMap.put("user_id", USER_ID);
        hashMap.put("device_number", device_number);
        hashMap.put("device_secret", device_secret);
        hashMap.put("device_name", device_name);
        hashMap.put("sign", getSign(hashMap, API_KEY));
        return HttpUtil.sendPost(API_BASE_URL + "Device/add",hashMap,false);
    }

    /**
     * 删除终端设备
     * @param  device_number 设备编号
     * @return               执行状态
     */
    public static String delDevice(String device_number){
        Map<String,String> hashMap=new HashMap<>();
        hashMap.put("user_id", USER_ID);
        hashMap.put("device_number", device_number);
        hashMap.put("sign", getSign(hashMap, API_KEY));
        return HttpUtil.sendPost(API_BASE_URL + "Device/del",hashMap,false);
    }

    /**
     * 获取设备状态
     * @param  device_number 设备编号
     * @return               状态
     */
    public static String getDeviceStatus(String device_number){
        Map<String,String> hashMap=new HashMap<>();
        hashMap.put("user_id", USER_ID);
        hashMap.put("device_number", device_number);
        hashMap.put("sign", getSign(hashMap, API_KEY));
        return HttpUtil.sendPost(API_BASE_URL + "Device/status",hashMap,false);
    }

    /**
     * 获取订单打印状态
     * @param  print_id 打印订单ID
     * @return          状态
     */
    public static String getPrintStatus(String print_id){
        Map<String,String> hashMap=new HashMap<>();
        hashMap.put("user_id", USER_ID);
        hashMap.put("print_id", print_id);
        hashMap.put("sign", getSign(hashMap, API_KEY));
        return HttpUtil.sendPost(API_BASE_URL + "Task/status",hashMap,false);
    }

    /**
     * 取消未打印订单
     * @param  print_id 打印订单ID
     * @return          状态
     */
    public static String cancelNotprint(String print_id){
        Map<String,String> hashMap=new HashMap<>();
        hashMap.put("user_id", USER_ID);
        hashMap.put("print_id", print_id);
        hashMap.put("sign", getSign(hashMap, API_KEY));
        return HttpUtil.sendPost(API_BASE_URL + "Task/cancel",hashMap,false);
    }

    /**
     * 取消所有未打印订单
     * @param  device_number 设备编号
     * @return          状态
     */
    public static String cancelNotprintAll(String device_number){
        Map<String,String> hashMap=new HashMap<>();
        hashMap.put("user_id", USER_ID);
        hashMap.put("device_number", device_number);
        hashMap.put("sign", getSign(hashMap, API_KEY));
        return HttpUtil.sendPost(API_BASE_URL + "Task/cancelAll",hashMap,false);
    }

    private static String getSign(Map<String, String> paramsMap, String apikey){
        Object[] key_arr = paramsMap.keySet().toArray();
        Arrays.sort(key_arr);
        StringBuffer stringBuffer = new StringBuffer();
        for (Object key : key_arr) {
            String val = paramsMap.get(key);
            stringBuffer.append("&").append(key).append("=").append(val);
        }
        // API_KEY
        System.out.println("API_KEY【"+API_KEY+"】");
        //即将要签名的字符串
        String preSign=stringBuffer.append("&apikey="+apikey).toString().replaceFirst("&", "");
        System.out.println("即将要签名的字符串【"+preSign+"】");
        //生成签名sign
        String sign=getMD5(preSign);
        System.out.println("签名结果【"+sign+"】");
        return sign;
    }

    /**
     * 计算MD5
     *
     * @param input
     * @return String
     */
    private static String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
