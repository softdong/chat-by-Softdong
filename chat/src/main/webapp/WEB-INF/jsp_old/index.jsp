<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>聊天室</title>

<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="css/font-awesome.min.css?v=4.4.0" rel="stylesheet">
<link href="css/animate.min.css" rel="stylesheet">
<link href="css/style.min.css" rel="stylesheet">
<link href="css/login.min.css" rel="stylesheet">

<script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/index.css">

<script src="<%=request.getContextPath()%>/js/chatclient.js"></script>
<script src="<%=request.getContextPath()%>/js/index.js"></script>
<!--[if lt IE 8]>
    	<meta http-equiv="refresh" content="0;ie.html" />
    <![endif]-->
<script>
	var root = "/chat";
	if (window.top !== window.self) {
		window.top.location = window.location
	};
</script>
</head>

<body>
<div class="main_content" style="height: 100%;"></div>
<!-- <div class="bb" style="height: 100%;background:red;"></div> -->
</body>

</html>