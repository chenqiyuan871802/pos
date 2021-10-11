package com.ims.websocket;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.ims.core.util.WebplusUtil;

@ServerEndpoint("/appPushServer/{userId}")
@Component
public class AppPushServer {

	static Log log = LogFactory.getLog(AppPushServer.class);
	// 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
	private static int onlineCount = 0;
	// concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
		private static CopyOnWriteArraySet<AppPushServer> webSocketSet = new CopyOnWriteArraySet<AppPushServer>();

	// 与某个客户端的连接会话，需要通过它来给客户端发送数据
	private Session session;

	// 接收sid
	private String userId = "";

	/**
	 * 连接建立成功调用的方法
	 */
	@OnOpen
	public void onOpen(Session session, @PathParam("userId") String userId) {
		this.session = session;
		webSocketSet.add(this); // 加入set中
		addOnlineCount(); // 在线数加1
		log.info("有新窗口开始监听:userId=(" +userId + "),当前在线人数为" + getOnlineCount());
		this.userId = userId;
		sendMessage("连接成功");
	
	}

	/**
	 * 连接关闭调用的方法
	 */
	@OnClose
	public void onClose() {
		webSocketSet.remove(this);
		subOnlineCount(); // 在线数减1
		log.info("有一连接关闭！当前在线人数为" + getOnlineCount());
	}

	/**
	 * 收到客户端消息后调用的方法
	 *
	 * @param message
	 *            客户端发送过来的消息
	 */
	@OnMessage
	public void onMessage(String message, Session session) {
		log.info("收到来自窗口:userId=(" + userId + ")的信息:" + message);
	
	}

	/**
	 * 
	 * @param session
	 * @param error
	 */
	@OnError
	public void onError(Session session, Throwable error) {
		log.error("Websocket断开连接");
	}

	/**
	 * 实现服务器主动推送
	 */
	public void sendMessage(String message) {
		try {
			this.session.getBasicRemote().sendText(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 群发自定义消息
	 */
	public static void sendMessage(String message, @PathParam("userId") String userId)  {
		
		if(WebplusUtil.isNotEmpty(userId)){
			for (AppPushServer item : webSocketSet) {
				if(item.userId.equals(userId)){
					log.info("推送消息到窗口" + userId + "，推送内容:" + message);
					item.sendMessage(message);
				}
			}
			
		}else{
			for (AppPushServer item : webSocketSet) {
				
					item.sendMessage(message);
				
			}
		}
	}

	public static synchronized int getOnlineCount() {
		return onlineCount;
	}

	public static synchronized void addOnlineCount() {
		AppPushServer.onlineCount++;
	}

	public static synchronized void subOnlineCount() {
		AppPushServer.onlineCount--;
	}
	
}
