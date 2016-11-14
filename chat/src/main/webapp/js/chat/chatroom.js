function sendMessage() {
	if($("#txtMessage").val() != "") {
		chatClient.sendMessage($("#txtMessage").val());
		$("#txtMessage").val("");
	}

}

$("#txtMessage").keydown(function(event) {
	if(event.which == 13) // 13等于回车键(Enter)键值,ctrlKey 等于 Ctrl
		sendMessage();
});

chatClient.removeHandler();

chatClient.registerHandler("server_getroomusersresult", function(data) {
	// 先删除当前用户数据
	if($("#personPanel button").size() > 0) {
		$("#personPanel button").remove();
	}

	// 再将所有成员信息添加进去
	for(var i = 0; i < json.userList.length; i++) {
		$("#personPanel").append(
			'<button type="button" class="list-group-item">' +
			data.userList[i].nickname +
			'</button>');
	}
});
hatClient.registerHandler("server_recvmessage", function(data) {
	var li = undefined;
	if(data.sender == user.userId) {
		li = $("#templateLi li:last").clone();
	} else {

		li = $("#templateLi li:first").clone();
	}
	li.children("div").html(data.sender_nickname);
	li.children("span").html(data.message);
	$("#lstChatWord").append(li);
	$("#lstChatWord").scrollTop(10000000);
});