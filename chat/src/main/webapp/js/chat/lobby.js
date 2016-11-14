
chatClient.removeHandler();
chatClient.registerHandler("server_getroomlistresult",function(data){	
	// 清除当前列表
	$(".chatroomlist li").remove();
	// 添加最新的聊天室数据
	for(var i = 0; i < data.length; i++) {
		var newRoomInfo = $("#template .roomli").clone();
		newRoomInfo.attr("onclick", "chatClient.enterRoom(" +
			data[i].id + ");");
		newRoomInfo.find("img").attr("src",
			root + "/img/roomicon.jpg"); // 显示房间图片
		newRoomInfo.find("span").html(data[i].name); // 显示房间名称
		//显示在线人数
		//显示房间锁状态
		$(".chatroomlist").append(newRoomInfo);
	}
});

chatClient.registerHandler("server_enterroomresult",function(){
	if(json.success) {
		user.roomId = json.roomId;
		window.replaceContent(root + "/chatroom.action",
			function() {
				chatClient.getRoomUserList(user.roomId);
			});
	}
});
chatClient.registerHandler("server_exitroomresult",function(){
	if(json.success) {
		// 替换为网页大厅的页面
		window.replaceContent(root + "/lobby.action");
	}else{
		window.showMessage(json.message);
	}
});
//给列表添加动画效果
$(".contact-box").each(function(){animationHover(this,"pulse")})