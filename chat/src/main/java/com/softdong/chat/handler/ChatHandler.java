package com.softdong.chat.handler;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.softdong.chat.core.ChatRoomContext;
import com.softdong.chat.core.annotation.Handler;
import com.softdong.chat.core.annotation.MessageHandler;
import com.softdong.chat.core.annotation.Param;
import com.softdong.chat.defination.ChatSession;
import com.softdong.chat.util.JSONUtil;

@Component
@MessageHandler
public class ChatHandler {
	static Logger logger = Logger.getLogger(ChatHandler.class);
	@Autowired
	private ChatRoomContext chatRoomContext;
	
	@Handler("client_sendmessage")
	public void onMessageSend(ChatSession session, @Param("message") String message) {
		// 首先获取user所在房间
		for (ChatSession curUser : chatRoomContext.getChatRoomById(session.getRoom().getId().toString()).getUsers()) {
			JSONObject json = JSONUtil.createJSONObject();
			json.put("command", "server_recvmessage");
			json.put("senderId", session.getUser().getId().toString());
			json.put("nickname", session.getUser().getNickname().toString());
			json.put("message", message);
			curUser.sendMessage(json.toString());
		}
		logger.info("用户" + session.getUser().getNickname() + "[ID:" + session.getUser().getId() + "]发送了消息:" + message);
	}
}
