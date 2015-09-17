package com.yxl.demo.quartzProject.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

/**
 * spring初始化时,添加spring上下文,以便手工获取bean
 * @author xiaolong.yuanxl
 *
 * 
 */
public class SpringBeanHelper{

	private static  ApplicationContext context;
	
	/**
	 * 手工获取bean
	 */
	public static Object getBean(String beanId) {
		return context.getBean(beanId);
	}
	
	
	public static void setApplicationContext(ApplicationContext ctx)
			throws BeansException {
		context = ctx;
	}

}
