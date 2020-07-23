package com.noahedu.conmon;

import java.util.ArrayList;

public class CallBackManager {

    public static ArrayList<ICallBack> sICallBacks = new ArrayList<>();

    public static void addCallBack(ICallBack ICallBack) {
        sICallBacks.add(ICallBack);
    }

    public static void removeCallBack(ICallBack ICallBack) {
        sICallBacks.remove(ICallBack);
    }

}
