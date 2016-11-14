function isEmpty(str) {
	return(str == null || str == "" || str == undefined);
}

function login() {
	var username = $("input[name='username']").val();
	var password = $("input[name='password']").val();

	//确认有效性
	if(isEmpty(username) || isEmpty(password)) {
		showMessage("请输入完整输入用户名和密码");
		return;
	}

	//进行websocket连接
	
	chatClient.login(username, password);
	//保证表单不被提交
	return false;

}