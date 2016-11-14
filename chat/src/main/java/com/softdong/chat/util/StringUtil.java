package com.softdong.chat.util;

public class StringUtil {

	/**
	 * 判断字符串是
	 * @param val
	 * @return
	 */
	public static boolean inNullOrEmpty(String val){
		return val == null || "".equals(val);
	}
}
