var chatClient;

// 定义消息提示函数
window.showMessage = window.alert;
function replaceContent(url) {
	$.ajax({
		type : "GET",
		url : url,
		async : true,
		dataType : 'html',
		success : function(data) {
			$("body").html(data);
		},
		error : function(XmlHttpRequest, txtStatus, errorThrown) {

		}
	});
}

$(document).ready(function() {
	// 加载登录页面
	replaceContent(root + "/login.action");
	chatClient = new ChatClient("ws://127.0.0.1:8080/chat/chatcontroller");
	// 设定处理函数
	chatClient.registerHandler("server_loginresult", function(data) {
		if (data.success) {
			// 更新顶部导航栏的信息
			$("#nickname").html(data.nickname);
			this.user.userId = data.userId;
			this.user.nickname = data.nickname;
			// 替换为网页大厅的页面
			// this.replaceContent待测试 预期是找不到函数
			window.replaceContent(root + "/lobby.action");
		} else {
			showMessage(data.message);
			this.close();
		}
	});
});
