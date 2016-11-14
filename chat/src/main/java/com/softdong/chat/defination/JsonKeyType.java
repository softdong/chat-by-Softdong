package com.softdong.chat.defination;

public enum JsonKeyType {
	KEY_USERID("userId"),
	KEY_SENDER_NICKNAME("sender_nickname"),
	KEY_SUCCESS("success"),
	KEY_NICKNAME("nickname"),
	KEY_SENDER("sender"),
	KEY_ROOMUSERLIST("roomUserList"),
	KEY_ROOMLIST("roomList"),
	KEY_CMD("command"),
	KEY_USERNAME("username"),
	KEY_PASSWORD("password"),
	KEY_ROOMID("roomId"),
	KEY_MESSAGE("message");
	

	private String key;

	JsonKeyType(String key) {
		this.key = key;
	}

	public String toString() {
		return key;
	}
}
