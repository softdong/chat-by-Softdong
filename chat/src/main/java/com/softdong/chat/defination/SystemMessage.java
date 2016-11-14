package com.softdong.chat.defination;

public enum SystemMessage {
	LOGOUT_TIPS("已经下线了"),
	LOGIN_TIPS("已经上线了"),
	LOGIN_FAILED("登陆失败，请确认你的用户名和密码");
		
	private String message;
	SystemMessage(String msg){
		this.message=msg;
	}
	
	public String toString(){
		return message;
	}
}
