package com.ims.core.oss;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import com.ims.core.util.WebplusUtil;

/**
 * 
 * 类名:com.ims.common.support.oss.AliOssUtil
 * 描述:阿里云OSS支持
 * 编写者:陈骑元
 * 创建时间:2019年5月17日 下午2:36:46
 * 修改说明:
 */
@Component
@PropertySource(value = "classpath:third.properties")
public class AliOss {
	
	private static Log log = LogFactory.getLog(AliOss.class);
	/**
	 * OSS节点访问的URL
	 */
	private static String endpoint;
	/**
	 * OSS用户accessKeyId;
	 */
	private static String accessKeyId;
	/**
	 * OSS用户accessKeySecret;
	 */
	private static String accessKeySecret;
	/**
	 * OSS存储bucket的名称
	 */
	private static String bucketName;
	
	@Value("${oss.endpoint}")
	public void setEndpoint(String endpoint) {
		AliOss.endpoint = endpoint;
	}
	@Value("${oss.accessKeyId}")
	public  void setAccessKeyId(String accessKeyId) {
		AliOss.accessKeyId = accessKeyId;
	}
	@Value("${oss.accessKeySecret}")
	public  void setAccessKeySecret(String accessKeySecret) {
		AliOss.accessKeySecret = accessKeySecret;
	}
	@Value("${oss.bucketName}")
	public void setBucketName(String bucketName) {
		AliOss.bucketName = bucketName;
	}
	/**
	 * 
	 * 简要说明：一个OSS客户端
	 * 编写者：陈骑元
	 * 创建时间：2019年5月17日 下午3:06:23
	 * @param 说明
	 * @return 说明
	 */
	public static OSSClient createOSSClient(){
		OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
		return ossClient;
	}
	/**
	 * 
	 * 简要说明：上传内容到指定文件中
	 * 编写者：陈骑元
	 * 创建时间：2019年5月17日 下午2:58:58
	 * @param 说明
	 * @return 说明
	 */
	public static boolean uploadByContent(String fileName,String content){
		try {
			if (WebplusUtil.isEmpty(content)) {

				throw new RuntimeException("上传的字符串不能为空");
			}
			OSSClient ossClient = AliOss.createOSSClient();
			ossClient.putObject(bucketName, fileName,
					new ByteArrayInputStream(content.getBytes()));
			// 关闭OSSClient。
			ossClient.shutdown();
			return true;
		} catch (Exception e) {
			log.error("上传指定内容到阿里云OSS存储失败："+e);
		}
		return false;
	}
	
	/**
	 * 
	 * 简要说明：上传byte数组内容到指定文件中
	 * 编写者：陈骑元
	 * 创建时间：2019年5月17日 下午2:58:58
	 * @param 说明
	 * @return 说明
	 */
	public static boolean uploadByByteArray(String fileName,byte[] byteArray){
		try {
			if (WebplusUtil.isEmpty(byteArray)) {

				throw new RuntimeException("数组内容不能为空");
			}
			OSSClient ossClient = AliOss.createOSSClient();
			ossClient.putObject(bucketName, fileName,
					new ByteArrayInputStream(byteArray));
			// 关闭OSSClient。
			ossClient.shutdown();
			return true;
		} catch (Exception e) {
			log.error("上传指定数组内容到阿里云OSS存储失败："+e);
		}
		return false;
	}
   

	/**
	 * 
	 * 简要说明：通过文件HttpUrl上传文件
	 * 编写者：陈骑元
	 * 创建时间：2019年5月17日 下午2:58:58
	 * @param 说明
	 * @return 说明
	 */
	public static boolean uploadByFileUrl(String fileName,String fileUrl){
		try {
			if (WebplusUtil.isEmpty(fileUrl)) {

				throw new RuntimeException("文件URL不能为空");
			}
			OSSClient ossClient = AliOss.createOSSClient();
			InputStream inputStream = new URL(fileUrl).openStream();
			ossClient.putObject(bucketName, fileName,
					inputStream);
			// 关闭OSSClient。
			ossClient.shutdown();
			return true;
		} catch (Exception e) {
			log.error("通过文件URL地址上传阿里云OSS存储失败："+e);
		}
		return false;
	}
	/**
	 * 
	 * 简要说明：通过文件路径上传
	 * 编写者：陈骑元
	 * 创建时间：2019年5月17日 下午2:58:58
	 * @param 说明
	 * @return 说明
	 */
	public static boolean uploadByFilePath(String fileName,String filePath){
		try {
			if (WebplusUtil.isEmpty(filePath)) {

				throw new RuntimeException("文件地址不能为空");
			}
			OSSClient ossClient = AliOss.createOSSClient();
			InputStream inputStream = new FileInputStream(filePath);
			ossClient.putObject(bucketName, fileName,
					inputStream);
			// 关闭OSSClient。
			ossClient.shutdown();
			return true;
		} catch (Exception e) {
			log.error("通过文件地址上传阿里云OSS存储失败："+e);
		}
		return false;
	}
	
	/**
	 * 
	 * 简要说明：通过文件输入流上传文件
	 * 编写者：陈骑元
	 * 创建时间：2019年5月17日 下午2:58:58
	 * @param 说明
	 * @return 说明
	 */
	public static boolean uploadByInputStream(String fileName,InputStream inputStream ){
		try {
			if (WebplusUtil.isEmpty(inputStream)) {

				throw new RuntimeException("输入流不能为空");
			}
			OSSClient ossClient = AliOss.createOSSClient();
			ossClient.putObject(bucketName, fileName,
					inputStream);
			// 关闭OSSClient。
			ossClient.shutdown();
			return true;
		} catch (Exception e) {
			log.error("通过文件输入流上传阿里云OSS存储失败："+e);
		}
		return false;
	}
	
	/**
	 * 
	 * 简要说明：通过文件输入流上传文件
	 * 编写者：陈骑元
	 * 创建时间：2019年5月17日 下午2:58:58
	 * @param 说明
	 * @return 说明
	 */
	public static boolean uploadByFile(String fileName,File file ){
		try {
			if (!file.exists()) {

				throw new RuntimeException("上传文件不存在");
			}
			OSSClient ossClient = AliOss.createOSSClient();
			ossClient.putObject(bucketName, fileName,file);
			// 关闭OSSClient。
			ossClient.shutdown();
			return true;
		} catch (Exception e) {
			log.error("通过文件上传阿里云OSS存储失败："+e);
		}
		return false;
	}
	
	/**
	 * 
	 * 简要说明：通过本地路径传文件
	 * 编写者：陈骑元
	 * 创建时间：2019年5月17日 下午2:58:58
	 * @param 说明
	 * @return 说明
	 */
	public  static boolean  uploadByLocalFilePath(String fileName,String localFilePath ){
		try {
			if (WebplusUtil.isEmpty(localFilePath)) {

				throw new RuntimeException("本地路径不能为空");
			}
			OSSClient ossClient = AliOss.createOSSClient();
			ossClient.putObject(bucketName, fileName,new File(localFilePath));
			// 关闭OSSClient。
			ossClient.shutdown();
			return true;
		} catch (Exception e) {
			log.error("通过本地文件上传阿里云OSS存储失败："+e);
		}
		return false;
	}
	
	/**
	 * 
	 * 简要说明：下载文件到本地
	 * 编写者：陈骑元
	 * 创建时间：2019年5月17日 下午2:58:58
	 * @param 说明
	 * @return 说明
	 */
	public  static boolean downloadFileToLocal(String fileName,String localFilePath ){
		try {
			if (WebplusUtil.isEmpty(localFilePath)) {

				throw new RuntimeException("本地路径不能为空");
			}
			OSSClient ossClient = AliOss.createOSSClient();
			ossClient.getObject(new GetObjectRequest(bucketName, fileName),new File(localFilePath));
			// 关闭OSSClient。
			ossClient.shutdown();
			return true;
		} catch (Exception e) {
			log.error("通过文件名下载文件到本地失败："+e);
		}
		return false;
	}
	/**
	 * 
	 * 简要说明：下载文件字节流
	 * 编写者：陈骑元
	 * 创建时间：2019年5月17日 下午6:17:33
	 * @param 说明
	 * @return 说明
	 */
	public  static byte[] downloadFileByte(String fileName){
		try {
			if (WebplusUtil.isEmpty(fileName)) {

				throw new RuntimeException("文件名称不能为空");
			}
			OSSClient ossClient = AliOss.createOSSClient();
			OSSObject ossObject = ossClient.getObject(bucketName, fileName);
			InputStream inputStream=ossObject.getObjectContent();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        int len = 1024;
	        byte tmp [] = new byte[len];
	        int i ;
	        while((i=inputStream.read(tmp, 0, len))>0){
	            baos.write(tmp, 0, i);
	         }
	    	// 关闭OSSClient。
	           inputStream.close();
	          ossClient.shutdown();
	          return   baos.toByteArray();
		
			
		} catch (Exception e) {
			log.error("通过文件名下载文件到本地失败："+e);
		}
		return new byte[0];
	}
	/**
	 * 
	 * 简要说明：通过文件名获取文件的签名地址访问
	 * 编写者：陈骑元
	 * 创建时间：2019年5月17日 下午4:08:37
	 * @param 说明 seconds 授权时间 秒
	 * @return 说明
	 */
	public static String getFilePresignedUrl(String fileName,int seconds){
		if (WebplusUtil.isEmpty(fileName)) {

			throw new RuntimeException("文件名不能为空");
		}
		OSSClient ossClient = AliOss.createOSSClient();
		Date expiration = new Date(new Date().getTime() +seconds);
		// 生成以GET方法访问的签名URL，访客可以直接通过浏览器访问相关内容。
		URL url = ossClient.generatePresignedUrl(bucketName, fileName, expiration);
		return url.toString();
	}
}
