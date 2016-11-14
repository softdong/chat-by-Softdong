package com.softdong.chat.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.softdong.chat.controller.websocket.ChatController;
import com.softdong.chat.dao.basic.ChatRoomMapper;
import com.softdong.chat.dao.basic.UserMapper;
import com.softdong.chat.defination.ChatSession;
import com.softdong.chat.defination.CommandType;
import com.softdong.chat.defination.JsonKeyType;
import com.softdong.chat.entity.basic.ChatRoom;
import com.softdong.chat.entity.basic.User;
import com.softdong.chat.service.IMessageService;
import com.softdong.chat.util.JSONUtil;

/**
 * 调用之前先要确保用户登录
 * 
 * @author 限量版大白菜
 *
 */
@Service
public class MessageService implements IMessageService {

	static Logger logger = Logger.getLogger(MessageService.class);
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private ChatRoomMapper chatRoomMapper;

	// 当前的聊天室列表
	private List<ChatRoom> rooms;
	// 当前的聊天室列表对应的用户
	private Map<String, List<ChatSession>> roomusers = new HashMap<String, List<ChatSession>>();

	public MessageService() {

	}

	@Override
	public void onMessageLogin(ChatSession session, String username, String password) throws IOException {
		if (rooms == null) {
			rooms = chatRoomMapper.selectAll();
			// 给每个房间分配成员列表
			for (ChatRoom cur : rooms) {
				roomusers.put(cur.getId().toString(), new ArrayList<ChatSession>());
			}
		}

		// 数据库中检索该用户是否存在
		User user = userMapper.selectByUserName(username);

		JSONObject result = JSONUtil.createJSONObject();
		result.put(JsonKeyType.KEY_CMD.toString(), CommandType.CMD_SERVER_LOGINRESULT.toString());

		if (user == null || !user.getPassword().equals(password)) {
			result.put(JsonKeyType.KEY_SUCCESS.toString(), false);
			result.put(JsonKeyType.KEY_MESSAGE.toString(), "用户名或者密码错误");
		} else {
			// 如果用户在登录状态（在房间内），
			session.setUser(user);
			onMessageExitRoom(session);
			session.setUser(user);
			result.put(JsonKeyType.KEY_SUCCESS.toString(), true);
			result.put(JsonKeyType.KEY_MESSAGE.toString(), "登录成功");
			result.put(JsonKeyType.KEY_NICKNAME.toString(), user.getNickname());
			result.put(JsonKeyType.KEY_USERID.toString(), user.getId());
			
			logger.info("用户" + session.getUser().getNickname() + "(" + session.getUser().getId() + ")已上线");

		}
		session.sendMessage(result.toString());
	}

	@Override
	public void onMessageLogout(ChatSession session) throws IOException {
		// 如果用户在房间内，清除出去
		onMessageExitRoom(session);
		//强制断开连接
		try{
			session.getWebSocketSession().close();
		}catch(IOException e){
			
		}
		logger.info("用户" + session.getUser().getNickname() + "(" + session.getUser().getId() + ")已下线");
	}

	@Override
	public void onMessageGetRoomList(ChatSession session) throws IOException {
		// 获取当前聊天室列表

		JSONObject result = JSONUtil.createJSONObject();
		result.put(JsonKeyType.KEY_CMD.toString(), CommandType.CMD_SERVER_RECVROOMLIST.toString());
		result.put(JsonKeyType.KEY_MESSAGE.toString(), "获取聊天室列表成功");
		result.put(JsonKeyType.KEY_ROOMLIST.toString(), JSONArray.toJSON(rooms));
		session.sendMessage(result.toString());
	}

	@Override
	public void onMessageEnterRoom(ChatSession session, String roomId) throws IOException {

		// 便于给房间发送消息
		roomusers.get(roomId).add(session);
		// 便于知道用户在哪房间
		ChatRoom selectedRoom = null;
		for (ChatRoom curRoom : rooms) {
			if (curRoom.getId().toString().equals(roomId)) {
				selectedRoom = curRoom;
			}
		}
		session.setRoom(selectedRoom);

		JSONObject result = JSONUtil.createResult(CommandType.CMD_SERVER_RECVENTERRESULT.toString(), true, "进入房间成功");
		result.put(JsonKeyType.KEY_ROOMID.toString(), selectedRoom.getId());
		session.sendMessage(result.toString());
		// 给房间的其他人发送消息，更新房间成员列表
		for (ChatSession otherUser : roomusers.get(roomId)) {
			if (!otherUser.getUser().getId().equals(session.getUser().getId())) {
				onMessageGetRoomUsers(otherUser, session.getRoom().getId().toString());
			}
		}
		
		logger.info("用户" + session.getUser().getNickname() + "(" + session.getUser().getId() + ")进入了房间" + session.getRoom().getName());
	}

	@Override
	public void onMessageExitRoom(ChatSession session) throws IOException {
		// 如果不在任何房间可以不进行任何操作
		if (session.getRoom() == null) {
			return;
		}
		Iterator<ChatSession> users = roomusers.get(session.getRoom().getId().toString()).iterator();
		// 利用迭代器房间里面的用户，删除自己
		while (users.hasNext()) {
			ChatSession s = users.next();
			if (s.getUser().getId().equals(session.getUser().getId())) {
				users.remove();
			}
		}
		
		//给客户端发送离开房间的信息
		JSONObject json = JSONUtil.createJSONObject();
		json.put(JsonKeyType.KEY_SUCCESS.toString(), true);
		json.put(JsonKeyType.KEY_CMD.toString(), CommandType.CMD_SERVER_RECVEXITRESULT.toString());
		json.put(JsonKeyType.KEY_MESSAGE.toString(), "已经离开房间");
		try{
			session.sendMessage(json.toString());
		}catch(Exception e){
			
		}
		
		
		// 给房间的其他人发送消息，更新房间成员列表
		for (ChatSession otherUser : roomusers.get(session.getRoom().getId().toString())) {
			onMessageGetRoomUsers(otherUser, session.getRoom().getId().toString());
		}
		
		logger.info("用户" + session.getUser().getNickname() + "(" + session.getUser().getId() + ")离开了房间" + session.getRoom().getName());
		session.setRoom(null);
	}

	@Override
	public void onMessageSendMessage(ChatSession session, String message) throws IOException {
		// 首先获取user所在房间
		for (ChatSession curUser : roomusers.get(session.getRoom().getId().toString())) {
			JSONObject json = JSONUtil.createJSONObject();
			json.put(JsonKeyType.KEY_CMD.toString(), CommandType.CMD_SERVER_RECVMESSAGE.toString());
			json.put(JsonKeyType.KEY_SENDER.toString(), session.getUser().getId().toString());
			json.put(JsonKeyType.KEY_SENDER_NICKNAME.toString(), session.getUser().getNickname().toString());
			json.put(JsonKeyType.KEY_MESSAGE.toString(), message);
			curUser.sendMessage(json.toString());
		}
		logger.info("用户" + session.getUser().getNickname() + "(" + session.getUser().getId() + ")发送了消息:" + message);

	}

	@Override
	public void onMessageGetRoomUsers(ChatSession session, String roomdId) throws IOException {
		List<User> users = new ArrayList<User>();
		for (ChatSession curUser : roomusers.get(roomdId)) {
			users.add(curUser.getUser());
		}

		JSONObject json = JSONUtil.createJSONObject();
		json.put(JsonKeyType.KEY_CMD.toString(), CommandType.CMD_SERVER_RECVUSERLIST.toString());
		json.put(JsonKeyType.KEY_ROOMUSERLIST.toString(), JSONArray.toJSON(users));
		session.sendMessage(json.toString());
	}

}
