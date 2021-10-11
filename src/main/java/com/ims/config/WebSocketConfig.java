package com.ims.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
/**
 * 
 * 类名:com.ims.config.WebSocketConfig
 * 描述:开启webSocket支持
 * 编写者:陈骑元
 * 创建时间:2019年7月30日 下午8:43:36
 * 修改说明:
 */
//@Configuration
public class WebSocketConfig{
	
	//@Bean  
    public ServerEndpointExporter serverEndpointExporter() {  
		
        return new ServerEndpointExporter();  
    }

	
   
}
