package com.softdong.chat.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.softdong.chat.core.annotation.Handler;
import com.softdong.chat.core.annotation.MessageHandler;
import com.softdong.chat.core.annotation.Param;
import com.softdong.chat.defination.ChatSession;
import com.softdong.chat.util.ClassScanUtil;
import com.softdong.chat.util.SpringUtil;

public class MessageDispatcher {
	static Logger logger = Logger.getLogger(MessageDispatcher.class);
	private static Map<String, CommandInvoker> commandHandlers = new HashMap<String, CommandInvoker>();

	// 保存command调用的
	private static class CommandInvoker {
		private String command;
		private Object handler;
		private Method method;

		public String getCommand() {
			return command;
		}

		public void setCommand(String command) {
			this.command = command;
		}

		public Object getHandler() {
			return handler;
		}

		public void setHandler(Object handler) {
			this.handler = handler;
		}

		public Method getMethod() {
			return method;
		}

		public void setMethod(Method method) {
			this.method = method;
		}

		public void invoke(ChatSession session, JSONObject data) {
			if (handler == null || method == null) {
				logger.error("未指定命令处理器，无法执行");
				return;
			}
			try {
				//在这里，获取方法的参数定义，并把响应的参数传递进去，其他的东西全部定义是字符串
				Parameter[] ps =method.getParameters();
				List<Object> params= new ArrayList<Object>();
				for(Parameter cur:ps){
					//如果是ChatSession类型，则直接传入
					if(cur.getType().equals(ChatSession.class)){
						params.add(session);
						continue;
					}
					//获取对应名字的数据
					String keyname = "";
					Param anno = cur.getAnnotation(Param.class);
					if(anno!=null){
						keyname =anno.value();
					}else{
						logger.warn("函数"+handler.getClass().getName()+"."+method.getName()+"含有未定义的Param注解的参数，这类参数的值将无法被传递");
						continue;
					}
					
					String d = (String) data.get(keyname);
					if(d!=null){
						//其他类型的数据，调用相应的构造函数构造,构造的时候采用带一个字符串的参数
						@SuppressWarnings("rawtypes")
						Constructor con= cur.getType().getConstructor(String.class);
						Object o= con.newInstance(d);
						params.add(o);
					}else{
						params.add(null);
					}
				}
				//参数传入
				method.invoke(handler, params.toArray());
			} catch (Exception e) {
				logger.error("消息处理器的函数必须定义为 function(ChatSession,...)类型");
			}
		}
	}

	public static void loadHandler(String handlerPackage) {
		if (handlerPackage == null || "".equals(handlerPackage)) {
			logger.error("要扫描的包路径无效，请重新指定");
			return;
		}
		// 扫描包下面所有的class
		Set<Class<?>> cla = ClassScanUtil.scanClasses(handlerPackage);

		Iterator<Class<?>> ite = cla.iterator();
		while (ite.hasNext()) {
			Class<?> c = ite.next();
			// 判断类是否具有MessageHandler注解
			if (c.isAnnotationPresent(MessageHandler.class)) {
				// 获得类名
				String keyclass = c.getName().split("\\.")[c.getName().split("\\.").length - 1];
				// 类名首字母小写
				keyclass = keyclass.replaceFirst(keyclass.substring(0, 1), keyclass.substring(0, 1).toLowerCase());

				MessageHandler cur = (MessageHandler) c.getAnnotation(MessageHandler.class);
				Method[] ms = c.getMethods();
				for (Method curMethod : ms) {
					if (curMethod.isAnnotationPresent(Handler.class)) {
						String keycmd = curMethod.getAnnotation(Handler.class).value();
						if (!"".equals(keycmd)) {
							// 先判断当前列表是否存在同名消息处理器
							if (null != commandHandlers.get(keycmd)) {
								logger.error("Command:" + keycmd + " 已经存在消息处理器，现存消息处理器将被覆盖");
							}
							// 创建调用器，并添加到消息处理器列表
							CommandInvoker caller = new CommandInvoker();
							caller.setCommand(keycmd);
							caller.setHandler(SpringUtil.getObject(keyclass));
							caller.setMethod(curMethod);
							commandHandlers.put(keycmd, caller);
							// 打印日志־
							logger.info("command:[" + curMethod.getAnnotation(Handler.class).value() + "] 已经映射到方法  ["
									+ c.getName() + "." + curMethod.getName() + "]");
						} else {
							logger.warn("Method [" + c.getName() + "." + curMethod.getName() + "] 未定义要处理的消息命令，忽略注解");
						}

					}
				}
			}
		}

	}

	public static void dispatchMessage(ChatSession session, String message) {
		// 转化为json对象
		JSONObject objMsg = null;
		try {
			objMsg = JSONObject.parseObject(message);
		} catch (Exception e) {
			logger.error("Json解析失败，请确认json格式是否正确，json：" + message);
			return;
		}
		// 获取json对象中的命令
		String cmd = (String) objMsg.get("command");
		if (cmd == null || "".equals(cmd)) {
			logger.warn("Json中不含有command字段，无法进行处理，json：" + message);
			return;
		}
		// 调用相应的处理器
		CommandInvoker caller = commandHandlers.get(cmd);
		if (caller == null) {
			logger.warn("请求的Command未找到消息处理器，将被忽略，json：" + message);
			return;
		}
		caller.invoke(session, objMsg);
	}

}
