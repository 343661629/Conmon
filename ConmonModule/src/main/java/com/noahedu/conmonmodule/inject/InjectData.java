package com.noahedu.conmonmodule.inject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InjectData {
    private int contentView = -1;
    private Map<Field, Integer> viewMap;//
    private List<Method> eventMethodList;//
    private Map<Field, PojoInfo> pojoFieldMap;//
    boolean init = false;

    public int getContentView() {
        return contentView;
    }

    public void setContentView(int contentView) {
        this.contentView = contentView;
    }

    public Map<Field, Integer> getViewMap() {
        return viewMap;
    }

    public void setViewMap(Map<Field, Integer> viewMap) {
        this.viewMap = viewMap;
    }

    public Map<Field, PojoInfo> getPojoFieldMap() {
        return pojoFieldMap;
    }

    public void setPojoFieldMap(Map<Field, PojoInfo> pojoFieldMap) {
        this.pojoFieldMap = pojoFieldMap;
    }

    public List<Method> getEventMethodList() {
        return eventMethodList;
    }

    public void putView(Field field, int view) {
        if (viewMap == null) {
            viewMap = new HashMap<Field, Integer>();
        }
        viewMap.put(field, view);
    }

    public int getView(Field field) {
        if (viewMap == null) {
            return -1;
        }
        Integer i = viewMap.get(field);
        if (i == null) {
            return -1;
        } else {
            return i;
        }
    }

    public void addEventMethod(Method method) {
        if (eventMethodList == null) {
            eventMethodList = new ArrayList<Method>();
        }
        eventMethodList.add(method);
    }


    public PojoInfo getPojoInfo(Field field) {
        if (pojoFieldMap == null) {
            return null;
        }
        return pojoFieldMap.get(field);
    }

    public void putPojoInfo(Field field, PojoInfo info) {
        if (pojoFieldMap == null) {
            pojoFieldMap = new HashMap<Field, PojoInfo>();
        }
        pojoFieldMap.put(field, info);
    }

    public boolean isInit() {
        return init;
    }

    public void setInit(boolean init) {
        this.init = init;
    }
}
