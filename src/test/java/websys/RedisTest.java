package websys;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ims.WebplusApplication;
import com.ims.buss.model.Position;
import com.ims.buss.print.FzCloudUtil;
import com.ims.buss.service.PositionService;
import com.ims.buss.sms.FastooUtil;
import com.ims.buss.util.BussUtil;
import com.ims.buss.util.PointApiUtil;
import com.ims.core.matatype.Dto;
import com.ims.core.matatype.Dtos;
import com.ims.core.util.WebplusHttp;
import com.ims.core.util.WebplusJson;
import com.ims.core.util.WebplusUtil;



@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = WebplusApplication.class)
public class RedisTest {
	


	 @Test
     public void test() {
		 FzCloudUtil.sendOpenCashbox("12343770", "2");
		 
			  
     }
     

		private void  save(String parentCode,String city) {
			String url = "http://api.thni.net/jzip/X0401/JSON/J/沖縄県/" + city +"/street_index.js";
			String json = WebplusHttp.doGet(url, Dtos.newDto());
			System.out.println(json);
			if (WebplusUtil.isNotEmpty(json)) {
				List<Dto> dataList = WebplusJson.fromJson(json);
				for (int i = 0; i < dataList.size(); i++) {
					Dto dataDto = dataList.get(i);
					String positionName = dataDto.getString("name");
					String kana = dataDto.getString("kana");
					String positionCode = parentCode + StringUtils.leftPad((i + 1) + "", 3, "0");
					Position position = new Position();
					position.setPositionCode(positionCode);
					position.setPositionName(positionName);
					position.setParentCode(parentCode);
					position.setKana(kana);
					position.setPositionType("3");
				}
			}
		}
	

	@Test
	public void getProvinceCityArea() throws Exception {
		/*
		 * String url = "http://api.thni.net/jzip/X0401/JSON/J/沖縄県/city_index.js";
		 * String json = WebplusHttp.doGet(url, Dtos.newDto());
		 * System.out.println(json); if (WebplusUtil.isNotEmpty(json)) { List<Dto>
		 * dataList = WebplusJson.fromJson(json); String parentCode = "100"; for (int i
		 * = 0; i < dataList.size(); i++) { Dto dataDto = dataList.get(i); String
		 * positionName = dataDto.getString("name"); String kana =
		 * dataDto.getString("kana"); String positionCode = parentCode +
		 * StringUtils.leftPad((i + 1) + "", 3, "0"); Position position = new
		 * Position(); position.setPositionCode(positionCode);
		 * position.setPositionName(positionName); position.setParentCode(parentCode);
		 * position.setKana(kana); position.setPositionType("2");
		 * positionSevice.insert(position); String streetUrl =
		 * "http://api.thni.net/jzip/X0401/JSON/J/沖縄県/" + positionName +
		 * "/street_index.js"; String jsonNew = WebplusHttp.doGet(streetUrl,
		 * Dtos.newDto()); System.out.println("jsonNew:"+jsonNew); if
		 * (WebplusUtil.isNotEmpty(jsonNew)) { List<Dto> streeList =
		 * WebplusJson.fromJson(jsonNew); for (int j = 0; j < streeList.size(); j++) {
		 * Dto streeDto = streeList.get(j); String codeNew=positionCode +
		 * StringUtils.leftPad((j + 1) + "", 3, "0"); Position positionNew = new
		 * Position(); positionNew.setPositionCode(codeNew);
		 * positionNew.setPositionName(streeDto.getString("name"));
		 * positionNew.setParentCode(positionCode);
		 * positionNew.setKana(streeDto.getString("kana"));
		 * positionNew.setPositionType("3"); positionSevice.insert(positionNew); } } }
		 * 
		 * }
		 */
	
		
	}

}
