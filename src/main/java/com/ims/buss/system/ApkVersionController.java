package com.ims.buss.system;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.plugins.Page;
import com.ims.core.cache.WebplusCache;
import com.ims.core.constant.WebplusCons;
import com.ims.core.matatype.Dto;
import com.ims.core.matatype.Dtos;
import com.ims.core.util.WebplusFile;
import com.ims.core.util.WebplusQrcode;
import com.ims.core.util.WebplusUtil;
import com.ims.core.vo.R;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ims.buss.constant.BussCons;
import com.ims.buss.model.ApkVersion;
import com.ims.buss.service.ApkVersionService;
import org.springframework.stereotype.Controller;
import com.ims.core.web.BaseController;

/**
 * <p>
 * APK版本管理 前端控制器
 * </p>
 *
 * @author 陈骑元
 * @since 2019-10-14
 */
@Controller
@RequestMapping("/buss/apkVersion")
public class ApkVersionController extends BaseController {

    @Autowired
    private ApkVersionService apkVersionService;


	/**
	 * 
	 * 简要说明：分页查询 
	 * 编写者：陈骑元
	 * 创建时间：2019-10-14
	 * @param 说明
	 * @return 说明
	 */
	@RequestMapping("list")
	@ResponseBody
	public R list() {
		Dto pDto = Dtos.newDto(request);
		pDto.setOrder(" version_num DESC ");
		Page<ApkVersion> page =apkVersionService.likePage(pDto);
		WebplusCache.convertItem(page);
		return R.toPage(page);
	}



	/**
	 * 
	 * 简要说明： 新增信息保存 
	 * 编写者：陈骑元
	 * 创建时间：2019-10-14
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("save")
	@ResponseBody
	public R save(ApkVersion apkVersion) {
		String userId=this.getUserId();
		apkVersion.setCreateBy(userId);
		apkVersion.setCreateTime(WebplusUtil.getDateTime());
		apkVersion.setUpdateBy(userId);
		apkVersion.setUpdateTime(WebplusUtil.getDateTime());
		boolean result = apkVersionService.insert(apkVersion);
		if (result) {
			return R.ok();
		} else {
			return R.error("保存失败");
		}

	}
	/**
	 * 
	 * 简要说明： 跳转到编辑页面 
	 * 编写者：陈骑元
	 * 创建时间：2019-10-14
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("edit")
	@ResponseBody
	public R edit(String id) {
		ApkVersion apkVersion=apkVersionService.selectById(id);
		return R.toData(apkVersion);
	}
	
	/**
	 * 
	 * 简要说明：修改信息
	 * 编写者：陈骑元
	 * 创建时间：2019-10-14
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("update")
	@ResponseBody
	public R update(ApkVersion apkVersion) {
		String userId=this.getUserId();
		apkVersion.setUpdateBy(userId);
		apkVersion.setUpdateTime(WebplusUtil.getDateTime());
		boolean result = apkVersionService.updateById(apkVersion);
		if (result) {
			return R.ok();
		} else {
			return R.error("更新失败");
		}
		
	}
	
	
	/**
	 * 
	 * 简要说明：删除信息
	 * 编写者：陈骑元
	 * 创建时间：2019-10-14
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("remove")
	@ResponseBody
	public R remove(String id) {
		ApkVersion apkVersion=apkVersionService.selectById(id);
		boolean result = apkVersionService.deleteById(id);
		if (result) {
			String rootPath=WebplusCache.getParamValue(WebplusCons.SAVE_ROOT_PATH_KEY);
			String filePath=rootPath+File.separator+BussCons.APK_FILE+File.separator+apkVersion.getFid();
			WebplusFile.deleteFile(filePath);
			return R.ok();
		} else {
			return R.error("删除失败");
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
	@PostMapping("uploadApk")
	@ResponseBody
	public R uploadApk(@RequestParam(value = "file", required = false) MultipartFile file,String filePrefix,
			HttpServletRequest request,HttpServletResponse response) {
		if(WebplusUtil.isNotEmpty(file)&&file.getSize()>0){
			
			try {
				String rootPath=WebplusCache.getParamValue(WebplusCons.SAVE_ROOT_PATH_KEY);
				String folderPath=rootPath+File.separator+BussCons.APK_FILE;
				String apkName=file.getOriginalFilename();
				String apkFileName=WebplusUtil.uuid()+"."+WebplusFile.getFileType(apkName);
				WebplusFile.createFolder(folderPath); 
				File targetFile = new File(folderPath,apkFileName); 
				file.transferTo(targetFile);
				return R.ok().put("apkName", apkName).put("fid",apkFileName);
			} catch (Exception e) {
				
				e.printStackTrace();
			} 
			return R.error();
			 
		}
       
		return R.error();
	}
	
	/***
	 * 
	 * 简要说明：展示订单二维码
	 * 编写者：陈骑元
	 * 创建时间：2019年10月19日 下午7:30:48
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("showDownloadQrcode")
	@ResponseBody
	public R showDownloadQrcode(String id){
		ApkVersion apkVersion = apkVersionService.selectById(id);
		if (WebplusUtil.isNotEmpty( apkVersion)) {
			String qrcodeImage="Q"+apkVersion.getVersionId()+".png";
			String rootPath=WebplusCache.getParamValue(WebplusCons.SAVE_ROOT_PATH_KEY);
			String qrcodeUrl = WebplusCache.getParamValue(WebplusCons.REQUEST_URL_KEY) + "/file/downloadApk?fid="
					+apkVersion.getFid();
			String destPath=rootPath+File.separator+BussCons.QRCODE_IMAGE;
			String filePath=destPath+File.separator+qrcodeImage;
			File file=new File(filePath);
			if(!file.exists()){
				 WebplusQrcode.createQrcodeImage(qrcodeUrl, destPath, qrcodeImage); //创建二维码
			}
           
            Dto dataDto=Dtos.newDto("fileName",qrcodeImage);
            return R.toData(dataDto);
		}
		return R.error();
	}
	
}

