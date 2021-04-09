/*
 * Copyright (C) 2016 david.wei (lighters)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zll.program.net;

import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.zll.program.MyApplication;
import com.zll.program.base.BaseBean;
import com.zll.program.base.ReLogin;
import com.zll.program.net.exception.TokenInvalidException;
import com.zll.program.net.exception.TokenNotExistException;
import com.zll.program.ui.login.LoginActivity;
import com.zll.program.utils.Contents;
import com.zll.program.utils.SystemUtil;
import com.zll.program.utils.UserPreference;
import com.zll.program.utils.Utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import retrofit2.http.Body;
import retrofit2.http.Query;

/**
 * Created by david on 16/8/21.
 * Email: huangdiv5@gmail.com
 * GitHub: https://github.com/alighters
 */
public class ProxyHandler implements InvocationHandler {

    private final static String TAG = "Token_Proxy";

    private final static String TOKEN = "accountId";

    private final static int REFRESH_TOKEN_VALID_TIME = 30;
    private static long tokenChangedTime = 0;
    private final String accountId;
    private Throwable mRefreshTokenError = null;
    private boolean mIsTokenNeedRefresh;

    private Object mProxyObject;
    private IGlobalManager mGlobalManager;

    public ProxyHandler(Object proxyObject, IGlobalManager globalManager) {
        mProxyObject = proxyObject;
        mGlobalManager = globalManager;
        accountId = UserPreference.getInstance(MyApplication.getInstance().getBaseContext()).getAccountId();
    }

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
//        return Observable.just(null).flatMap(new Func1<Object, Observable<?>>() {
//            @Override
//            public Observable<?> call(Object o) {
//                try {
//                    try {
//                        if (mIsTokenNeedRefresh) {
//                            updateMethodToken(method, args);
//                        }
//                        return (Observable<?>) method.invoke(mProxyObject, args);
//                    } catch (InvocationTargetException e) {
//                        e.printStackTrace();
//                    }
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//                return null;
//            }
//        }).retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {
//            @Override
//            public Observable<?> call(Observable<? extends Throwable> observable) {
//                return observable.flatMap(new Func1<Throwable, Observable<?>>() {
//                    @Override
//                    public Observable<?> call(Throwable throwable) {
//                        if (throwable instanceof TokenInvalidException) {
//                            return refreshTokenWhenTokenInvalid();
//                        } else if (throwable instanceof TokenNotExistException) {
//                            // Token 不存在，执行退出登录的操作。（为了防止多个请求，都出现 Token 不存在的问题，
//                            // 这里需要取消当前所有的网络请求）
//                            mGlobalManager.exitLogin();
//                            return Observable.error(throwable);
//                        }
//                        return Observable.error(throwable);
//                    }
//                });
//            }
//        });

        return Observable.just(true).flatMap(new Function<Object, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(Object o) throws Exception {
                try {
                    try {
                        if (mIsTokenNeedRefresh) {
                            updateMethodToken(method, args);
                        }
                        return (Observable<?>) method.invoke(mProxyObject, args);
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }).retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Exception {
                return throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Throwable throwable) throws Exception {
                        if (throwable instanceof TokenInvalidException) {
                            return refreshTokenWhenTokenInvalid();
                        } else if (throwable instanceof TokenNotExistException) {
                            // Token 不存在，执行退出登录的操作。（为了防止多个请求，都出现 Token 不存在的问题，
                            // 这里需要取消当前所有的网络请求）
                            mGlobalManager.exitLogin();
                            return Observable.error(throwable);
                        }
                        return Observable.error(throwable);
                    }
                });
            }
        });


    }

    /**
     * Refresh the token when the current token is invalid.
     *
     * @return Observable
     */
    private Observable<?> refreshTokenWhenTokenInvalid() {
        synchronized (ProxyHandler.class) {
            // Have refreshed the token successfully in the valid time.
            if (new Date().getTime() - tokenChangedTime < REFRESH_TOKEN_VALID_TIME) {
                mIsTokenNeedRefresh = true;
                return Observable.just(true);
            } else {
                HashMap<String, Object> map = new HashMap<>();
                map.put("accountId", UserPreference.getInstance(MyApplication.getInstance().getBaseContext()).getAccountId() + "");
                map.put("appName", Contents.APPNAME);
                map.put("brand", SystemUtil.getDeviceBrand() + "");
                // map.put("channel", Contents.CHANNEL);
                map.put("deviceModel", SystemUtil.getSystemModel() + "");
                // map.put("deviceType", "ANDROID");
                map.put("uuid", SystemUtil.getIMEI(MyApplication.getInstance().getBaseContext()) + "");
                map.put("token", UserPreference.getInstance(MyApplication.getInstance().getBaseContext()).gettoken() + "");
                map.put("mobile", UserPreference.getInstance(MyApplication.getInstance().getBaseContext()).getMobile() + "");
                map.put("version", Utils.getAppVersionCode(MyApplication.getInstance().getBaseContext()));
                RetrofitClient.getInstance().create(ApiService.class).getNewAccountId(map)
                        .subscribe(new Consumer<BaseBean<String>>() {
                            @Override
                            public void accept(BaseBean<String> stringBaseBean) throws Exception {
                                if (stringBaseBean.getData() != null) {
                                    mIsTokenNeedRefresh = true;
                                    tokenChangedTime = new Date().getTime();
                                    UserPreference.getInstance(MyApplication.getInstance().getBaseContext()).setaccountId(stringBaseBean.getData());
                                }else {
                                    if(stringBaseBean.getCode()==401){
                                        SharedPreferences setting = MyApplication.getInstance().getBaseContext().getSharedPreferences("firstOpen", 0);
                                        SharedPreferences.Editor edit = setting.edit();
                                        edit.putBoolean("isLogin", false);
                                        edit.commit();
                                        MyApplication.getInstance().getBaseContext().startActivity(new Intent(MyApplication.getInstance().getBaseContext(), LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("isLogin", false));

                                        mRefreshTokenError=new ReLogin(new Throwable("请重新登录"),401);
                                    }
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                mRefreshTokenError = throwable;
                            }
                        });
                if (mRefreshTokenError != null) {
                    return Observable.error(mRefreshTokenError);
                } else {
                    return Observable.just(true);
                }
            }
        }
    }

    /**
     * Update the token of the args in the method.
     * <p>
     * PS： 因为这里使用的是 GET 请求，所以这里就需要对 Query 的参数名称为 token 的方法。
     * 若是 POST 请求，或者使用 Body ，自行替换。因为 参数数组已经知道，进行遍历找到相应的值，进行替换即可（更新为新的 token 值）。
     */
    private void updateMethodToken(Method method, Object[] args) {
        if (mIsTokenNeedRefresh && !TextUtils.isEmpty(UserPreference.getInstance(MyApplication.getInstance().getBaseContext()).getAccountId())) {
            Annotation[][] annotationsArray = method.getParameterAnnotations();
            Annotation[] annotations;
            if (annotationsArray != null && annotationsArray.length > 0) {
                for (int i = 0; i < annotationsArray.length; i++) {
                    annotations = annotationsArray[i];
                    for (Annotation annotation : annotations) {
                        if (annotation instanceof Query) {
                            if (TOKEN.equals(((Query) annotation).value())) {
                                args[i] =UserPreference.getInstance(MyApplication.getInstance().getBaseContext()).getAccountId();
                            }
                        }else if(annotation instanceof Body){
                            HashMap<String, Object> arg1 = (HashMap<String, Object>) args[i];
                            arg1.put(TOKEN,UserPreference.getInstance(MyApplication.getInstance().getBaseContext()).getAccountId());
                            args[i]=arg1;

                        }
                    }
                }
            }
            mIsTokenNeedRefresh = false;
        }
    }
    public static String gbEncoding(final String gbString) {
        char[] utfBytes = gbString.toCharArray();
        String unicodeBytes = "";
        for (int i = 0; i < utfBytes.length; i++) {
            String hexB = Integer.toHexString(utfBytes[i]);
            if (hexB.length() <= 2) {
                hexB = "00" + hexB;
            }
            unicodeBytes = unicodeBytes + "\\u" + hexB;
        }
        return unicodeBytes;
    }
}
