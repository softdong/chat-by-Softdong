package com.softdong.chat.entity.realte;

import java.util.List;

import com.softdong.chat.defination.ChatSession;
import com.softdong.chat.entity.basic.ChatRoom;

public class ChatRoomRelateEntity {
	ChatRoom  chatRoom;
	List<ChatSession> users;
	public ChatRoom getChatRoom() {
		return chatRoom;
	}
	public void setChatRoom(ChatRoom chatRoom) {
		this.chatRoom = chatRoom;
	}
	public List<ChatSession> getUsers() {
		return users;
	}
	public void setUsers(List<ChatSession> users) {
		this.users = users;
	}
	

}
