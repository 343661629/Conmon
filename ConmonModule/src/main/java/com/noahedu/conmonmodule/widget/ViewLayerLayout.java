package com.noahedu.conmonmodule.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.noahedu.conmonmodule.R;


/**
 * Created by huangjialin on 2018/4/25.
 * 数据为空，无网络，服务器异常，加载中占位图
 */

public class ViewLayerLayout extends FrameLayout {

    public static final int LAYER_LOADING = 0;   //加载中

    public static final int LAYER_CONTENT = 1;  //内容层

    public static final int LAYER_EMPTY = 2;   //返回数据为空

    public static final int LAYER_SERVER_ERROR = 3;  //请求失败的情况，如服务器出现异常

    public static final int LAYER_NO_NETWORK = 4;   //无网络

    protected View loadingView;

    protected View contentView;

    protected View emptyView;

    protected View serverErrorView;

    protected View noNetworkView;

    protected OnReloadListener onReloadListener;

    protected OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (onReloadListener != null) {
                setViewLayer(LAYER_LOADING);
                onReloadListener.onReload();
            }
        }
    };


    /**
     * 对外提供监听器，如可以点击空白处重新加载
     */
    public void setOnReloadListener(OnReloadListener onReloadListener) {
        this.onReloadListener = onReloadListener;
    }


    public ViewLayerLayout(@NonNull Context context) {
        super(context);
    }

    public ViewLayerLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewLayerLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * 指定显示某层视图，并隐藏其他层
     *
     * @param layer
     */
    public void setViewLayer(int layer) {
        View view = getView(layer);

        boolean isFound = false;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child == view) {
                isFound = true;
            } else {
                child.setVisibility(View.GONE);
            }
        }
        if (!isFound) {
            addView(view);
        }
        view.setVisibility(View.VISIBLE);
    }


    /**
     * 拿到对应的图层
     */
    protected View getView(int layer) {
        View view = null;
        switch (layer) {
            case LAYER_LOADING: //加载中
                view = getLoadingView();
                break;
            case LAYER_CONTENT: //内容层
                view = getContentView();
                break;
            case LAYER_EMPTY:  //数据为空层
                view = getEmptyView();
                break;
            case LAYER_SERVER_ERROR: //服务器异常层
                view = getServerErrorView();
                break;
            case LAYER_NO_NETWORK: //无网络层
                view = getNoNetworkView();
                break;
        }
        return view;
    }


    /**
     * 获取加载中层
     *
     * @return
     */
    public View getLoadingView() {
        if (loadingView == null) {
            loadingView = inflate(getContext(), R.layout.layout_loading,
                    null);
            loadingView.setVisibility(View.GONE);
        }
        return loadingView;
    }


    /**
     * 获取展示内容层
     *
     * @return
     */
    public View getContentView() {
        if (contentView == null && getChildCount() > 0) {
            contentView = getChildAt(0);
        }
        return contentView;
    }


    /**
     * 获取数据为空层
     */
    public View getEmptyView() {
        if (emptyView == null) {
            emptyView = inflate(getContext(), R.layout.layout_empty, null);
        }
        return emptyView;
    }

    /**
     * 如果个别页面的数据为空层要求不一样，可以通过该方法进行设置，不然即使用默认的布局
     */
    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
    }


    /**
     * 拿到服务器异常层
     */
    public View getServerErrorView() {
        if (serverErrorView == null) {
            serverErrorView = inflate(getContext(),
                    R.layout.layout_server_error, null);
            serverErrorView.findViewById(R.id.reload)
                    .setOnClickListener(onClickListener);
        }
        return serverErrorView;
    }


    /**
     * 获取无网络
     */
    public View getNoNetworkView() {
        if (noNetworkView == null) {
            noNetworkView = inflate(getContext(),
                    R.layout.layout_neterror, null);
            noNetworkView.findViewById(R.id.reload)
                    .setOnClickListener(onClickListener);
        }
        return noNetworkView;
    }


    public interface OnReloadListener {
        void onReload();
    }


}
