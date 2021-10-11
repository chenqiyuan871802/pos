package com.ims.buss.system;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ims.core.cache.WebplusCache;
import com.ims.core.matatype.Dto;
import com.ims.core.matatype.Dtos;
import com.ims.core.util.WebplusFormater;
import com.ims.core.util.WebplusSqlHelp;
import com.ims.core.util.WebplusUtil;
import com.ims.core.vo.R;
import java.util.List;
import com.ims.buss.model.LanguagePack;
import com.ims.buss.service.LanguagePackService;
import com.ims.buss.util.BussCache;

import org.springframework.stereotype.Controller;
import com.ims.core.web.BaseController;

/**
 * <p>
 * 语言包 前端控制器
 * </p>
 *
 * @author 陈骑元
 * @since 2019-08-03
 */
@Controller
@RequestMapping("/buss/languagePack")
public class LanguagePackController extends BaseController {

    @Autowired
    private LanguagePackService languagePackService;


	/**
	 * 
	 * 简要说明：分页查询 
	 * 编写者：陈骑元
	 * 创建时间：2019-08-03
	 * @param 说明
	 * @return 说明
	 */
	@RequestMapping("list")
	@ResponseBody
	public R list() {
		Dto pDto = Dtos.newDto(request);
		pDto.setOrder(" language_code ASC,language_type ASC ");
		Page<LanguagePack> page =languagePackService.likePage(pDto);
		WebplusCache.convertItem(page);
		return R.toPage(page);
	}



	/**
	 * 
	 * 简要说明： 新增信息保存 
	 * 编写者：陈骑元
	 * 创建时间：2019-08-03
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("save")
	@ResponseBody
	public R save(LanguagePack languagePack) {
		EntityWrapper<LanguagePack> countWrapper = new EntityWrapper<LanguagePack>();
		WebplusSqlHelp.eq(countWrapper, "language_code", languagePack.getLanguageCode());
		WebplusSqlHelp.eq(countWrapper, "language_type",languagePack.getLanguageType());
		int count=languagePackService.selectCount(countWrapper);
		if(count>0){
			return R.warn("该语言类型对照码已经存在。");
		}
		String userId=this.getUserId();
		languagePack.setCreateBy(userId);
		languagePack.setUpdateBy(userId);
		languagePack.setCreateTime(WebplusUtil.getDateTime());
		languagePack.setUpdateTime(WebplusUtil.getDateTime());
		boolean result = languagePackService.insert(languagePack);
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
	 * 创建时间：2019-08-03
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("edit")
	@ResponseBody
	public R edit(String id) {
		LanguagePack languagePack=languagePackService.selectById(id);
		return R.toData(languagePack);
	}
	
	/**
	 * 
	 * 简要说明：修改信息
	 * 编写者：陈骑元
	 * 创建时间：2019-08-03
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("update")
	@ResponseBody
	public R update(LanguagePack languagePack) {
		if(WebplusUtil.isNotEmpty(languagePack.getLanguageCode())){
			LanguagePack languagePackOld=languagePackService.selectById(languagePack.getLanguageId());
			if(!(languagePackOld.getLanguageCode().equals(languagePack.getLanguageCode())
					&&languagePackOld.getLanguageType().equals(languagePack.getLanguageType()))){
				EntityWrapper<LanguagePack> countWrapper = new EntityWrapper<LanguagePack>();
				WebplusSqlHelp.eq(countWrapper, "language_code", languagePack.getLanguageCode());
				WebplusSqlHelp.eq(countWrapper, "language_type",languagePack.getLanguageType());
				int count=languagePackService.selectCount(countWrapper);
				if(count>0){
					return R.warn("该语言类型对照码已经存在。");
				}
			}
		}
		String userId=this.getUserId();
		languagePack.setUpdateBy(userId);
		languagePack.setUpdateTime(WebplusUtil.getDateTime());
		boolean result = languagePackService.updateById(languagePack);
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
	 * 创建时间：2019-08-03
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("remove")
	@ResponseBody
	public R remove(String id) {
		boolean result = languagePackService.deleteById(id);
		if (result) {
			return R.ok();
		} else {
			return R.error("删除失败");
		}
		
	}
	
	/**
	 * 
	 * 简要说明：批量删除信息
	 * 编写者：陈骑元
	 * 创建时间：2019-08-03
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("batchRemove")
	@ResponseBody
	public R batchRemove(String ids) {
		List<String> idList=WebplusFormater.separatStringToList(ids);
		boolean result = languagePackService.deleteBatchIds(idList);
		if (result) {
			return R.ok();
		} else {
			return R.error("删除失败");
		}
		
	}
	
	@PostMapping("refreshCache")
	@ResponseBody
	public R refreshCache() {
		BussCache.cacheAllLanguage();
	    
		return R.ok();
	}
	
}

