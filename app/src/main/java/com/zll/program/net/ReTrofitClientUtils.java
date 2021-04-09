package com.zll.program.net;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.zll.program.base.BaseBean;
import com.zll.program.base.ReLogin;
import com.zll.program.ui.login.LoginActivity;
import com.zll.program.utils.Contents;
import com.zll.program.utils.SystemUtil;
import com.zll.program.utils.UserPreference;
import com.zll.program.utils.Utils;

import java.util.HashMap;

import androidx.fragment.app.Fragment;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.http.ResponseThrowable;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * Created by Administrator on 2019/3/21.
 */

public class ReTrofitClientUtils {

    public static <T> void get(final Observable baseBeanObservable, final BaseViewModel viewModel, final HttpInterFaceClass httpInterFace) {

        if (!Utils.isNetWorkConn(viewModel.getApplication())) {
            ToastUtils.showShort("请检查网络是否正常");
            return;
        }
        baseBeanObservable
                .compose(RxUtils.bindToLifecycle(viewModel.getLifecycleProvider())) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        httpInterFace.startDialog();
                    }
                })
                .subscribe(new Consumer<BaseBean<T>>() {
                    @Override
                    public void accept(BaseBean<T> loginBeanBaseBean) throws Exception {
                        httpInterFace.dismissloading();
                        if (loginBeanBaseBean.getCode() == 0) {
                            httpInterFace.success(loginBeanBaseBean);
                        } else if (loginBeanBaseBean.getCode() == 401) {
                            SharedPreferences setting = viewModel.getApplication().getApplicationContext().getSharedPreferences("firstOpen", 0);
                            SharedPreferences.Editor edit = setting.edit();
                            edit.putBoolean("isLogin", false);
                            edit.commit();
                            viewModel.getApplication().getApplicationContext().startActivity(new Intent(viewModel.getApplication().getApplicationContext(), LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("isLogin", false));
                        } else {
                            ToastUtils.showShort(loginBeanBaseBean.getCode() == 00000 ? "请求失败" : loginBeanBaseBean.getMessage());

                        }
                    }
                }, new Consumer<ResponseThrowable>() {
                    @Override
                    public void accept(ResponseThrowable throwable) throws Exception {
                        ToastUtils.showShort(throwable.message);
                        httpInterFace.dismissloading();
                        httpInterFace.error(throwable);
                        throwable.printStackTrace();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        httpInterFace.dismissloading();
                        //请求刷新完成收回
                    }
                });
    }


//    public static <T> void get(final Observable baseBeanObservable, final Context lifecycle, final HttpInterFaceClass httpInterFace) {
//        if (!Utils.isNetWorkConn(lifecycle)) {
//            ToastUtils.showShort("请检查网络是否正常");
//            return;
//        }
//
//        baseBeanObservable
////                .flatMap(httpresult -> {
////                    try {
////                        BaseBean httpresult1 = (BaseBean) httpresult;
////                        if (httpresult1.getErrorCode() != null && httpresult1.getErrorCode().contains("9990")) {
////                            return Observable.error(new Throwable("RetryWithNewAccountid"));
////                        }
////                    } catch (Exception e) {
////                        return Observable.just(httpresult);
////                    }
////                    return Observable.just(httpresult);
////                })
////                .retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {
////                    @Override
////                    public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Exception {
////                        return throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {
////                            @Override
////                            public ObservableSource<?> apply(Throwable throwable) throws Exception {
////                                if (throwable.getMessage().equals("RetryWithNewAccountid")) {
////                                    return refreshTokenWhenTokenInvalid(lifecycle);
////                                } else {
////                                    return Observable.error(throwable);
////                                }
////                            }
////                        }).subscribeOn(Schedulers.io());
////                    }
////                })
//                .compose(RxUtils.bindToLifecycle(lifecycle)) //请求与View周期同步
//                .compose(RxUtils.schedulersTransformer()) //线程调度
//                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
//                .doOnSubscribe(new Consumer<Disposable>() {
//                    @Override
//                    public void accept(Disposable disposable) throws Exception {
//                        httpInterFace.startDialog();
//                    }
//                })
//                .subscribe(new Consumer<BaseBean<T>>() {
//                    @Override
//                    public void accept(BaseBean<T> loginBeanBaseBean) throws Exception {
//                        httpInterFace.dismissloading();
//                        if (loginBeanBaseBean == null || loginBeanBaseBean.getResult() == null) {
//                            httpInterFace.error(null);
//                            return;
//                        }
//                        if (loginBeanBaseBean.isSuccess()) {
//                            if (loginBeanBaseBean.getCode() == 200) {
//
//                                    httpInterFace.success(loginBeanBaseBean);
//                                } else if (loginBeanBaseBean.getCode()==401) {
//                                    SharedPreferences setting = lifecycle.getSharedPreferences("firstOpen", 0);
//                                    SharedPreferences.Editor edit = setting.edit();
//                                    edit.putBoolean("isLogin", false);
//                                    edit.commit();
//                                    lifecycle.startActivity(new Intent(lifecycle, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("isLogin", false));
//                                } else {
//                                    ToastUtils.showShort(TextUtils.isEmpty(loginBeanBaseBean.getMessage()) ? "请求失败" : loginBeanBaseBean.getMessage());
//
//                                }
//
//
//                        } else if (loginBeanBaseBean.getCode().equals("1005")) {
//                            if (loginBeanBaseBean.getCode().contains("9990")) {
//                                updataaccounid(baseBeanObservable, lifecycle, httpInterFace);
//                            } else if (loginBeanBaseBean.getCode().contains("9991")) {
//                                SharedPreferences setting = lifecycle.getSharedPreferences("firstOpen", 0);
//                                SharedPreferences.Editor edit = setting.edit();
//                                edit.putBoolean("isLogin", false);
//                                edit.commit();
//                                lifecycle.startActivity(new Intent(lifecycle, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("isLogin", false));
//                            } else {
//                                ToastUtils.showShort(TextUtils.isEmpty(loginBeanBaseBean.getErrorMsg()) ? "请求失败" : loginBeanBaseBean.getErrorMsg());
//
//                            }
//                        } else {
//                            ToastUtils.showShort(TextUtils.isEmpty(loginBeanBaseBean.getErrorMsg()) ? "请求失败" : loginBeanBaseBean.getErrorMsg());
//                        }
//
//                    }
//                }, new Consumer<ResponseThrowable>() {
//                    @Override
//                    public void accept(ResponseThrowable throwable) throws Exception {
//                        httpInterFace.dismissloading();
//                        ToastUtils.showShort(throwable.message);
//                        httpInterFace.error(throwable);
//                        throwable.printStackTrace();
//                    }
//                }, new Action() {
//                    @Override
//                    public void run() throws Exception {
//                        httpInterFace.dismissloading();
//                        //请求刷新完成收回
//                    }
//                });
//    }

//    public static <T> void get(final Observable baseBeanObservable, final Fragment lifecycle, final HttpInterFaceClass httpInterFace) {
//        if (!Utils.isNetWorkConn(lifecycle.getContext())) {
//            ToastUtils.showShort("请检查网络是否正常");
//            return;
//        }
//        baseBeanObservable
//                .compose(RxUtils.bindToLifecycle(lifecycle)) //请求与View周期同步
//                .compose(RxUtils.schedulersTransformer()) //线程调度
//                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
//                .doOnSubscribe(new Consumer<Disposable>() {
//                    @Override
//                    public void accept(Disposable disposable) throws Exception {
//                        httpInterFace.startDialog();
//                    }
//                })
//                .subscribe(new Consumer<BaseBean<T>>() {
//                    @Override
//                    public void accept(BaseBean<T> loginBeanBaseBean) throws Exception {
//                        httpInterFace.dismissloading();
//                        if (loginBeanBaseBean == null || loginBeanBaseBean.getReturnCode() == null) {
//                            httpInterFace.error(null);
//                            return;
//                        }
//                        if (loginBeanBaseBean.isSuccess()) {
//                            if (loginBeanBaseBean.getReturnCode().equals("200")) {
//                                if (TextUtils.isEmpty(loginBeanBaseBean.getErrorCode())) {
//                                    httpInterFace.success(loginBeanBaseBean);
//                                } else if (loginBeanBaseBean.getErrorCode().contains("9990")) {
//                                    updataaccounid(baseBeanObservable, lifecycle.getContext(), httpInterFace);
//                                } else if (loginBeanBaseBean.getErrorCode().contains("9991")) {
//                                    SharedPreferences setting = lifecycle.getContext().getSharedPreferences("firstOpen", 0);
//                                    SharedPreferences.Editor edit = setting.edit();
//                                    edit.putBoolean("isLogin", false);
//                                    edit.commit();
//                                    lifecycle.getContext().startActivity(new Intent(lifecycle.getContext(), LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("isLogin", false));
//                                } else {
//                                    ToastUtils.showShort(TextUtils.isEmpty(loginBeanBaseBean.getErrorMsg()) ? "请求失败" : loginBeanBaseBean.getErrorMsg());
//
//                                }
//                            } else {
//                                ToastUtils.showShort(TextUtils.isEmpty(loginBeanBaseBean.getErrorMsg()) ? "请求失败" : loginBeanBaseBean.getErrorMsg());
//                            }
//
//                        } else if (loginBeanBaseBean.getReturnCode().equals("1005")) {
//                            if (loginBeanBaseBean.getErrorCode().contains("9990")) {
//                                updataaccounid(baseBeanObservable, lifecycle.getContext(), httpInterFace);
//                            } else if (loginBeanBaseBean.getErrorCode().contains("9991")) {
//                                SharedPreferences setting = lifecycle.getContext().getSharedPreferences("firstOpen", 0);
//                                SharedPreferences.Editor edit = setting.edit();
//                                edit.putBoolean("isLogin", false);
//                                edit.commit();
//                                lifecycle.getContext().startActivity(new Intent(lifecycle.getContext(), LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("isLogin", false));
//                            } else {
//                                ToastUtils.showShort(TextUtils.isEmpty(loginBeanBaseBean.getErrorMsg()) ? "请求失败" : loginBeanBaseBean.getErrorMsg());
//
//                            }
//                        } else {
//                            ToastUtils.showShort(TextUtils.isEmpty(loginBeanBaseBean.getErrorMsg()) ? "请求失败" : loginBeanBaseBean.getErrorMsg());
//                        }
//
//                    }
//                }, new Consumer<ResponseThrowable>() {
//                    @Override
//                    public void accept(ResponseThrowable throwable) throws Exception {
//                        httpInterFace.dismissloading();
//                        ToastUtils.showShort(throwable.message);
//                        httpInterFace.error(throwable);
//                        throwable.printStackTrace();
//                    }
//                }, new Action() {
//                    @Override
//                    public void run() throws Exception {
//                        httpInterFace.dismissloading();
//                        //请求刷新完成收回
//                    }
//                });
//    }
//
//    public static Throwable mRefreshTokenError = null;
//
//    public static Observable<?> refreshTokenWhenTokenInvalid(Context lifecycle) {
//        Log.e("=======", "牛逼了3333");
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("accountId", UserPreference.getInstance(lifecycle).getAccountId() + "");
//        map.put("appName", Contents.APPNAME);
//        map.put("brand", SystemUtil.getDeviceBrand() + "");
//        // map.put("channel", Contents.CHANNEL);
//        map.put("deviceModel", SystemUtil.getSystemModel() + "");
//        // map.put("deviceType", "ANDROID");
//        map.put("uuid", SystemUtil.getIMEI(lifecycle) + "");
//        map.put("token", UserPreference.getInstance(lifecycle).gettoken() + "");
//        map.put("mobile", UserPreference.getInstance(lifecycle).getMobile() + "");
//        map.put("version", Utils.getAppVersionCode(lifecycle));
//        RetrofitClient.getInstance().create(ApiService.class).getNewAccountId(map)
//                .subscribe(new Consumer<BaseBean<String>>() {
//                    @Override
//                    public void accept(BaseBean<String> stringBaseBean) throws Exception {
//                        if (stringBaseBean.getResult() != null) {
//                            UserPreference.getInstance(lifecycle).setaccountId(stringBaseBean.getResult());
//                        } else {
//                            if (stringBaseBean.getErrorCode() != null && stringBaseBean.getErrorCode().contains("9991")) {
//                                SharedPreferences setting = lifecycle.getSharedPreferences("firstOpen", 0);
//                                SharedPreferences.Editor edit = setting.edit();
//                                edit.putBoolean("isLogin", false);
//                                edit.commit();
//                                lifecycle.startActivity(new Intent(lifecycle, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("isLogin", false));
//
//                                mRefreshTokenError = new ReLogin(new Throwable("请重新登录"), 9991);
//                            }
//                        }
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        mRefreshTokenError = throwable;
//                    }
//                });
//        if (mRefreshTokenError != null) {
//            return Observable.error(mRefreshTokenError);
//        } else {
//            return Observable.just(true);
//        }
//    }
//
//
//    public static void updataaccounid(final Observable baseBeanObservable, final Context lifecycle, final HttpInterFaceClass httpInterFace) {
//
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("accountId", UserPreference.getInstance(lifecycle).getAccountId() + "");
//        map.put("appName", Contents.APPNAME);
//        map.put("brand", SystemUtil.getDeviceBrand() + "");
//        // map.put("channel", Contents.CHANNEL);
//        map.put("deviceModel", SystemUtil.getSystemModel() + "");
//        // map.put("deviceType", "ANDROID");
//        map.put("uuid", SystemUtil.getIMEI(lifecycle) + "");
//        map.put("token", UserPreference.getInstance(lifecycle).gettoken() + "");
//        map.put("mobile", UserPreference.getInstance(lifecycle).getMobile() + "");
//        map.put("version", Utils.getAppVersionCode(lifecycle));
//        Observable<BaseBean<String>> newAccountId = RetrofitClient.getInstance().create(ApiService.class).getNewAccountId(map);
//        newAccountId
//
//                .compose(RxUtils.bindToLifecycle(lifecycle)) //请求与View周期同步
//                .compose(RxUtils.schedulersTransformer()) //线程调度
//                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
//                .doOnSubscribe(new Consumer<Disposable>() {
//                    @Override
//                    public void accept(Disposable disposable) throws Exception {
//                        httpInterFace.startDialog();
//                    }
//                }).subscribe(new Consumer<BaseBean<String>>() {
//
//                                 @Override
//                                 public void accept(BaseBean<String> loginBeanBaseBean) throws Exception {
//                                     if (loginBeanBaseBean.isSuccess()) {
//                                         if (loginBeanBaseBean.getCode() == 200) {
//                                                 UserPreference.getInstance(lifecycle).setaccountId(loginBeanBaseBean.getResult());
//
//                                                 //  get(baseBeanObservable,lifecycle,httpInterFace);
////                                             } else if (loginBeanBaseBean.getCode().equals("9991")) {
//
//                                             } else if (loginBeanBaseBean.getCode() != 401) {
//                                                 ToastUtils.showShort(TextUtils.isEmpty(loginBeanBaseBean.getMessage()) ? "请求失败" : loginBeanBaseBean.getMessage());
//
//                                             }
//
//                                     } else {
//                                         ToastUtils.showShort(TextUtils.isEmpty(loginBeanBaseBean.getMessage()) ? "请求失败" : loginBeanBaseBean.getMessage());
//                                     }
//                                 }
//                             }, new Consumer<ResponseThrowable>() {
//                                 @Override
//                                 public void accept(ResponseThrowable throwable) throws Exception {
//                                     httpInterFace.dismissloading();
//                                     ToastUtils.showShort(throwable.message);
//                                     httpInterFace.error(throwable);
//                                     throwable.printStackTrace();
//                                 }
//                             }, new Action() {
//                                 @Override
//                                 public void run() throws Exception {
//                                     httpInterFace.dismissloading();
//                                 }
//                             }
//        );
//
//
//    }
//
//
//    public static void updataaccounid(final Observable baseBeanObservable, final BaseViewModel lifecycle, final HttpInterFaceClass httpInterFace) {
////        HashMap<String, Object> map = new HashMap<>();
////        map.put("accountId", UserPreference.getInstance(lifecycle.getApplication()).getAccountId() + "");
////        map.put("appName", Contents.APPNAME);
////        map.put("brand", SystemUtil.getDeviceBrand() + "");
////        // map.put("channel", Contents.CHANNEL);
////        map.put("deviceModel", SystemUtil.getSystemModel() + "");
////        // map.put("deviceType", "ANDROID");
////        map.put("uuid", SystemUtil.getIMEI(lifecycle.getApplication()) + "");
////        map.put("token", UserPreference.getInstance(lifecycle.getApplication()).gettoken() + "");
////        map.put("mobile", UserPreference.getInstance(lifecycle.getApplication()).getMobile() + "");
////        map.put("version", Utils.getAppVersionCode(lifecycle.getApplication()));
////        Observable<BaseBean<String>> newAccountId = RetrofitClient.getInstance().create(ApiService.class).getNewAccountId(map);
////        newAccountId
////                .compose(RxUtils.bindToLifecycle(lifecycle.getLifecycleProvider())) //请求与View周期同步
////                .compose(RxUtils.schedulersTransformer()) //线程调度
////                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
////                .doOnSubscribe(new Consumer<Disposable>() {
////                    @Override
////                    public void accept(Disposable disposable) throws Exception {
////                        httpInterFace.startDialog();
////                    }
////                }).subscribe(new Consumer<BaseBean<String>>() {
////
////                                 @Override
////                                 public void accept(BaseBean<String> loginBeanBaseBean) throws Exception {
////                                     if (loginBeanBaseBean.isSuccess()) {
////                                         if (loginBeanBaseBean.getReturnCode().equals("200")) {
////                                             if (TextUtils.isEmpty(loginBeanBaseBean.getErrorCode())) {
////                                                 UserPreference.getInstance(lifecycle.getApplication()).setaccountId(loginBeanBaseBean.getResult());
////
////                                                 // get(baseBeanObservable,lifecycle,httpInterFace);
////                                             } else if (loginBeanBaseBean.getErrorCode().equals("9991")) {
////
////                                             } else {
////                                                 ToastUtils.showShort(TextUtils.isEmpty(loginBeanBaseBean.getErrorMsg()) ? "请求失败" : loginBeanBaseBean.getErrorMsg());
////
////                                             }
////                                         } else {
////                                             ToastUtils.showShort(TextUtils.isEmpty(loginBeanBaseBean.getErrorMsg()) ? "请求失败" : loginBeanBaseBean.getErrorMsg());
////                                         }
////
////                                     } else {
////                                         ToastUtils.showShort(TextUtils.isEmpty(loginBeanBaseBean.getErrorMsg()) ? "请求失败" : loginBeanBaseBean.getErrorMsg());
////                                     }
////                                 }
////                             }, new Consumer<ResponseThrowable>() {
////                                 @Override
////                                 public void accept(ResponseThrowable throwable) throws Exception {
////                                     httpInterFace.dismissloading();
////                                     ToastUtils.showShort(throwable.message);
////                                     httpInterFace.error(throwable);
////                                     throwable.printStackTrace();
////                                 }
////                             }, new Action() {
////                                 @Override
////                                 public void run() throws Exception {
////                                     httpInterFace.dismissloading();
////                                 }
////                             }
////        );
//
//
//    }


}
