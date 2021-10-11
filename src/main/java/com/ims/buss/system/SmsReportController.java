package com.ims.buss.system;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ims.buss.constant.BussCons;
import com.ims.buss.model.SmsRecord;
import com.ims.buss.service.SmsRecordService;
import com.ims.buss.util.BussCache;
import com.ims.core.matatype.Dto;
import com.ims.core.matatype.Dtos;
import com.ims.core.util.WebplusUtil;


/**
 * 
 * 类名:com.ims.buss.system.SmsReportController
 * 描述:短信报告回调接口
 * 编写者:陈骑元
 * 创建时间:2019年12月15日 上午12:16:14
 * 修改说明:
 */
@Controller
public class SmsReportController {
	
	private static Log log = LogFactory.getLog(SmsReportController.class);
	
	@Autowired
	private SmsRecordService smsRecordService;
	
	/**
	 * 
	 * 简要说明：短信报告回调接口
	 * 编写者：陈骑元
	 * 创建时间：2019-10-14
	 * @param 说明
	 * @return 说明
	 */
	@GetMapping("smsReport")
	@ResponseBody
	public void smsReport(String msgid,String da,String status) {
	  log.info("短信回调报告：msgid="+msgid+",da="+da+",status="+status);
	  if(WebplusUtil.isNotAnyEmpty(msgid,da,status)){
		  String msgStatus=BussCons.SMS_STATUS_SEND_FAIL ;
		  if(BussCons.SMS_STATUS_SEND_SUCCESS.equals(status)){
			  msgStatus=BussCons.SMS_STATUS_SEND_SUCCESS;
		  }
		  SmsRecord smsRecord=new SmsRecord();
		  smsRecord.setMsgStatus(msgStatus);
		  EntityWrapper<SmsRecord> entityWrapper=new EntityWrapper<SmsRecord>();
		  da=da.replace(BussCons.AREA_CODE_JP, "");
		  entityWrapper.eq("mobile", da);
		  entityWrapper.eq("msg_id", msgid);
		  smsRecordService.update(smsRecord, entityWrapper);
	  }
	 
	}

}
