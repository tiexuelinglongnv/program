package com.zll.program.net;


import com.zll.program.MyApplication;
import com.zll.program.utils.UserPreference;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 网络拦截器
 */

public class HttpInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        //添加消息头
        Request request;
//        if (MyApplication.ServerToken != null)
        request = chain.request()
                .newBuilder()
                .addHeader("Content-Type", "application/json;charset=UTF-8")
                .addHeader("HgAuth", UserPreference.getInstance(MyApplication.getInstance()).gettoken()+"")
                .addHeader("HgSn", UserPreference.getInstance(MyApplication.getInstance()).getDeviceID()+"")
                .build();

//        else
//            request = chain.request()
//                    .newBuilder()
//                    .addHeader("Content-Type", "application/json;charset=UTF-8")
//                    .addHeader("HG_Device", MyApplication.DeviceId)
//                    .build();
        // try the request
        Response originalResponse = chain.proceed(request);
        int code = originalResponse.code();

        //根据和服务端的约定判断token过期:自动刷新token
        if (code == 401) {
//            Intent intent = new Intent();
//            intent.setClass(MyApplication.mContext, LoginActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            MyApplication.mContext.startActivity(intent);
//            Sp_Utils.clearLogin();
//            PostUtils.refreshToken(null);
        }
        return originalResponse;
    }
}
