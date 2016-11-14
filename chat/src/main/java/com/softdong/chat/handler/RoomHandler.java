package com.softdong.chat.handler;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.softdong.chat.core.ChatRoomContext;
import com.softdong.chat.core.annotation.Handler;
import com.softdong.chat.core.annotation.MessageHandler;
import com.softdong.chat.core.annotation.Param;
import com.softdong.chat.dao.basic.UserMapper;
import com.softdong.chat.defination.ChatSession;
import com.softdong.chat.entity.basic.ChatRoom;
import com.softdong.chat.entity.basic.User;
import com.softdong.chat.entity.realte.ChatRoomRelateEntity;
import com.softdong.chat.util.JSONUtil;
import com.softdong.chat.util.StringUtil;

@Component
@MessageHandler
public class RoomHandler {
	static Logger logger = Logger.getLogger(RoomHandler.class);
	@Autowired
	private ChatRoomContext chatRoomContext;
	@Autowired
	private UserMapper userMapper;

	@Handler("client_getroomlist")
	public void onMessageGetRoomList(ChatSession session) {

		JSONArray rooms = new JSONArray();
		for (ChatRoomRelateEntity cur : chatRoomContext.getChatRooms()) {
			JSONObject objRoom = new JSONObject();
			objRoom.put("id", cur.getChatRoom().getId());
			objRoom.put("name", cur.getChatRoom().getName());
			objRoom.put("locked",
					!(cur.getChatRoom().getPassword() == null || "".equals(cur.getChatRoom().getPassword())));
			objRoom.put("userCount", cur.getUsers().size());
			objRoom.put("tips", cur.getChatRoom().getTips());
			objRoom.put("icon", cur.getChatRoom().getIcon());
			rooms.add(objRoom);
		}
		// 获取当前聊天室列表
		JSONObject result = JSONUtil.createJSONObject();
		result.put("command", "server_getroomlistresult");
		result.put("success", true);
		result.put("message", "获取聊天室列表成功");
		result.put("roomList", JSONArray.toJSON(rooms));
		session.sendMessage(result.toString());
	}

	@Handler("client_enterroom")
	public void onMessageEnterRoom(ChatSession session, @Param("roomId") String roomId,
			@Param("roomId") String password) {

		ChatRoomRelateEntity room = chatRoomContext.getChatRoomById(roomId);

		// 确认房间号存在
		if (room == null) {
			logger.error("房间ID：" + roomId + "的房间不存在，无法进入");
			session.sendMessage(JSONUtil.createResult("server_enterroomresult", false, "房间不存在，无法进入").toString());
			return;
		}

		// 确认密码正确
		boolean hasPassword = !StringUtil.inNullOrEmpty(room.getChatRoom().getPassword());
		if (hasPassword && !room.getChatRoom().getPassword().equals(password)) {
			logger.error("房间ID：" + roomId + "的房间不存在，无法进入");
			session.sendMessage(JSONUtil
					.createResult("server_enterroomresult", false, "房间" + room.getChatRoom().getName() + "不存在，无法进入")
					.toString());
			return;
		}

		// 判断用户是否已经在房间内了
		for (ChatSession cur : room.getUsers()) {
			if (session.getUser().getId().equals(cur.getUser().getId())) {
				// 不做任何处理，直接返回进入房间成功
				session.sendMessage(JSONUtil
						.createResult("server_enterroomresult", true, "用户已在" + room.getChatRoom().getName() + "房间内。")
						.toString());
				return;
			}
		}

		// 建立用户和房间的关联,相互关联，即通过房间能找到用户，通过用户能找到房间
		room.getUsers().add(session);
		session.setRoom(room.getChatRoom());

		// 相应客户端消息
		session.sendMessage(JSONUtil.createResult("server_enterroomresult", true, "进入房间成功").toString());
		// 给房间的其他人发送消息，更新房间成员列表
		for (ChatSession otherUser : room.getUsers()) {
			if (!otherUser.getUser().getId().equals(session.getUser().getId())) {
				// 直接调用更新房间成员列表的函数
				onMessageGetRoomUsers(otherUser, session.getRoom().getId().toString());
			}
		}

		// 更新数据库用户的最后登陆时间
		User record = new User();
		record.setId(session.getUser().getId());
		record.setLastlogintime(new Date());
		userMapper.updateByPrimaryKeySelective(record);

		logger.info("用户" + session.getUser().getNickname() + "[ID:" + session.getUser().getId() + "]进入了房间"
				+ session.getRoom().getName());

	}

	@Handler("client_exitroom")
	public void onMessageExitRoom(ChatSession session) throws IOException {
		ChatRoom room = session.getRoom();
		// 若用户不在房间内，则直接返回失败
		if (room == null) {
			session.sendMessage(JSONUtil.createResult("server_exitroomresult", false, "用户不在任何房间内，无法离开房间").toString());
			return;
		}

		// 用户和房间相互解除关联
		Iterator<ChatSession> users = chatRoomContext.getChatRoomById(session.getRoom().getId().toString()).getUsers()
				.iterator();
		while (users.hasNext()) {
			ChatSession s = users.next();
			if (s.getUser().getId().equals(session.getUser().getId())) {
				users.remove();
			}
		}
		session.setRoom(null);

		// 响应客户端
		session.sendMessage(JSONUtil.createResult("server_exitroomresult", true, "离开房间成功").toJSONString());

		// 给房间的其他人发送消息，更新房间成员列表
		for (ChatSession otherUser : chatRoomContext.getChatRoomById(session.getRoom().getId().toString()).getUsers()) {
			if (!otherUser.getUser().getId().equals(session.getUser().getId())) {
				// 直接调用更新房间成员列表的函数
				onMessageGetRoomUsers(otherUser, session.getRoom().getId().toString());
			}
		}
		logger.info("用户" + session.getUser().getNickname() + "(" + session.getUser().getId() + ")离开了房间"
				+ session.getRoom().getName());
		session.setRoom(null);
	}

	/**
	 * 用户获取聊天室的成员列表
	 * 
	 * @param session
	 * @param roomId
	 */
	@Handler("client_getroomusers")
	public void onMessageGetRoomUsers(ChatSession session, @Param("roomId") String roomId) {

		// 获取用户列表数据
		JSONArray lstUser = new JSONArray();
		ChatRoomRelateEntity room = chatRoomContext.getChatRoomById(roomId);
		for (ChatSession curUser : room.getUsers()) {
			JSONObject u = new JSONObject();
			u.put("nickname", curUser.getUser().getNickname());
			u.put("id", curUser.getUser().getId().toString());
			lstUser.add(u);
		}
		// 返回客户端聊天室成员列表数据
		JSONObject json = JSONUtil.createResult("server_getroomusersresult", true, "获取房间用户列表成功");
		json.put("userList", lstUser.toJSONString());
		session.sendMessage(json.toString());
	}
}
