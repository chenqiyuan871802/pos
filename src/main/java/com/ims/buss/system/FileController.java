package com.ims.buss.system;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.assertj.core.util.Lists;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.ims.buss.constant.BussCons;
import com.ims.core.cache.WebplusCache;
import com.ims.core.constant.WebplusCons;
import com.ims.core.matatype.Dto;
import com.ims.core.matatype.Dtos;
import com.ims.core.util.WebplusFile;
import com.ims.core.util.WebplusQrcode;
import com.ims.core.util.WebplusUtil;
import com.ims.core.vo.R;
import com.ims.core.web.BaseController;


/**
 * 
 * 类名:com.ims.business.controller.FileController
 * 描述:文件操作控制类
 * 编写者:陈骑元
 * 创建时间:2019年7月9日 上午8:20:26
 * 修改说明:
 */
@Controller
@RequestMapping("/file")
public class FileController extends BaseController{
	
	
	/**
	 * 显示图片
	 * @param response
	 * @param request
	 * @return whetherImage =1 如果fileName
	 */
	@RequestMapping(value = "showImage")
	public void showImage(String fileName,String whetherImage,HttpServletRequest request,HttpServletResponse response) {
		
		if(WebplusUtil.isNotEmpty(fileName)&&!"null".equals(fileName)){
			String folderPath=WebplusCache.getParamValue(WebplusCons.SAVE_ROOT_PATH_KEY	);
			String filePath=folderPath+File.separator;
			if(fileName.indexOf(BussCons.SHOP_IMAGE_PREFIX)>-1){
				filePath+=BussCons.SHOP_IMAGE;
			}else if(fileName.indexOf(BussCons.QRCODE_IMAGE_PREFIX)>-1){
				filePath+=BussCons.QRCODE_IMAGE;
			}else if(fileName.indexOf(BussCons.MENU_IMAGE_PREFIX)>-1){
				filePath+=BussCons.MENU_IMAGE;
			}else if(fileName.indexOf(BussCons.COUPON_IMAGE_PREFIX)>-1){
				filePath+=BussCons.COUPON_IMAGE;
			}
			filePath+=File.separator+fileName;
			File file=new File(filePath);
			if(file.exists()) {
				WebplusFile.downloadFile(request, response,  filePath, fileName);
			}
			
		}else{
			if(WebplusCons.WHETHER_YES.equals(whetherImage)){
				String folderPath=WebplusCache.getParamValue(WebplusCons.SAVE_ROOT_PATH_KEY	);
				String filePath=folderPath+File.separator+BussCons.DEFAULT_MENU_DICT_IMAGE;
				File file=new File(filePath);
				if(file.exists()) {
					WebplusFile.downloadFile(request, response,  filePath, BussCons.DEFAULT_MENU_DICT_IMAGE);
				}
				
			}
		}
		
		
		
	}
	/**
	 * 
	 * 简要说明：下载APK方法
	 * 编写者：陈骑元
	 * 创建时间：2019年10月25日 下午11:03:57      
	 */
	@RequestMapping(value = "downloadApk")
	public void downloadApk(String fid,HttpServletRequest request,HttpServletResponse response) {
		
		if(WebplusUtil.isNotEmpty(fid)){
			String folderPath=WebplusCache.getParamValue(WebplusCons.SAVE_ROOT_PATH_KEY	);
			String filePath=folderPath+File.separator+BussCons.APK_FILE+File.separator+fid	;
			WebplusFile.downloadFile(request, response,  filePath, fid);
		}
		
	}
	
	
	/**
	 * 
	 * 简要说明： 新增信息保存 
	 * 编写者：陈骑元
	 * 创建时间：2019-02-23
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("uploadImage")
	@ResponseBody
	public R uploadImage(@RequestParam(value = "file", required = false) MultipartFile file,String filePrefix,
			HttpServletRequest request,HttpServletResponse response) {
		if(WebplusUtil.isNotEmpty(file)&&file.getSize()>0){
			
			try {
				String rootPath=WebplusCache.getParamValue(WebplusCons.SAVE_ROOT_PATH_KEY);
				String folderPath=rootPath+File.separator;
				if(BussCons.MENU_IMAGE_PREFIX.equals(filePrefix)){
					folderPath+=BussCons.MENU_IMAGE;
							
				}else if(BussCons.SHOP_IMAGE_PREFIX.equals(filePrefix)){
					folderPath+=BussCons.SHOP_IMAGE;
				}else if(BussCons.COUPON_IMAGE_PREFIX.equals(filePrefix)){
					folderPath+=BussCons.COUPON_IMAGE;
				}
				String imageName=file.getOriginalFilename();
				String imageFileName=WebplusUtil.createFileId(filePrefix)+"."+WebplusFile.getFileType(imageName);
				WebplusFile.createFolder(folderPath); 
				File targetFile = new File(folderPath,imageFileName); 
				file.transferTo(targetFile);
				return R.ok().put("fileName", imageFileName);
			} catch (Exception e) {
				
				e.printStackTrace();
			} 
			return R.error();
			 
		}
       
		return R.error();
	}
	/**
	 * 
	 * 简要说明： 多文件上传
	 * 编写者：陈骑元
	 * 创建时间：2019-02-23
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("uploadMoreImage")
	@ResponseBody
	public R uploadMoreImage(String filePrefix,HttpServletRequest request,HttpServletResponse response) {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		
		try {
			if (multipartResolver.isMultipart(request)) {
				MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
				Iterator<String> fileForms = multipartRequest.getFileNames();
				String rootPath=WebplusCache.getParamValue(WebplusCons.SAVE_ROOT_PATH_KEY);
				String folderPath=rootPath+File.separator;
				if(BussCons.MENU_IMAGE_PREFIX.equals(filePrefix)){
					folderPath+=BussCons.MENU_IMAGE;
							
				}else if(BussCons.SHOP_IMAGE_PREFIX.equals(filePrefix)){
					folderPath+=BussCons.SHOP_IMAGE;
				}
				List<Dto> dataList=Lists.newArrayList();
				while (fileForms.hasNext()) {
					String fileForm = fileForms.next();// 文件提交表单域名称
					List<MultipartFile> files = multipartRequest.getFiles(fileForm);
					for (int i = 0; i < files.size(); i++) {
						MultipartFile file = files.get(i);
						if (file != null && file.getSize() > 0) { // 如果存在上传文件

							String imageName = file.getOriginalFilename();
							String imageFileName = WebplusUtil.createFileId(filePrefix)+ "." + WebplusFile.getFileType(imageName);
							WebplusFile.createFolder(folderPath);
							File targetFile = new File(folderPath, imageFileName);
							file.transferTo(targetFile);
							Dto dataDto=Dtos.newDto();
							dataDto.put("fileName", imageFileName);
							dataList.add(dataDto);
						}
					}
				}
				return R.toList(dataList);

			} 
		} catch (Exception e) {
			return R.error();
		}
		return R.error();
	}
	
	/**
	 * 
	 * 简要说明：下载导入模板文件
	 * 编写者：陈骑元
	 * 创建时间：2019年8月26日 下午5:55:50
	 * @param 说明
	 * @return 说明
	 */
	@RequestMapping(value = "downloadTemplateFile")
	public  void downloadTemplateFile(String fileName,HttpServletRequest request,HttpServletResponse response){
		if(WebplusUtil.isNotEmpty(fileName)){
			String filePath="";
			try {
				filePath = ResourceUtils.getURL("classpath:excel").getPath()+File.separator+fileName;
				WebplusFile.downloadFile(request, response,  filePath, fileName);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		
		}
	}
	/**
	 * 
	 * 简要说明：下载excel文文件
	 * 编写者：陈骑元
	 * 创建时间：2019年8月26日 下午5:55:50
	 * @param 说明
	 * @return 说明
	 */
	@RequestMapping(value = "downloadExcelFile")
	public  void downloadExcelFile(String fid,String fileName,HttpServletRequest request,HttpServletResponse response){
		if(WebplusUtil.isNotEmpty(fid)){
			String folderPath=WebplusCache.getParamValue(WebplusCons.SAVE_ROOT_PATH_KEY	);
			String filePath=folderPath+File.separator+WebplusCons.EXCEL_PATH+File.separator+fid;;
			if(WebplusUtil.isEmpty(fileName)){
				fileName=fid;
			}
			WebplusFile.downloadFile(request, response,  filePath, fileName);
		}
	}
	
	/***
	 * 
	 * 简要说明：展示订单二维码
	 * 编写者：陈骑元
	 * 创建时间：2019年10月19日 下午7:30:48
	 * @param 说明
	 * @return 说明
	 */
	@RequestMapping("showShopQrcode")
	public void showShopQrcode(String fileName,String shopId,HttpServletRequest request,HttpServletResponse response){
		if (WebplusUtil.isNotEmpty(fileName)) {
			String rootPath=WebplusCache.getParamValue(WebplusCons.SAVE_ROOT_PATH_KEY);
			
			String destPath=rootPath+File.separator+BussCons.QRCODE_IMAGE;
			String filePath=destPath+File.separator+fileName;
			File file=new File(filePath);
			if(!file.exists()){
				 String qrcodeUrl=WebplusCache.getParamValue(WebplusCons.REQUEST_URL_KEY)+"/h5/index.html?shopId="+shopId;
						
				 WebplusQrcode.createQrcodeImage(qrcodeUrl, destPath, fileName); //创建二维码
			}
           
			WebplusFile.downloadFile(request, response,  filePath,fileName);
		}
	}
	/***
	 * 
	 * 简要说明：展示订单二维码
	 * 编写者：陈骑元
	 * 创建时间：2019年10月19日 下午7:30:48
	 * @param 说明
	 * @return 说明
	 */
	@RequestMapping("showTakeOutQrcode")
	public void showTakeOutQrcode(String shopId,HttpServletRequest request,HttpServletResponse response){
		if (WebplusUtil.isNotEmpty(shopId)) {
			String qrcodeImage="Q"+shopId+"1.png";
			String rootPath=WebplusCache.getParamValue(WebplusCons.SAVE_ROOT_PATH_KEY);
			
			String destPath=rootPath+File.separator+BussCons.QRCODE_IMAGE;
			String filePath=destPath+File.separator+qrcodeImage;
			File file=new File(filePath);
			if(!file.exists()){
				String qrcodeUrl = WebplusCache.getParamValue(WebplusCons.TAKEOUT_URL_KEY) + "/h6/index.html?shopId="+shopId;
						
				 WebplusQrcode.createQrcodeImage(qrcodeUrl, destPath, qrcodeImage); //创建二维码
			}
           
			WebplusFile.downloadFile(request, response,  filePath,qrcodeImage);
		}
	}
}
