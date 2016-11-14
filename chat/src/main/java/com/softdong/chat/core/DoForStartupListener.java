package com.softdong.chat.core;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

/**
 * spring 初始化完成之后，立即运行的代码，之后，我的代码开始运行
 * @author softdong
 *
 */
@Service
public class DoForStartupListener implements ApplicationListener<ContextRefreshedEvent> {

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		//applicationContext容器加载完成后，运行的代码
		if (event.getApplicationContext().getParent() == null) {
			//扫描事件处理器
			MessageDispatcher.loadHandler("com.softdong.chat.handler");
		}
	}

}
