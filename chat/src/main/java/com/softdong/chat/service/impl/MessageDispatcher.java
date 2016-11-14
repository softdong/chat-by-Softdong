package com.softdong.chat.service.impl;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.softdong.chat.defination.ChatSession;
import com.softdong.chat.defination.CommandType;
import com.softdong.chat.defination.JsonKeyType;
import com.softdong.chat.service.IMessageDispatcher;
import com.softdong.chat.service.IMessageService;

/**
 * 消息传递中心，把消息转化为相应的事件处理程序
 * 
 * 优待优化项目，传入数据的数据校验，若数据不满足要求则返回错误信息
 * 
 * @author 限量版大白菜
 *
 */
@Service
public class MessageDispatcher implements IMessageDispatcher {

	@Autowired
	private IMessageService messageService;

	@Override
	public void dispachMessage(ChatSession session, String data) throws IOException {
		JSONObject json = JSONObject.parseObject(data);
		String cmd = json.getString(JsonKeyType.KEY_CMD .toString());

		// 登录系统
		if (CommandType.CMD_CLIENT_LOGIN.toString().equals(cmd)) {
			messageService.onMessageLogin(session, json.getString(JsonKeyType.KEY_USERNAME.toString()), json.getString(JsonKeyType.KEY_PASSWORD.toString()));
		}

		// 获取列表
		if (CommandType.CMD_CLIENT_GETROOMLIST.toString().equals(cmd)) {
			messageService.onMessageGetRoomList(session);
		}

		// 进入房间
		if (CommandType.CMD_CLIENT_ENTERROOM.toString().equals(cmd)) {
			messageService.onMessageEnterRoom(session, json.getString(JsonKeyType.KEY_ROOMID.toString()));
		}
		//获取房间成员信息
		
		if (CommandType.CMD_CLIENT_GETROOMUSER.toString().equals(cmd)) {
			messageService.onMessageGetRoomUsers(session, json.getString(JsonKeyType.KEY_ROOMID.toString()));
		}
		
		// 发送信息
		if (CommandType.CMD_CLIENT_SENDMESSAGE.toString().equals(cmd)) {
			messageService.onMessageSendMessage(session, json.getString(JsonKeyType.KEY_MESSAGE.toString()));
		}

		// 离开房间
		if (CommandType.CMD_CLIENT_EXITROOM.toString().equals(cmd)) {
			messageService.onMessageExitRoom(session);
		}

		// 退出登录
		if (CommandType.CMD_CLIENT_LOGOUT.toString().equals(cmd)) {
			messageService.onMessageLogout(session);
		}
	}

	@Override
	public void clearLoginInfo(ChatSession session) throws IOException {
		messageService.onMessageLogout(session);
	}
}
