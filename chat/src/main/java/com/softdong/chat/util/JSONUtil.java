package com.softdong.chat.util;

import com.alibaba.fastjson.JSONObject;

public class JSONUtil {

	public static JSONObject createJSONObject(){
		return new JSONObject();
	}
	
	public static JSONObject createResult(String cmd,boolean success,String message){
		JSONObject result = new JSONObject();
		result.put("command", cmd);
		result.put("success", success);
		result.put("message", message);
		return result;
	}
}
