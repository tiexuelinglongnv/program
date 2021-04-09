package com.zll.program.ui.mine;

import android.app.Application;
import android.text.TextUtils;

import com.zll.program.ui.main.MainActivity;

import androidx.annotation.NonNull;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * @author zll
 * @class name：
 * @class describe
 * @time
 * @change
 * @chang time
 * @class describe
 */
public class PersonViewModel extends BaseViewModel {
    String str;

    public PersonViewModel(@NonNull Application application) {
        super(application);
    }

    public BindingCommand gotomain = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (!TextUtils.isEmpty(str)) {
                startActivity(MainActivity.class);
            }
        }
    });
}
