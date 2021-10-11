package websys;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.assertj.core.util.Lists;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


import com.ims.buss.model.MenuDict;
import com.ims.buss.util.PointApiUtil;
import com.ims.core.matatype.Dto;
import com.ims.core.matatype.Dtos;
import com.ims.core.util.WebplusHashCodec;
import com.ims.core.util.WebplusJson;
import com.ims.core.util.WebplusUtil;

import net.sf.json.JSONObject;

public class TestPrint {

	public static void main(String[] args) throws FileNotFoundException {
		
		Dto dataDto = Dtos.newDto();
		dataDto.put("configid", "test1234");
		/*
		 * Dto memberDto=Dtos.newDto(); memberDto.put("MemberID", "OI001");
		 * memberDto.put("Cardeditno","C0001"); dataDto.put("member", memberDto);
		 */
		
		Dto transactionDto = Dtos.newDto();
		transactionDto.put("OrderID", "2005102000469");
		transactionDto.put("Amount", 1);
		String[] payMethods = new String[] { "credit" };
		transactionDto.put("PayMethods", payMethods);
		transactionDto.put("RetUrl", "http://order.xiaomiqiu.com/websys/h5/payReturn");
		dataDto.put("transaction", transactionDto);
		Dto customer = Dtos.newDto();
		customer.put("MailAddress", "werewrew");
		customer.put("CustomerName", "dsfds");
		customer.put("CustomerKana", "ewe");
		customer.put("TelNo", "13802907704");
		customer.put("credit", "4123450131003312");
		//dataDto.put("customer", customer);
		Dto desDto = Dtos.newDto();
		desDto.put("TemplateID", "designA");
		desDto.put("ShopName:", "ADMINISTRATOR");
		desDto.put("Lang", "ja");
		dataDto.put("displaysetting", desDto);
        Dto cardDto=Dtos.newDto();
        cardDto.put("JobCd", "AUTH");
        cardDto.put("Method", "1");
        cardDto.put("PayTime", null);
        cardDto.put("ItemCode", "0000990");
        cardDto.put("TdFlag", "0");
        cardDto.put("MemberID", "20200904001");
        dataDto.put("credit", cardDto);
		String json=WebplusJson.toJson(dataDto);
		//System.out.println(json);
	  json=WebplusHashCodec. decBase64("eyJjb25maWdpZCI6InRlc3QxMjM0IiwibWVtYmVyIjp7Ik1lbWJlcklEIjoiMjg5OTAwMDAwMDYwMiIsIk1lbWJlck5hbWUiOiJrb2hhcnUiLCJDYXJkZWRpdG5vIjoiMjg5OTAwMDAwMDYwMiIsIlJldFVybCI6Imh0dHA6Ly8xNC4xMTYuMjA1LjIyODo4ODg2L3dlYnN5cy9oNS9wYXlSZXR1cm4/bWVtYmVySWRcdTAwM2RkNDU2M2JjMTBiMjQ0YTBhYTZlOGIyMjFjYjYwN2Y2OFx1MDAyNnNob3BJZFx1MDAzZGI2NDM2Nzc1OTJlNTQ4ZjhhODExOGY1Nzg0MmE3MmM0XHUwMDI2b3JkZXJGb29kVHlwZVx1MDAzZDIifX0=");
	   System.out.println(json);
	
		String baseJson=WebplusHashCodec.base64(json);
	    String sha=baseJson+"q8wwk5zf";
	    System.out.println(sha);
	    String b="https://stg.link.mul-pay.jp/v1/plus/tshop00044559/checkout/"+baseJson+"."+WebplusHashCodec.sha256(sha);
	    System.out.println(b);
	   
	  
	}
}
