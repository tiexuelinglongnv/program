package com.zll.program.ui.main;

import android.app.Application;
import android.util.Log;

import com.zll.program.base.MyBaseViewModel;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;

/**
 * @author zll
 * @class name：
 * @class describe
 * @time
 * @change
 * @chang time
 * @class describe
 */
public class MainViewModel extends MyBaseViewModel {
    public MainViewModel(@NonNull Application application) {
        super(application);
        uc.isOpen.setValue(false);
    }
    //封装一个界面发生改变的观察者
    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        //左侧画框是否打开或者关闭
        public SingleLiveEvent<Boolean> isOpen = new SingleLiveEvent<Boolean>();
        public SingleLiveEvent<String> openYuYin = new SingleLiveEvent<>();
        public SingleLiveEvent<String> openCamera = new SingleLiveEvent<>();
        public SingleLiveEvent<String> openTuiChu = new SingleLiveEvent<>();
        public SingleLiveEvent<String> closeDrawerLayout = new SingleLiveEvent<>();

        public UIChangeObservable() {
        }
    }

}
