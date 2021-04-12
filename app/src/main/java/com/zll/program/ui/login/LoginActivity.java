package com.zll.program.ui.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.zll.program.BR;
import com.zll.program.R;
import com.zll.program.base.MyBaseActivity;
import com.zll.program.databinding.ActivityLoginBinding;
import com.zll.program.utils.Utils;
import com.zll.program.widget.actionbar.TitleBar;

import androidx.lifecycle.Observer;
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
public class LoginActivity  extends MyBaseActivity<ActivityLoginBinding,LoginViewModel> {
    @Override
    public int initContentView(Bundle savedInstanceState) {
        setbarfull();
        statusBarLightMode();
        return R.layout.activity_login;
    }

    @Override
    public int initVariableId() {
        return BR.loginviewmodel;
    }

    @Override
    public void initData() {
        super.initData();
        binding.titlebar.setTitle("登录");
        binding.activityLoginVerification.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length()==6&& Utils.isPhone(viewModel.phone.get())){
                    viewModel.uc.isClick.setValue(true);
                }else{
                    viewModel.uc.isClick.setValue(false);
                }
            }
        });
        binding.titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.showLong("点击返回");
            }
        }).setCenterClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showLong("点击标题");

            }
        }).addAction(new TitleBar.ImageAction(R.mipmap.delete) {
            @Override
            public void performAction(View view) {
                ToastUtils.showLong("点击更多！");
            }
        });
    }
    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.uc.isClick.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    binding.activityLoginLogin.setBackgroundResource(R.drawable.bg_startup_dialog_button);
                }else{
                    binding.activityLoginLogin.setBackgroundResource(R.drawable.bg_startup_dialog_unclick_button);
                }
            }
        });
    }
}
