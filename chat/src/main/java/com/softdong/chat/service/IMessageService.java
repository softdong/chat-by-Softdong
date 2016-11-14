package com.softdong.chat.service;

import java.io.IOException;

import com.softdong.chat.defination.ChatSession;

public interface IMessageService {

	public void onMessageLogin(ChatSession session,String username,String password) throws IOException;
	
	public void onMessageLogout(ChatSession session) throws IOException;
	
	public void onMessageGetRoomList(ChatSession session) throws IOException;
	
	public void onMessageEnterRoom(ChatSession session,String roomId) throws IOException;
	
	public void onMessageGetRoomUsers(ChatSession session,String roomdId)throws IOException;
	
	public void onMessageExitRoom(ChatSession session) throws IOException;
	
	public void onMessageSendMessage(ChatSession session,String message) throws IOException;
}
