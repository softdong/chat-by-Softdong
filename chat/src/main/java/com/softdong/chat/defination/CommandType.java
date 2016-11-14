package com.softdong.chat.defination;

/**
 * 命令的类型，是客户端和服务器的交换协议
 * @author 董俊庆
 *
 */
public enum CommandType {
	
	//以下是服务器发给客户端的命令
	CMD_SERVER_LOGINRESULT("server_loginresult"),
	CMD_SERVER_RECVMESSAGE("server_recvmessage"),
	CMD_SERVER_RECVINFO("server_recvinfo"),
	CMD_SERVER_RECVROOMLIST("server_recvroomlist"),
	CMD_SERVER_RECVENTERRESULT("server_enterresult"),
	CMD_SERVER_RECVEXITRESULT("server_exitresult"),
	CMD_SERVER_RECVUSERLIST("server_recvuserlist"),
	//以下是客户端发给服务器的命令
	CMD_CLIENT_GETROOMUSER("client_getroomuser"),
	CMD_CLIENT_GETROOMLIST("client_getroomlist"),
	CMD_CLIENT_LOGIN("client_login"),
	CMD_CLIENT_LOGOUT("client_logout"),
	CMD_CLIENT_SENDMESSAGE("client_sendmessage"),
	CMD_CLIENT_CREATEROOM("client_createroom"),
	CMD_CLIENT_ENTERROOM("client_enterroom"),
	CMD_CLIENT_EXITROOM("client_exitroom");
	
		
	private String command;
	CommandType(String cmd){
		this.command=cmd;
	}
	
	public String toString(){
		return command;
	}
}
