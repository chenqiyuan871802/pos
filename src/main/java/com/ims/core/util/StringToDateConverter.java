package com.ims.core.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

/**
 * 
 * @author lanfeng
 *字符串转时间实体
 */
public class StringToDateConverter implements Converter<String, Date>{
	
	private static final String dateFormat = "yyyy-MM-dd HH:mm:ss";
	private static final String dateMinuteFormat = "yyyy-MM-dd HH:mm";
    private static final String shortDateFormat = "yyyy-MM-dd";

    @Override
    public Date convert(String value) {

        if(StringUtils.isEmpty(value)) {
            return null;
        }

        value = value.trim();

        try {
            if(value.contains("-")) {
                SimpleDateFormat formatter;
                if(value.contains(":")) {
                	if(value.length()>16){
                		 formatter = new SimpleDateFormat(dateFormat);
                	}else{
                		 formatter = new SimpleDateFormat(dateMinuteFormat);
                	}
                   
                }else {
                    formatter = new SimpleDateFormat(shortDateFormat);
                }

                Date dtDate = formatter.parse(value);
                return dtDate;              
            }else if(value.matches("^\\d+$")) {
                Long lDate = new Long(value);
                return new Date(lDate);
            }
        } catch (Exception e) {
            throw new RuntimeException(String.format("parser %s to Date fail", value));
        }
        throw new RuntimeException(String.format("parser %s to Date fail", value));
    }


}


