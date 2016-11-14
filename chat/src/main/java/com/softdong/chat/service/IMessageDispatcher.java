package com.softdong.chat.service;

import java.io.IOException;

import com.softdong.chat.defination.ChatSession;

public interface IMessageDispatcher {
	public void dispachMessage(ChatSession session,String data) throws IOException;
	public void clearLoginInfo(ChatSession session)throws IOException;
	
}
