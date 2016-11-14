<link rel="stylesheet" href="<%= request.getContextPath() %>/css/lobby.css">

<div class="lobby">
	<ul class="chatroomlist">
		
	</ul>
</div>

<div id="template" style="display: none;">
	<div class="room">
		<li>
			<div>
				<img class="roomicon" src="../../img/roomicon.jpg"/>
				<span class ="roomname">聊天室名称</span>
				<span class="roomusercount">在线人数</span>
				<img class="roomlock"/>
			</div>
		</li>
	</div>

</div>


<script src="<%= request.getContextPath() %>/js/lobby.js"></script>