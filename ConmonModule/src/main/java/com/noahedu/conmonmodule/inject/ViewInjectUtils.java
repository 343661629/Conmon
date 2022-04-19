package com.noahedu.conmonmodule.inject;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;

import com.socks.library.KLog;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ViewInjectUtils {
    public static Map<Class<?>, InjectData> injectDataMap = new HashMap<Class<?>, InjectData>();


    /**
     * activity页面
     * @param activity
     */
    public static void inject(Activity activity) {
        InjectData injectData = injectDataMap.get(activity.getClass());
        if (injectData == null) {
            injectData = new InjectData();
            injectDataMap.put(activity.getClass(), injectData);

        }

        injectContentView(activity, injectData);
        injectViewsAndPojo(activity, injectData);
        injectEvents(activity, activity, injectData);

        injectData.setInit(true);
    }

    public static View inject(Fragment fragment, LayoutInflater inflater, ViewGroup container) {
        InjectData injectData = injectDataMap.get(fragment.getClass());
        if (injectData == null) {
            injectData = new InjectData();
            injectDataMap.put(fragment.getClass(), injectData);

        }

        View view = injectContentView(fragment, inflater, container, injectData);
        if (view != null) {
            injectViewsAndPojo(fragment, view, injectData);
            injectEvents(fragment, view, injectData);
        }
        return view;

    }


    /**
     * fragment页面
     * @param fragment
     * @param inflater
     * @param container
     * @return
     */
    public static View inject(android.support.v4.app.Fragment fragment, LayoutInflater inflater, ViewGroup container) {
        InjectData injectData = injectDataMap.get(fragment.getClass());
        if (injectData == null) {
            injectData = new InjectData();
            injectDataMap.put(fragment.getClass(), injectData);
        }

        View view = injectContentView(fragment, inflater, container, injectData);
        if (view != null) {
            injectViewsAndPojo(fragment, view, injectData);
            injectEvents(fragment, view, injectData);
        }
        return view;

    }

    /**
     * 注入所有的控件
     *
     * @param activity
     */
    private static void injectViewsAndPojo(Activity activity,
                                           InjectData injectData) {
        if (!injectData.isInit()) {
            Class<? extends Activity> clazz = activity.getClass();
            Field[] fields = clazz.getDeclaredFields();
            boolean autoInjectViews = clazz.getAnnotation(AutoInjectView.class) != null;
            // 遍历所有成员变量
            for (Field field : fields) {
                try {

                    int viewId = injectData.getView(field);
                    if (viewId == -1) {
                        InjectView viewInjectAnnotation = field
                                .getAnnotation(InjectView.class);
                        if (viewInjectAnnotation != null) {
                            viewId = viewInjectAnnotation.value();
                            injectData.putView(field, viewId);
                            field.setAccessible(true);
                        } else if (autoInjectViews
                                && View.class.isAssignableFrom(field.getType())) {
                            /**
                             * 如果自动注入视图控件，且当前field的类型是View的子类，则根据字段名去R.
                             * id中去找到对应的id常量。 因此，如果需要自动注入的字段，其字段名和布局文件中的id名必须一致
                             */

                            String viewName = field.getName();

                            viewId = activity.getResources().getIdentifier(viewName, "id", activity.getPackageName());
                            // TODO 作为一个框架而言，这里引入了项目里面的R类，显然是不通用的。下一步考虑剥离的问题。
                            //viewId = r.getDeclaredField(viewName).getInt(null);
                            injectData.putView(field, viewId);
                            field.setAccessible(true);
                        }
                    }

                    PojoInfo pojoinfo = injectData.getPojoInfo(field);
                    if (pojoinfo == null) {
                        Inject pojoInject = field.getAnnotation(Inject.class);
                        if (pojoInject != null) {
                            Class<?> fieldClazz = field.getType();
                            Singleton singleton = fieldClazz
                                    .getAnnotation(Singleton.class);
                            Object fieldValue = null;
                            if (singleton != null) {
                                fieldValue = fieldClazz.newInstance();
                            }
                            pojoinfo = new PojoInfo(fieldValue,
                                    singleton != null, fieldClazz);
                            injectData.putPojoInfo(field, pojoinfo);
                            field.setAccessible(true);
                        }
                    }
                } catch (Exception e) {
                    KLog.e("inject field fail:class=" + clazz.getSimpleName()
                            + ",field=" + field.getName(), e.getMessage());
                }

            }

        }

        if (injectData.getViewMap() != null) {
            for (Field field : injectData.getViewMap().keySet()) {
                int viewId = injectData.getView(field);
                View resView = activity.findViewById(viewId);
                try {
                    field.set(activity, resView);
                } catch (Exception e) {
                    KLog.e("inject field fail:field=" + field.getName(), e.getMessage());
                }
            }
        }

        if (injectData.getPojoFieldMap() != null) {
            for (Field field : injectData.getPojoFieldMap().keySet()) {
                PojoInfo info = injectData.getPojoInfo(field);
                Object fieldValue = info.getSingleton();
                try {
                    if (fieldValue == null) {
                        // 不是singleton，需要创建新对象
                        fieldValue = info.getFieldType().newInstance();
                    }
                    field.set(activity, fieldValue);
                } catch (Exception e) {
                   KLog.e("inject field fail:field=" + field.getName(), e.getMessage());
                }
            }
        }

    }

    private static void injectViewsAndPojo(Object fragment, View view, InjectData injectData) {
        if (!injectData.isInit()) {
            Class<?> clazz = fragment.getClass();
            Field[] fields = clazz.getDeclaredFields();
            boolean autoInjectViews = clazz.getAnnotation(AutoInjectView.class) != null;

            Context mContext = null;
            if (fragment instanceof Fragment) {
                mContext = ((Fragment) fragment).getActivity();
            } else if (fragment instanceof android.support.v4.app.Fragment) {
                mContext = ((android.support.v4.app.Fragment) fragment).getActivity();
            }
            // 遍历所有成员变量
            for (Field field : fields) {
                try {

                    int viewId = injectData.getView(field);
                    if (viewId == -1) {
                        InjectView viewInjectAnnotation = field
                                .getAnnotation(InjectView.class);
                        if (viewInjectAnnotation != null) {
                            viewId = viewInjectAnnotation.value();
                            injectData.putView(field, viewId);
                            field.setAccessible(true);
                        } else if (autoInjectViews && View.class.isAssignableFrom(field.getType())) {
                            /**
                             * 如果自动注入视图控件，切当前field的类型是View的子类，则根据字段名去R.id中去找到对应的id常量。
                             * 因此，如果需要自动注入的字段，其字段名和布局文件中的id名必须一致
                             */

                            String viewName = field.getName();

                            viewId = mContext.getResources().getIdentifier(viewName, "id", mContext.getPackageName());
                            // TODO 作为一个框架而言，这里引入了项目里面的R类，显然是不通用的。下一步考虑剥离的问题。
                            injectData.putView(field, viewId);
                            field.setAccessible(true);
                        }
                    }


                    PojoInfo pojoinfo = injectData.getPojoInfo(field);
                    if (pojoinfo == null) {
                        Inject pojoInject = field.getAnnotation(Inject.class);
                        if (pojoInject != null) {
                            Class<?> fieldClazz = field.getType();
                            Singleton singleton = fieldClazz.getAnnotation(Singleton.class);
                            Object fieldValue = null;
                            if (singleton != null) {
                                fieldValue = fieldClazz.newInstance();
                            }
                            pojoinfo = new PojoInfo(fieldValue, singleton != null, fieldClazz);
                            injectData.putPojoInfo(field, pojoinfo);
                            field.setAccessible(true);
                        }
                    }
                } catch (Exception e) {
                    KLog.e( "inject field fail:field=" + field.getName(), e.getMessage());
                }

            }


        }
        if (injectData.getViewMap() != null) {
            for (Field field : injectData.getViewMap().keySet()) {
                int viewId = injectData.getView(field);
                View resView = view.findViewById(viewId);
                try {
                    field.set(fragment, resView);
                } catch (Exception e) {
                    KLog.e("inject field fail:field=" + field.getName(), e.getMessage());
                }
            }
        }

        if (injectData.getPojoFieldMap() != null) {
            for (Field field : injectData.getPojoFieldMap().keySet()) {
                PojoInfo info = injectData.getPojoInfo(field);
                Object fieldValue = info.getSingleton();
                try {
                    if (fieldValue == null) {
                        //不是singleton，需要创建新对象
                        fieldValue = info.getFieldType().newInstance();
                    }
                    field.set(fragment, fieldValue);
                } catch (Exception e) {
                    KLog.e("inject field fail:field=" + field.getName(), e.getMessage());
                }
            }
        }
    }


    /**
     * 注入所有的事件
     */
    private static void injectEvents(Object hostObject, Object viewParent,
                                     InjectData injectData) {
        if (!injectData.isInit()) {
            Class<?> clazz = hostObject.getClass();
            Method[] methods = clazz.getDeclaredMethods();
            // 遍历所有成员变量
            for (Method method : methods) {
                OnEvent onEvent = method.getAnnotation(OnEvent.class);
                if (onEvent != null) {
                    injectData.addEventMethod(method);
                }
            }
        }

        if (injectData.getEventMethodList() != null) {
            for (Method method : injectData.getEventMethodList()) {
                OnEvent event = method.getAnnotation(OnEvent.class);
                EventListener listener = new EventListener(hostObject, method);
                switch (event.event()) {
                    case CLICK:
                        for (int viewId : event.target()) {
                            View view = findViewById(viewParent, viewId);
                            if (hostObject instanceof OnClickListener) {
                                view.setOnClickListener((OnClickListener) hostObject);
                            } else {
                                view.setOnClickListener(listener);
                            }
                        }
                        break;
                    case DRAG:
                        for (int viewId : event.target()) {
                            View view = findViewById(viewParent, viewId);
                            if (hostObject instanceof OnDragListener) {
                                view.setOnDragListener((OnDragListener) hostObject);
                            } else {
                                view.setOnDragListener(listener);
                            }
                        }
                        break;
                    case ITEM_CLICK:
//                         for(int viewId:event.target()) {
//                         View listView = findViewById(viewParent, viewId);
//                         OnItemClickListener finalListener = hostObject instanceof
//                         OnItemClickListener ? ((OnItemClickListener)hostObject) :
//                         listener;
//                         if(listView instanceof AbsListView) {
//                         ((AbsListView)listView).setOnItemClickListener(finalListener);
//                         } else if (listView instanceof
//                         PullToRefreshAdapterViewBase) {
//                         ((PullToRefreshAdapterViewBase<?>)listView).setOnItemClickListener(finalListener);
//                         } else {
//                         LogUtils.e(TAG,
//                         "Can't bind a onitemclick event to a non-listview");
//                         }
//
//                         }
                        break;
                    case ITEM_LONGCLICK:
                        // for(int viewId:event.target()) {
                        // View listView = findViewById(viewParent, viewId);
                        // OnItemLongClickListener finalListener = hostObject
                        // instanceof OnItemLongClickListener ?
                        // ((OnItemLongClickListener)hostObject) : listener;
                        // if(listView instanceof AbsListView) {
                        // ((AbsListView)listView).setOnItemLongClickListener(finalListener);
                        // } else if (listView instanceof
                        // PullToRefreshAdapterViewBase) {
                        // ((PullToRefreshAdapterViewBase<?>)listView).setOnItemLongClickListener(finalListener);
                        // } else {
                        // LogUtils.e(TAG,
                        // "Can't bind a onitemlongclick event to a non-listview");
                        // }
                        // }
                        break;
                    case LONGCLICK:
                        for (int viewId : event.target()) {
                            OnLongClickListener finalListener = hostObject instanceof OnLongClickListener ? ((OnLongClickListener) hostObject)
                                    : listener;

                            findViewById(viewParent, viewId)
                                    .setOnLongClickListener(finalListener);
                        }
                        break;
                    case TOUCH:
                        for (int viewId : event.target()) {
                            OnTouchListener finalListener = hostObject instanceof OnTouchListener ? ((OnTouchListener) hostObject)
                                    : listener;

                            findViewById(viewParent, viewId).setOnTouchListener(
                                    finalListener);
                        }
                        break;

                }
            }
        }
    }

    private static View findViewById(Object hostObject, int viewId) {
        if (hostObject instanceof Activity) {
            return ((Activity) hostObject).findViewById(viewId);
        } else if (hostObject instanceof View) {
            return ((View) hostObject).findViewById(viewId);
        } else {
            return null;
        }
    }

    /**
     * 注入主布局文件
     *
     * @param activity
     */
    private static void injectContentView(Activity activity,
                                          InjectData injectData) {
        if (injectData.getContentView() == -1) {
            Class<? extends Activity> clazz = activity.getClass();
            // 查询类上是否存在ContentView注解
            ContentView contentView = clazz.getAnnotation(ContentView.class);
            if (contentView != null)// 存在
            {
                int contentViewLayoutId = contentView.value();
                injectData.setContentView(contentViewLayoutId);
            }
        }

        if (injectData.getContentView() != 1) {
            activity.setContentView(injectData.getContentView());
        }
    }


    private static View injectContentView(Object fragment, LayoutInflater inflater, ViewGroup container, InjectData injectData) {
        if (injectData.getContentView() == -1) {
            Class<?> clazz = fragment.getClass();
            // 查询类上是否存在ContentView注解
            ContentView contentView = clazz.getAnnotation(ContentView.class);
            if (contentView != null)// 存在
            {
                int contentViewLayoutId = contentView.value();
                injectData.setContentView(contentViewLayoutId);
            }
        }

        if (injectData.getContentView() != 1) {
            return inflater.inflate(injectData.getContentView(), container, false);
        }
        return null;
    }


    private static void injectViews(Object viewHolder, View view,
                                    InjectData injectData, Context context) {
        if (!injectData.isInit()) {
            Class<?> clazz = viewHolder.getClass();
            Field[] fields = clazz.getDeclaredFields();
            boolean autoInjectViews = clazz.getAnnotation(AutoInjectView.class) != null;
            // 遍历所有成员变量
            for (Field field : fields) {
                try {
                    int viewId = injectData.getView(field);
                    if (viewId == -1) {
                        InjectView viewInjectAnnotation = field
                                .getAnnotation(InjectView.class);
                        if (viewInjectAnnotation != null) {
                            viewId = viewInjectAnnotation.value();
                            injectData.putView(field, viewId);
                            field.setAccessible(true);
                        } else if (autoInjectViews
                                && View.class.isAssignableFrom(field.getType())) {
                            /**
                             * 如果自动注入视图控件，切当前field的类型是View的子类，则根据字段名去R.
                             * id中去找到对应的id常量。 因此，如果需要自动注入的字段，其字段名和布局文件中的id名必须一致
                             */

                            String viewName = field.getName();

                            viewId = context.getResources().getIdentifier(viewName, "id", context.getPackageName());
                            // TODO 作为一个框架而言，这里引入了项目里面的R类，显然是不通用的。下一步考虑剥离的问题。
                            //viewId = r.getDeclaredField(viewName).getInt(null);
                            injectData.putView(field, viewId);
                            field.setAccessible(true);
                        }
                    }
                } catch (Exception e) {
                    KLog.e("inject field fail:class=" + clazz.getSimpleName()
                            + ",field=" + field.getName(), e.getMessage());
                }

            }

        }
        if (injectData.getViewMap() != null) {
            for (Field field : injectData.getViewMap().keySet()) {
                int viewId = injectData.getView(field);
                View resView = view.findViewById(viewId);
                try {
                    field.set(viewHolder, resView);
                } catch (Exception e) {
                   KLog.e("inject field fail:field=" + field.getName(), e.getMessage());
                }
            }
        }
    }

    public static void injectViewHolder(View convertView, Object holder, Context context) {
        InjectData injectData = injectDataMap.get(holder.getClass());
        if (injectData == null) {
            injectData = new InjectData();
            injectDataMap.put(holder.getClass(), injectData);

        }
        injectViews(holder, convertView, injectData, context);
    }
}
