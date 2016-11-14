package com.softdong.chat.controller.websocket;

import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import com.softdong.chat.core.ChatRoomContext;
import com.softdong.chat.core.MessageDispatcher;
import com.softdong.chat.defination.ChatSession;
import com.softdong.chat.util.SpringUtil;

@Controller
@ServerEndpoint(value = "/chatcontroller")
public class ChatController {
	static Logger logger = Logger.getLogger(ChatController.class);
	private ChatSession chatSession =new ChatSession();
	ChatRoomContext context;

	public ChatController(){
		context= (ChatRoomContext) SpringUtil.getObject("chatRoomContext");
	}
	/**
	 * 打开连接
	 * 
	 * @param session
	 * @param nickName
	 * @throws IOException
	 */
	@OnOpen
	public void onOpen(Session session) throws IOException {
		chatSession.setWebSocketSession(session);
		logger.info("连接已经建立-ID：" + session.getId());
	}

	/**
	 * 关闭连接
	 * 
	 * @throws IOException
	 */
	@OnClose
	public void onClose() throws IOException {
		
		//如果是非正常关闭，也就是未点击退出登陆按钮，要补发一次退出登陆包
		if(chatSession.getUser()!=null){
			context.clearUserLoginInfo(chatSession);
		}
		logger.info("连接已经关闭");
	}

	/**
	 * 接收信息
	 * 
	 * @param message
	 * @param nickName
	 * @throws IOException
	 */
	@OnMessage
	public void onMessage(String message) throws IOException {
		MessageDispatcher.dispatchMessage(chatSession, message);
	}

	/**
	 * 错误信息响应
	 * 
	 * @param throwable
	 */
	@OnError
	public void onError(Throwable throwable) {
		logger.error(throwable.getMessage());
	}

}
