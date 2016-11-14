package com.softdong.chat.defination;

import java.io.IOException;

import javax.websocket.Session;

import org.apache.log4j.Logger;

import com.softdong.chat.core.MessageDispatcher;
import com.softdong.chat.entity.basic.ChatRoom;
import com.softdong.chat.entity.basic.User;

/**
 * 聊天会话
 * @author 董俊庆
 *
 */
public class ChatSession {
	static Logger logger = Logger.getLogger(ChatSession.class);
	private Session webSocketSession;
	private User user;
	private ChatRoom room;
	
	public ChatRoom getRoom() {
		return room;
	}
	public void setRoom(ChatRoom room) {
		this.room = room;
	}
	public Session getWebSocketSession() {
		return webSocketSession;
	}
	public void setWebSocketSession(Session webSocketSession) {
		this.webSocketSession = webSocketSession;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public void sendMessage(String msg){
		if(webSocketSession != null){
			try {
				webSocketSession.getBasicRemote().sendText(msg);
			} catch (IOException e) {
				logger.error("发送消息失败,连接可能已经被关闭，消息内容"+msg);
				e.printStackTrace();
			}
		}
	}
	
}
