package com.noahedu.conmonmodule.inject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @author ding
 * 作用于方法上，被注解的方法将被绑定到target指定的所有控件的指定事件上。
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OnEvent {
	public enum Event {CLICK, LONGCLICK, TOUCH, DRAG, ITEM_CLICK, ITEM_LONGCLICK}
	
	/**
	 * 目标控件id列表
	 * @return
	 */
	int[] target();
	/**
	 * 监控事件类型，默认是onclick事件
	 * @return
	 */
	Event event() default Event.CLICK;
	
	
}
