package com.softdong.chat.handler;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.softdong.chat.core.ChatRoomContext;
import com.softdong.chat.core.annotation.Handler;
import com.softdong.chat.core.annotation.MessageHandler;
import com.softdong.chat.core.annotation.Param;
import com.softdong.chat.dao.basic.UserMapper;
import com.softdong.chat.defination.ChatSession;
import com.softdong.chat.entity.basic.User;
import com.softdong.chat.util.JSONUtil;

@Component
@MessageHandler
public class LoginHandler {
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private ChatRoomContext chatRoomContext;
	
	static Logger logger = Logger.getLogger(LoginHandler.class);
	
	@Handler("client_login")
	public void login(ChatSession session,
			@Param("username")String username,
			@Param("password")String password){
		//校验数据格式是否正确
		// 数据库中检索该用户是否存在
		User user = userMapper.selectByUserName(username);

		JSONObject result = JSONUtil.createJSONObject();
		result.put("command", "server_loginresult");

		if (user == null || !user.getPassword().equals(password)) {
			result.put("success", false);
			result.put("message", "用户名或者密码错误");
		} else {
			// 如果用户在登录状态（在房间内），
			session.setUser(user);
			chatRoomContext.clearUserLoginInfo(session);
			result.put("success", true);
			result.put("message", "登录成功");
			result.put("nickname", user.getNickname());
			result.put("userId", user.getId());
			
			logger.info("用户" + session.getUser().getNickname() + "[ID:" + session.getUser().getId() + "]已上线");
		}
		session.sendMessage(result.toString());
	}
	
	@Handler("client_logout")
	public void logout(ChatSession session){
		// 如果用户在房间内，清除出去
		chatRoomContext.clearUserLoginInfo(session);
		//服务器主动强制断开连接，客户端会引发断开连接的消息
		try{
			session.getWebSocketSession().close();
		}catch(IOException e){
			
		}
		logger.info("用户" + session.getUser().getNickname() + "[ID:" + session.getUser().getId() + "]已下线");
	}
}
