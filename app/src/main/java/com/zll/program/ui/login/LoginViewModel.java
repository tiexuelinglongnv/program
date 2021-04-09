package com.zll.program.ui.login;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Handler;
import android.text.TextUtils;

import com.zll.program.ui.main.MainActivity;
import com.zll.program.base.BaseBean;
import com.zll.program.base.MyBaseViewModel;
import com.zll.program.net.ApiService;
import com.zll.program.net.HttpInterFace;
import com.zll.program.net.ReTrofitClientUtils;
import com.zll.program.net.RetrofitClient;
import com.zll.program.utils.UserPreference;
import com.zll.program.utils.Utils;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * @author zll
 * @class name：
 * @class describe
 * @time
 * @change
 * @chang time
 * @class describe
 */
public class LoginViewModel extends MyBaseViewModel {
    public ObservableField<String> phone = new ObservableField<>("");
    public ObservableField<String> verification = new ObservableField<>("");
    public ObservableField<String> countdown = new ObservableField<>("发送验证码");
    public ObservableField<Boolean> countdownclickable = new ObservableField<>(true);
    private Handler handler = new Handler();
    private final String SHARE_APP_TAG = "firstOpen";
    private SharedPreferences setting;
    public int laiyuan = 0;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        setting = getApplication().getSharedPreferences(SHARE_APP_TAG, 0);
        uc.isClick.setValue(false);
    }

    //发送验证码
    public BindingCommand startcount = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            getVerificationCode(phone.get());

        }
    });

    public void getVerificationCode(String mobile) {
        if (!Utils.isPhone(mobile)) {
            ToastUtils.showShort("请输入正确的手机号");
            return;
        }

        ReTrofitClientUtils.get(RetrofitClient.getInstance().create(ApiService.class).sendSms(mobile,1003), this, new HttpInterFace() {
            @Override
            public void success(BaseBean t) {
                super.success(t);
                if (t.isSuccess()) {
                    ToastUtils.showShort("验证码已发送");
                    handler.post(runnable);
                }
            }

            @Override
            public void error(Exception e) {
                super.error(e);
            }
        });
    }

    private int count = 60;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (count == 0) {
                count = 60;
                countdown.set("发送验证码");
                countdownclickable.set(true);
            } else {
                countdownclickable.set(false);
                countdown.set(count + "秒后重试");
                count--;
                handler.postDelayed(runnable, 1000);
            }
        }
    };
    //发送验证码结束
//关闭登录页面
    public BindingCommand finish = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (laiyuan == 1) {
                startActivity(MainActivity.class);
            }
            finish();
        }
    });

    //封装一个界面发生改变的观察者
    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        //左侧画框是否打开或者关闭
        public SingleLiveEvent<Boolean> isClick = new SingleLiveEvent<>();
    }


    public BindingCommand login = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            loginByMobile();
        }
    });

    public void loginByMobileSms() {
        if (!Utils.isPhone(phone.get())) {
            return;
        }
        if (TextUtils.isEmpty(verification.get()) || verification.get().length() < 6) {
            return;
        }
        ReTrofitClientUtils.get(RetrofitClient.getInstance().create(ApiService.class).mobileLogin(phone.get(),verification.get(),UserPreference.getInstance(getApplication()).getDeviceID())
                , this, new HttpInterFace<LoginBean>() {
                    @Override
                    public void success(BaseBean<LoginBean> result) {
                        super.success(result);
                        ToastUtils.showShort("登录成功");
                        UserPreference.getInstance(getApplication().getApplicationContext()).removeAll();
                        UserPreference.getInstance(getApplication().getApplicationContext()).setaccountId(result.getData().getMobile());
                        UserPreference.getInstance(getApplication().getApplicationContext()).setmobile(phone.get());
                        UserPreference.getInstance(getApplication().getApplicationContext()).settoken(result.getData().getToken());
                        SharedPreferences.Editor edit = setting.edit();
                        edit.putBoolean("isLogin", true);
                        edit.commit();
                        startActivity(MainActivity.class);
                        finish();

                    }

                    @Override
                    public void error(Exception e) {
                        super.error(e);
                        ToastUtils.showShort(e+"");
                    }
                }


                );

    }
    public void loginByMobile() {
        if (!Utils.isPhone(phone.get())) {
            return;
        }
        if (TextUtils.isEmpty(verification.get()) || verification.get().length() < 6) {
            return;
        }
        ReTrofitClientUtils.get(RetrofitClient.getInstance().create(ApiService.class).passwordLogin(phone.get(),verification.get(),UserPreference.getInstance(getApplication()).getDeviceID())
                , this, new HttpInterFace<LoginBean>() {
                    @Override
                    public void success(BaseBean<LoginBean> result) {
                        super.success(result);
                        ToastUtils.showShort("登录成功");
                        UserPreference.getInstance(getApplication().getApplicationContext()).removeAll();
                        UserPreference.getInstance(getApplication().getApplicationContext()).setaccountId(result.getData().getMobile());
                        UserPreference.getInstance(getApplication().getApplicationContext()).setmobile(phone.get());
                        UserPreference.getInstance(getApplication().getApplicationContext()).settoken(result.getData().getToken());
                        SharedPreferences.Editor edit = setting.edit();
                        edit.putBoolean("isLogin", true);
                        edit.commit();
                        startActivity(MainActivity.class);
                        finish();

                    }

                    @Override
                    public void error(Exception e) {
                        super.error(e);
                        ToastUtils.showShort(e+"");
                    }
                }


        );

    }
}
