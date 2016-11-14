
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<link href="<%=request.getContextPath()%>/css/chat/login.css"
	rel="stylesheet">
<div class="signinpanel">
	<div class="row">
		<div class="col-sm-7 color_black">
			<div class="signin-info">
				<div class="logopanel m-b">
					<h1 class="">聊天室</h1>
				</div>
				<div class="m-b color_black"></div>
				<h4 class="color_black">
					欢迎使用 <strong> 聊天室</strong>
				</h4>
				<ul class="m-b color_black">
					<li><i class="fa fa-arrow-circle-o-right m-r-xs"></i> 美观的界面风格</li>
					<li><i class="fa fa-arrow-circle-o-right m-r-xs"></i> 优势二</li>
					<li><i class="fa fa-arrow-circle-o-right m-r-xs"></i> 优势三</li>
					<li><i class="fa fa-arrow-circle-o-right m-r-xs"></i> 优势四</li>
					<li><i class="fa fa-arrow-circle-o-right m-r-xs"></i> 优势五</li>
				</ul>
				<strong class="color_black">还没有账号？ <a href="#">立即注册&raquo;</a></strong>
			</div>
		</div>
		<div class="col-sm-5 color_black">
			<form method="post" onsubmit="return login();">
				<h4 class="no-margins">登录：</h4>
				<p class="m-t-md">登录到 聊天室</p>
				<input type="text" name="username" class="form-control uname"
					placeholder="用户名" /> <input type="password" name="password"
					class="form-control pword m-b" placeholder="密码" /> <input
					type="checkbox">记住密码 <a href="" class="lostpwd">忘记密码了？</a>
				<button class="btn btn-success btn-block">登录</button>
			</form>
		</div>
	</div>
	<div class="signup-footer">
		<div class="pull-left">&copy; 2016 All Rights Reserved. softdong
		</div>
	</div>
</div>

<script src="<%=request.getContextPath()%>/js/chat/login.js"></script>