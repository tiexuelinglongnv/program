package com.zll.program.ui.top;

import android.app.Application;
import android.view.View;

import com.zll.program.ui.login.LoginActivity;

import androidx.annotation.NonNull;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.utils.KLog;

/**
 * @author zll
 * @class name：
 * @class describe
 * @time
 * @change
 * @chang time
 * @class describe
 */
public class TopViewModel extends BaseViewModel {
    public TopViewModel(@NonNull Application application) {
        super(application);
    }

    //登录按钮的点击事件
    public View.OnClickListener loginOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            KLog.i("============"+loginOnClick);
            startActivity(LoginActivity.class);
        }
    };
}
