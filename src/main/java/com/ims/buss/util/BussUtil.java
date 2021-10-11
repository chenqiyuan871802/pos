package com.ims.buss.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;

import org.apache.poi.ss.usermodel.Workbook;

import com.ims.buss.constant.BussCons;
import com.ims.core.cache.WebplusCache;
import com.ims.core.constant.WebplusCons;
import com.ims.core.matatype.Dto;
import com.ims.core.util.WebplusFile;
import com.ims.core.util.WebplusUtil;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;

/**
 * 
 * 类名:com.ims.buss.util.BussUtil
 * 描述:业务工具类
 * 编写者:陈骑元
 * 创建时间:2019年7月24日 上午9:13:16
 * 修改说明:
 */
public class BussUtil {
	
	/**
	 * 
	 * 简要说明：返回用餐类型
	 * 编写者：陈骑元
	 * 创建时间：2019年9月7日 下午9:21:05
	 * @param 说明
	 * @return 说明
	 */
	public static String  getMenuMealType(String timeLimitStart,String timeLimit){
		if(WebplusUtil.isEmpty(timeLimitStart)){
			timeLimitStart="900";
			
		}
		if(WebplusUtil.isEmpty(timeLimit)){
			timeLimit="1400";
			
		}
		String currentMin=WebplusUtil.getDateTimeStr("HHmm");
		int timeLimitStartInt=Integer.parseInt(timeLimitStart);
		int timeLimitInt=Integer.parseInt(timeLimit);
		int currentMinInt=Integer.parseInt(currentMin);
		if(currentMinInt>=timeLimitStartInt&&currentMinInt<=timeLimitInt){  //如果当前时间大于分割时间则返回晚餐
			return BussCons.MEAL_TYPE_LUNCH;
			
		}
		return BussCons.MEAL_TYPE_DINNER;  
	}
	
	/**
	 * 
	 * 简要说明：处理空的金额
	 * 编写者：陈骑元
	 * 创建时间：2019年7月23日 下午12:19:45
	 * @param 说明
	 * @return 说明
	 */
	public static Integer dealEmptyAmount(Integer amount){
		  
		if(WebplusUtil.isEmpty(amount)){
			return 0;
		}
		return amount;
	    
	}
	/**
	 * 
	 * 简要说明：计算税金
	 * 编写者：陈骑元
	 * 创建时间：2019年7月24日 上午9:12:34
	 * @param 说明
	 * @return 说明
	 */
	public static Integer countTaxes(Integer deskAmount,Integer menuAmount,Integer taxes){
		Float sum=deskAmount+menuAmount+0f;
		float taxesAmount=sum*taxes/100;
		return Math.round(taxesAmount);
	}
	
	/**
	 * 
	 * 简要说明：计算税后税金
	 * 编写者：陈骑元
	 * 创建时间：2019年7月24日 上午9:12:34
	 * @param 说明
	 * @return 说明
	 */
	public static Integer countAfterTaxes(Integer deskAmount,Integer menuAmount,Integer taxes){
		float taxesAmount=(deskAmount+menuAmount)*taxes/(100+taxes);
		return Math.round(taxesAmount);
	}
	/**
	 * 
	 * 简要说明：计算税后税金
	 * 编写者：陈骑元
	 * 创建时间：2019年7月24日 上午9:12:34
	 * @param 说明
	 * @return 说明
	 */
	public static Integer countAfterTaxes(Integer menuAmount,Integer taxes){
		float taxesAmount=menuAmount*taxes/(100+taxes);
		return Math.round(taxesAmount);
	}
	/**
	 * 
	 * 简要说明：计算抵扣率
	 * 编写者：陈骑元
	 * 创建时间：2019年7月24日 上午9:12:34
	 * @param 说明
	 * @return 说明
	 */
	public static Integer countSubRate(Integer menuAmount,Integer subAmount,Integer rate){
		if(WebplusUtil.isEmpty(menuAmount)) {
			menuAmount=0;
		}
		if(WebplusUtil.isEmpty(subAmount)||subAmount<0) {
			subAmount=0;
		}
	
		if(subAmount>menuAmount) {
			subAmount=menuAmount;
		}
	
		if(WebplusUtil.isEmpty(rate)||rate<=0||rate>100) {
			rate=100;
		}
		menuAmount=menuAmount-subAmount;
		float amountTemp=menuAmount+0f;
		float taxesAmount=amountTemp*rate/(100);
		return Math.round(taxesAmount);
	}
	/**
	 * 
	 * 简要说明：把字符串时间转化为整数
	 * 编写者：陈骑元
	 * 创建时间：2019年12月1日 上午9:28:58
	 * @param 说明
	 * @return 说明
	 */
	public static int countHourTime(String hourTimeStr){
		if(WebplusUtil.isNotEmpty(hourTimeStr)&&
				!BussCons.TIME_REPORT_DEFAULT.equals(hourTimeStr)){
			String[] timeArr=hourTimeStr.split(":");
			String hourStr=timeArr[0];
			String minuteStr=timeArr[1];
			int hour=Integer.parseInt(hourStr);
			int minute=Integer.parseInt(minuteStr);
			return hour*60+minute;
		}
		
		return 0;
	}
    /**
     * 
     * 简要说明：创建易联云打印订单内容
     * 编写者：陈骑元
     * 创建时间：2019年8月4日 下午3:37:45
     * @param 说明
     * @return 说明
     */
	public static String createLavPrintMenuContent(String deskName,int buyNum,String menuName){
		StringBuffer sb=new StringBuffer();
		sb.append(BussCons.SOUDN_COMMAND).append("<FH2><FW2>").append(deskName).append("</FW2></FH2>            ");
		sb.append("<FS2>").append(WebplusUtil.getDateStr(WebplusCons.DATETIMEMIN)).append("</FS2>\r");
		sb.append("@@2------------------------------------------------\r");
		sb.append("@@2<FS2>").append(buyNum).append("</FS2>  <FS2>").append(menuName).append("</FS2>");
		return sb.toString();
	}
	 /**
     * 
     * 简要说明：创建飞猪云打印订单内容
     * 编写者：陈骑元
     * 创建时间：2019年8月4日 下午3:37:45
     * @param 说明
     * @return 说明
     */
	public static String createFzPrintMenuContent(String deskName,String menuName){
		StringBuffer sb=new StringBuffer();
		sb.append("<left><FH2><FW2><FB>").append(deskName).append("</FB></FW2></FH2></left>                     <FS>");
		sb.append(WebplusUtil.getDateStr(WebplusCons.DATETIMEMIN)).append("</FS>\n\n\n");
		sb.append("************************************************\n");
		sb.append("<FS2>").append("</FS2>  <FS2><FW2><FB>").append(menuName).append("</FB></FW2></FS2>");
		return sb.toString();
	}
	
	 /**
     * 
     * 简要说明：创建飞猪云打印订单内容
     * 编写者：陈骑元
     * 创建时间：2019年8月4日 下午3:37:45
     * @param 说明
     * @return 说明
     */
	public static String createFzPrintMenuContent(String deskName,int buyNum,String menuName){
		StringBuffer sb=new StringBuffer();
		sb.append("<left><FH2><FW2><FB>").append(deskName).append("</FB></FW2></FH2></left>           <FS>");
		sb.append(WebplusUtil.getDateStr(WebplusCons.DATETIMEMIN)).append("</FS>\n\n\n");
		sb.append("************************************************\n");
		sb.append("<FS2>").append(buyNum).append("</FS2>  <FS2><FW2><FB>").append(menuName).append("</FB></FW2></FS2>");
		return sb.toString();
	}
	
	/**
	 * 
	 * 简要说明：获取返回默认值
	 * 编写者：陈骑元
	 * 创建时间：2019年12月15日 下午11:28:05
	 * @param 说明
	 * @return 说明
	 */
	public static int getInt(Dto pDto,String key,int defaultInt){
		 if(WebplusUtil.isNotAnyEmpty(pDto,key)){
			  Integer value=pDto.getInteger(key);
			 if(WebplusUtil.isNotEmpty(value)){
				 
				 return value;
			 }
		 }
		 return defaultInt;	 
	}
	/**
	 * 
	 * 简要说明：积分换算
	 * 编写者：陈骑元
	 * 创建时间：2019年12月17日 上午12:09:21
	 * @param 说明
	 * @return 说明
	 */
	public static int  convertPoint(String pointPer,int point){
		 Double per=0.005;
		 if(BussCons.POINT_PER_ONE.equals(pointPer)){
			 per=0.01;
		 }
	     Double amount=point*per;
		 amount=Math.floor(amount);
		 return amount.intValue();
	} 
	/**
	 * 
	 * 简要说明：根据报表时间段推算报表日期
	 * 编写者：陈骑元
	 * 创建时间：2020年1月11日 下午2:55:28
	 * @param 说明
	 * @return 说明
	 */
	public static String getReportDate(String timeReportStr ){
		if(WebplusUtil.isNotEmpty(timeReportStr)){
			timeReportStr=timeReportStr.replace(":","");
			int timeReport=Integer.parseInt(timeReportStr);
			String currentHour=WebplusUtil.getCurrentDate("HHmm");
			int h=Integer.parseInt(currentHour);
			if(h>=timeReport&&h<=2400	){
				return WebplusUtil.getDateStr();
			}
			return WebplusUtil.getCurrentDate(WebplusCons.DATE, -1);
		}
		
		
		return WebplusUtil.getDateStr();
	}
	/**
	 * 检查自助餐是否超时
	 * @param orderTime
	 * @param timeLimit
	 * @return
	 */
	public  static boolean checkBuffetTimeLimit(String orderTime,Long timeLimit) {
		if(WebplusUtil.isNotAnyEmpty(orderTime,timeLimit)) {
			Long second=WebplusUtil.plusSecond(orderTime);
			long limit=timeLimit*60;
			if(second<limit) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * 
	 * 简要说明：创建excel模板
	 * 编写者：陈骑元
	 * 创建时间：2019年12月20日 上午11:36:35
	 * @param 说明
	 * @return 说明
	 */
	public static String  createTemplateExcel(String excelTemplatePath,Dto dataDto){
		OutputStream out = null;
		Workbook workbook =null;
		try {
			TemplateExportParams params = new TemplateExportParams(excelTemplatePath);
			 workbook = ExcelExportUtil.exportExcel(params, dataDto);
			String folderPath=WebplusCache.getParamValue(WebplusCons.SAVE_ROOT_PATH_KEY	);
			String filePath=folderPath+File.separator+WebplusCons.EXCEL_PATH;
			WebplusFile.createFolder(filePath);
			String fileType=WebplusFile.getFileType(excelTemplatePath);
			String fileName=WebplusUtil.uuid()+"."+fileType;
			File file=new File(filePath,fileName);
			out = new FileOutputStream(file);
			workbook.write(out);
			return fileName;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			
				try {
					if(workbook!=null){
					  workbook.close();
					}
					
					if(out!=null){
						  out.close();
				    }
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
		}
		return "";
		
	}
	/**
	 * 
	 * 简要说明：格式化金额
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年8月2日 下午9:12:13 
	 * @param 说明
	 * @return 说明
	 */
	public static String formatAmount(Integer amount) {
		DecimalFormat df = new DecimalFormat("#,###");
		
		return df.format(amount);
	}
	/**
	 * 测试
	 * 
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String args[]) throws ParseException {
		  System.out.println(BussUtil.formatAmount(10000));
	}
}
