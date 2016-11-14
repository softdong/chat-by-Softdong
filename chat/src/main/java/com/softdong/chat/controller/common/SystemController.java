package com.softdong.chat.controller.common;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
@RequestMapping("/")
public class SystemController {
//	@Resource
//	private IUserService userService;	
	@RequestMapping("index.action")
	public String toIndex(HttpServletRequest request,Model model){
		return "index";
	}
	
	@RequestMapping("login.action")
	public String toLogin(HttpServletRequest request,Model model){
		return "login";
	}
	@RequestMapping("lobby.action")
	public String toLobby(HttpServletRequest request,Model model){
		return "lobby";
	}
	
	@RequestMapping("chatroom.action")
	public String toChatroom(HttpServletRequest request,Model model){
		return "chatroom";
	}
	
	@RequestMapping("register.action")
	public String toRegister(HttpServletRequest request,Model model){
		return "register";
	}
	
	@RequestMapping("register.action")
	@ResponseBody
	public String registerUser(HttpServletRequest request,Model model){
		return "chatroom";
	}
	
	@RequestMapping("nav.action")
	@ResponseBody
	public String toNav(HttpServletRequest request,Model model){
		return "nav";
	}
}
