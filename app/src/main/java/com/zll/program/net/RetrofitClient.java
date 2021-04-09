package com.zll.program.net;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zll.program.BuildConfig;
import com.zll.program.utils.UserPreference;

import java.io.File;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.goldze.mvvmhabit.http.cookie.CookieJarImpl;
import me.goldze.mvvmhabit.http.cookie.store.PersistentCookieStore;
import me.goldze.mvvmhabit.http.interceptor.BaseInterceptor;
import me.goldze.mvvmhabit.http.interceptor.CacheInterceptor;
import me.goldze.mvvmhabit.http.interceptor.logging.Level;
import me.goldze.mvvmhabit.http.interceptor.logging.LoggingInterceptor;
import me.goldze.mvvmhabit.utils.KLog;
import me.goldze.mvvmhabit.utils.Utils;
import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author zll
 * @class name：
 * @class describe
 * @time
 * @change
 * @chang time
 * @class describe
 */
public class RetrofitClient implements IGlobalManager {
    //超时时间
    private static final int DEFAULT_TIMEOUT = 20;


    //缓存时间
    private static final int CACHE_TIMEOUT = 10 * 1024 * 1024;
    //服务端根路径
    public static String baseUrl = BuildConfig.ServerPath;;
    private static Context mContext = Utils.getContext();
    private static OkHttpClient okHttpClient;
    private static Retrofit retrofit;

    private Cache cache = null;
    private File httpCacheDirectory;
    @Override
    public void exitLogin() {

    }

    private static class SingletonHolder {
        private static RetrofitClient INSTANCE = new RetrofitClient();
    }

    public static RetrofitClient getInstance() {

        return SingletonHolder.INSTANCE;
    }

    private RetrofitClient() {
        this(baseUrl, null);
    }
    private RetrofitClient(String url, Map<String, String> headers) {

        if (TextUtils.isEmpty(url)) {
            url = baseUrl;
        }
        if (httpCacheDirectory == null) {
            httpCacheDirectory = new File(mContext.getCacheDir(), "goldze_cache");
        }

        try {
            if (cache == null) {
                cache = new Cache(httpCacheDirectory, CACHE_TIMEOUT);
            }
        } catch (Exception e) {
            KLog.e("Could not create http cache", e);
        }
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

//        builder .cookieJar(new CookieJarImpl(new PersistentCookieStore(mContext)))
                builder.addInterceptor(new BaseInterceptor(headers))
                .addInterceptor(new CacheInterceptor(mContext))
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .addInterceptor(new HttpInterceptor())
                .addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.i("RetrofitClient","HttpLoggingInterceptor>>>  " + message);
            }
        }).setLevel(HttpLoggingInterceptor.Level.BASIC).setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE))
                .build();;
//        builder .addInterceptor(new LoggingInterceptor
//                .Builder()//构建者模式
//                .loggable(BuildConfig.DEBUG) //是否开启日志打印
//                .setLevel(Level.BASIC) //打印的等级
//                .log(Platform.INFO) // 打印类型
//                .request("Request") // request的Tag
//                .addHeader("app-v", "2.6")
//                .addHeader("v", "3.0")//系统版本号
//                .addHeader("device", "android")//设备信息
//                .addHeader("Content-Type","application/json;charset=UTF-8")
//                .addHeader("HgAuth", UserPreference.getInstance(mContext).gettoken()+"")
//                .addHeader("HgSn", UserPreference.getInstance(mContext).getDeviceID()+"")
//                .response("Response")// Response的Tag
//                .addHeader("log-header", "I am the log request header.") // 添加打印头, 注意 key 和 value 都不能是中文
//                .build()
//        );

        //   }

        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(8, 15, TimeUnit.SECONDS));
        Gson gson = new GsonBuilder()
                .setLenient()  // 设置GSON的非严格模式setLenient()
                .create();
        retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(url)
                .build();

    }

    /**
     * create you ApiService
     * Create an implementation of the API endpoints defined by the {@code service} interface.
     */
    public <T> T create(final Class<T> service) {
        if (service == null) {
            throw new RuntimeException("Api service is null!");
        }
        return retrofit.create(service);
    }

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> tClass) {
        T t = retrofit.create(tClass);
        return (T) Proxy.newProxyInstance(tClass.getClassLoader(), new Class<?>[] { tClass }, new ProxyHandler(t, this));
    }


    /**
     * /**
     * execute your customer API
     * For example:
     * MyApiService service =
     * RetrofitClient.getInstance(MainActivity.this).create(MyApiService.class);
     * <p>
     * RetrofitClient.getInstance(MainActivity.this)
     * .execute(service.lgon("name", "password"), subscriber)
     * * @param subscriber
     */

    public static <T> T execute(Observable<T> observable, Observer<T> subscriber) {
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

        return null;
    }
}
