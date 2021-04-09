package com.zll.program.ui.main;

import android.app.Application;

import com.zll.program.base.MyBaseViewModel;

import androidx.annotation.NonNull;
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
public class MainViewModel2 extends MyBaseViewModel {
    public MainViewModel2(@NonNull Application application) {
        super(application);
    }

    public BindingCommand userdetailgo = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

        }
    });
}
