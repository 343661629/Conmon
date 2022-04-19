package com.noahedu.conmonmodule.inject;

import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.noahedu.utils.KLog;

import java.lang.reflect.Method;


/**
 * @author ding
 * 各种事件的监听器，相应的onXX被调用时，直接调用handler的method。method是注解获取的，handler是当前的activity或者fragment。
 *
 */
public class EventListener implements OnClickListener, OnLongClickListener, OnTouchListener, OnDragListener, OnItemClickListener, OnItemLongClickListener {
	private Object handler;
	private Method method;
	
	public EventListener(Object handler, Method method) {
		super();
		this.handler = handler;
		this.method = method;
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		try {
			return (Boolean)method.invoke(handler, parent, view, position, id);
		} catch (Exception e) {
			KLog.e("inject invoke method failed.", e.getMessage());
			return false;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		try {
			method.invoke(handler, parent, view, position, id);
		} catch (Exception e) {
			KLog.e("inject invoke method failed.", e.getMessage());
		}
	}

	@Override
	public boolean onDrag(View v, DragEvent event) {
		try {
			return (Boolean)method.invoke(handler, v, event);
		} catch (Exception e) {
			KLog.e("inject invoke method failed.", e.getMessage());
			return false;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		try {
			boolean result = (Boolean)method.invoke(handler, v, event);
			v.performClick();
			return result;
		} catch (Exception e) {
			KLog.e("inject invoke method failed.", e.getMessage());
			return false;
		}
	}

	@Override
	public boolean onLongClick(View v) {
		try {
			return (Boolean)method.invoke(handler, v);
		} catch (Exception e) {
			KLog.e("inject invoke method failed.", e.getMessage());
			return false;
		}
	}

	@Override
	public void onClick(View v) {
		try {
			method.invoke(handler, v);
		} catch (Exception e) {
			KLog.e("inject invoke method failed.", e.getMessage());
		}
	}

}
