var STATE_LOGIN = 0;
var STATE_LOBBY = 1;
var STATE_CHATROOM = 2;

function ChatClient(url) {

	// 关于通信方式的问题，同步和异步的问题，现在暂时采用异步的方式
	// 初始化websocket
	this.url = url;
	this.ws = undefined;
	var self = this;
	this.user= {};
	this.user.username = "";
	this.user.password = "";
	this.isConnected =false;
	this.state = STATE_LOGIN;
	this.messageHandler = {};
	this.commonMessageHandler= {};
	
	this.onOpen = function() {
		console.log("已经建立与服务器的连接");
		this.isConnected = true;
		var json = {
				command : "client_login",
				username : this.user.username,
				password : this.user.password
			}

		this.ws.send(JSON.stringify(json));
		
	};
	this.onClose = function() {
		this.ws = undefined;
		this.isConnected = false;
		console.log("已经断开与服务器的连接");
		
		window.showMessage("连接已经断开，请重新连接服务器");
		window.history.go(0);
	};
	this.onError = function(message) {
		window.showMessage(message);
	};

	//消息分发器
	this.onMessage = function(event) {
		var json = eval("(" + event.data + ")");
		// 解析命令，分发函数
		var cmd = json.command;
		// 登录结果
		if(cmd){
			if(this.messageHandler.hasOwnProperty(cmd)){
				this.messageHandler[cmd].call(this,json);
			}
			if(this.commonMessageHandler.hasOwnProperty(cmd)){
				this.commonMessageHandler[cmd].call(this,json);
			}
		}
	};

	// 自带函数，用于主动给服务端发送消息
	this.connect = function() {
		// 创新新的websocket连接
		this.ws = new WebSocket(this.url);
		//封装基本的消息处理器	
		this.ws.onopen = function() {
			self.onOpen();
		}
		this.ws.onerror = function(data) {
			self.onError(data);
		}
		this.ws.onmessage = function(event) {
			self.onMessage(event);
		}
		this.ws.onclose = function() {
			self.onClose();
		}

	}

	this.login = function(username, password) {
		//建立连接
		this.user.username = username;
		this.user.password = password;
		this.connect();
	}

	this.close = function() {
		if (this.ws != undefined) {
			this.ws.close();
		}
	}

	this.getRoomList = function() {
		var json = {
			command : CMD_CLIENT_GETROOMLIST
		}
		this.ws.send(JSON.stringify(json));
	}

	this.enterRoom = function(roomId) {
		var json = {
			command : CMD_CLIENT_ENTERROOM,
			roomId : roomId
		}
		this.ws.send(JSON.stringify(json));
	}

	this.exitRoom = function() {
		var json = {
			command : CMD_CLIENT_EXITROOM
		}
		this.ws.send(JSON.stringify(json));
	};

	this.getRoomUserList = function(roomId) {
		var json = {
			command : CMD_CLIENT_GETROOMUSER,
			roomId : roomId
		}
		this.ws.send(JSON.stringify(json));
	};

	this.sendMessage = function(msg) {
		var json = {
			command : "client_sendmessage",
			message : msg
		}
		this.ws.send(JSON.stringify(json));
	};

	// 接收函数，需要用户自己定义，用来接收用户发送的消息

	/**
	 * 注册消息处理事件,这里注册的是私有事件，和页面有很强的关联性，在页面切换的过程中应该removeHandler进行移除
	 */
	this.registerHandler = function(name,handler){
		this.messageHandler[name] = handler;
	};
	
	/**
	 * 注册全局事件处理器，此事件处理器一般在
	 */
	this.registerCommonHandler = function(name,handler){
		this.commonMessageHandler[name] = handler;
	};
	

	this.removeHandler= function(name){
		//如果不指定handler名称的情况下，移除所有handler
		if(name == undefined){
			this.messageHandler = {};
			return;
		}
		if(this.messageHandler.hasOwnProperty(name)){
			delete this.messageHandler[name];
		}
	};
	this.removeCommonHandler= function(name){
		//如果不指定handler名称的情况下，移除所有handler
		if(name == undefined){
			this.commonMessageHandler = {};
			return;
		}
		if(this.commonMessageHandler.hasOwnProperty(name)){
			delete this.commonMessageHandler[name];
		}
	};

}