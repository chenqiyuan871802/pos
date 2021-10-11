package websys;

import com.ims.core.matatype.Dto;
import com.ims.core.matatype.Dtos;
import com.ims.core.util.WebplusHashCodec;
import com.ims.core.util.WebplusJson;

/**
 * 
 * @ClassName:  TestAccount   
 * @Description:TODO
 * @author: 陈骑元（chenqiyuan@toonan.com)
 * @date:   2020年9月4日 下午11:05:46     
 * @Copyright: 2020 www.toonan.com Inc. All rights reserved. 
 * 注意：本内容仅限于广州市图南软件有限公司内部传阅，禁止外泄以及用于其他的商业目
 */
public class TestAccount {
	
	
    public static void main(String[] args) {
    	Dto dataDto = Dtos.newDto();
		dataDto.put("configid", "test1234");
	    Dto memberDto=Dtos.newDto();
	    memberDto.put("MemberID","20200904005");
	    memberDto.put("MemberName","chenqiyuan");
	    memberDto.put("Cardeditno",System.currentTimeMillis()+"");
	    dataDto.put("member",memberDto);
	    String json=WebplusJson.toJson(dataDto);
	    String baseJson=WebplusHashCodec.base64(json);
	    String sha=baseJson+"q8wwk5zf";
	    String b="https://stg.link.mul-pay.jp/v1/plus/tshop00044559/member/"+baseJson+"."+WebplusHashCodec.sha256(sha);
	    System.out.println(b);
	    json=WebplusHashCodec. decBase64("eyJjb25maWdpZCI6InRlc3QxMjM0IiwibWVtYmVyIjp7Ik1lbWJlcklEIjoiMjg5OTAwMDAwMDM0MyIsIk1lbWJlck5hbWUiOiLjgr/jg4rjgqvjg4Tjg6jjgrciLCJDYXJkZWRpdG5vIjoiMjEiLCJSZXRVcmwiOiJodHRwczovL2FhY2hpaW1vYi5jb20vd2Vic3lzL2g1L21lbWJlclJldHVybj9tZW1iZXJJZFx1MDAzZGFkMjdkMjE5LTk3OGMtMTFlYS1iOWVjLTA2NTM4Nzk1YWRlNFx1MDAyNnNob3BJZFx1MDAzZDQ3YzhlZmU3YTkzMzRiMDhiZGQwNjA5MDg1Y2IyY2ZlXHUwMDI2b3JkZXJGb29kVHlwZVx1MDAzZDBcdTAwMjZ0b2tlblx1MDAzZDZhMDgzMzRjNGE0NDRkNmI4NmY5OWJhYjM1Mjg3YjQ3In19");
		   System.out.println(json);
	   
    }

}
