package com.ims.buss.job;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ims.buss.asyncTask.PrintAsyncTask;
import com.ims.buss.constant.BussCons;
import com.ims.buss.model.MenuDict;
import com.ims.buss.model.PrintLog;
import com.ims.buss.service.MenuDictService;
import com.ims.buss.service.PrintLogService;
import com.ims.core.cache.WebplusCache;
import com.ims.core.constant.WebplusCons;
import com.ims.core.util.WebplusUtil;

/**
 * 
 * 类名:com.ims.buss.job.SysJob
 * 描述:系统定时作业
 * 编写者:陈骑元
 * 创建时间:2020年1月21日 上午9:17:52
 * 修改说明:
 */
@Component
public class SysJob {
	
	private static Log log = LogFactory.getLog(SysJob.class);
	
	@Autowired
	private MenuDictService menuDictService;
	@Autowired
	private PrintLogService printLogService;
	@Autowired
	private PrintAsyncTask printAsyncTask;
	
    /**
     * 
     * 简要说明：更新状态的停售的菜单为已售状态
     * 编写者：陈骑元
     * 创建时间：2020年1月21日 上午9:25:13
     * @param 说明
     * @return 说明
     */
	 @Scheduled(cron = "0 0 9 * * ?")
	 public void syncMenuStatusJob(){
	    String menuStatusSwitch=WebplusCache.getParamValue(BussCons.MENU_STATUS_SWITCH);
	    if(WebplusCons.SWITCH_ON.equals(menuStatusSwitch)){
	    	log.info("定时更新售完的菜单为已售任务开始");
	    	MenuDict menuDict=new MenuDict();
	    	menuDict.setMenuStatus(BussCons.MENU_STATUS_IN_SALE);
	    	EntityWrapper<MenuDict> wrapper=new EntityWrapper<MenuDict>();
	    	wrapper.eq("menu_status", BussCons.MENU_STATUS_SOLD_OUT);
	    	menuDictService.update(menuDict, wrapper);
	    	log.info("定时更新售完的菜单为已售任务结束");
	    }
	      
	 }
	   
	/**
	 * 
	 * 简要说明：定时重发失败的打印
	 * 编写者：陈骑元
	 * 创建时间：2020年1月21日 上午9:47:09
	 * @param 说明
	 * @return 说明
	 */
	@Scheduled(cron = "0 0/1 * * * ?")
	public void syncFailurePrintJob() {
		String menuStatusSwitch = WebplusCache.getParamValue(BussCons.MENU_STATUS_SWITCH);
		if (WebplusCons.SWITCH_ON.equals(menuStatusSwitch)) {
			log.info("定时发送补发失败打印服务开始");
			EntityWrapper<PrintLog> wrapper=new EntityWrapper<PrintLog>();
			wrapper.eq("error_count", 2);
			wrapper.eq("log_status", WebplusCons.ERROR+"");
			wrapper.and("TIMESTAMPDIFF(MINUTE,log_time,NOW())>1");
			List<PrintLog> printLogList=printLogService.selectList(wrapper);
			if(WebplusUtil.isNotEmpty(printLogList)){
				for(PrintLog printLog:printLogList){
					printAsyncTask.resendPrint(printLog);
				}
			}
			log.info("定时发送补发失败打印服务结束");
		}

	}

}
