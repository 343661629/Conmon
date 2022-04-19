package com.noahedu.conmonmodule.inject;

public class PojoInfo {
	private Object singleton;
	private boolean isSingleton;
	private Class<?> fieldType;
	
	public PojoInfo() {}
	public PojoInfo(Object singleton, boolean isSingleton, Class<?> fieldType) {
		super();
		this.singleton = singleton;
		this.isSingleton = isSingleton;
		this.fieldType = fieldType;
	}
	
	
	public Object getSingleton() {
		return singleton;
	}
	public void setSingleton(Object singleton) {
		this.singleton = singleton;
	}
	public boolean isSingleton() {
		return isSingleton;
	}
	public void setSingleton(boolean isSingleton) {
		this.isSingleton = isSingleton;
	}
	public Class<?> getFieldType() {
		return fieldType;
	}
	public void setFieldType(Class<?> fieldType) {
		this.fieldType = fieldType;
	}

}
