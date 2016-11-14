<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div id="chatroomcontent">
	<div id="personPanel" class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title">房间成员</h3>
		</div>


	</div>

	<div id="chatRoom" class="panel panel-primary">
		<div class="panel-heading" style="height:4vh">
			<h3 class="panel-title">聊天</h3>
		</div>
		<ul id="lstChatWord" style="position: relative;height:62vh;overflow:auto;background:#eee;">
			
		</ul>
		<div class="input-group custom_group" style="height:4vh">
			<input id="txtMessage" type="text" class="form-control"
				placeholder=""> <span class="input-group-btn">
				<button class="btn btn-default" type="submit"
					onclick="sendMessage()">发送</button>
			</span>
		</div>
	</div>
</div>

<ul id="templateLi" style="display: none;">
	<li class="worditem left">
		<div>昵称</div> <span>左边的信息</span>
	</li>
	<li class="worditem right">
		<div>昵称</div> <span>右边的信息</span>
	</li>

</ul>


<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/chatroom.css">
<script src="<%=request.getContextPath()%>/js/chatroom.js"></script>
