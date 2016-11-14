package com.softdong.chat.core;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softdong.chat.dao.basic.ChatRoomMapper;
import com.softdong.chat.defination.ChatSession;
import com.softdong.chat.entity.basic.ChatRoom;
import com.softdong.chat.entity.realte.ChatRoomRelateEntity;

/**
 * 房间的信息，房间和用户的关系都保存在这里面，任何用户的操作都被保存在这里面
 * @author Administrator
 *
 */
@Component
public class ChatRoomContext implements InitializingBean{
	
	private List<ChatRoomRelateEntity> chatRooms;

	@Autowired
	private ChatRoomMapper chatRoomMapper;

	public List<ChatRoomRelateEntity> getChatRooms() {
		return chatRooms;
	}

	public void setChatRooms(List<ChatRoomRelateEntity> chatRooms) {
		this.chatRooms = chatRooms;
	}
	
	public ChatRoomRelateEntity getChatRoomById(String id){
		for(ChatRoomRelateEntity cur:chatRooms){
			if(cur.getChatRoom() !=null && cur.getChatRoom().getId().equals(id)){
				return cur;
			}
		}
		return null;
	}
	
	public void clearUserLoginInfo(ChatSession session){
		if(session!=null && session.getUser()!=null && session.getUser().getId()!=null){
			//遍历所有房间的所有用户，如果存在该用户，则移除
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		chatRooms = new ArrayList<ChatRoomRelateEntity>();
		//查询基本的mapper
		List<ChatRoom> rooms = chatRoomMapper.selectAll();
		
		//转化为业务对象,刚启动的时候，上下文只存房间信息
		for(ChatRoom item:rooms){
			chatRooms.add(toChatRoomRelateEntity(item));
		}
		
		 
	}
	
	private ChatRoomRelateEntity toChatRoomRelateEntity(ChatRoom r){
		ChatRoomRelateEntity entity =new ChatRoomRelateEntity();
		entity.setChatRoom(r);
		entity.setUsers(new ArrayList<ChatSession>());
		return entity;
	}
	
	 
	
}
