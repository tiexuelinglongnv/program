package com.zll.program.base;

import android.Manifest;
import android.os.Bundle;
import android.text.TextUtils;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zll.program.utils.MyProgressDialog;
import com.zll.program.utils.StatusBarUtil;

import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Observer;
import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.base.BaseActivity;

/**
 * @author zll
 * @class name：
 * @class describe
 * @time
 * @change
 * @chang time
 * @class describe
 */
public abstract class MyBaseActivity<V extends ViewDataBinding, VM extends MyBaseViewModel> extends BaseActivity<V,VM> {
    public void changebarColor(int color){
        StatusBarUtil.setStatusBarColor(this, color);
    }
    public void statusBarLightMode(){
        StatusBarUtil.statusBarLightMode(this);
    }
    //需要先调用透明，在调用改变字体颜色，才能保证及透明又改变颜色
    public void setbarfull(){
        StatusBarUtil.setStatusBarFullTransparent(this);
    }
    private MyProgressDialog progressDialog;
    @Override
    public void onCreate(Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);

        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        permissions();
    }
    public void permissions(){
        RxPermissions rxPermissions =new RxPermissions(this);
        rxPermissions.requestEach(Manifest.permission.CAMERA,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.MODIFY_AUDIO_SETTINGS,
                Manifest.permission.WRITE_SETTINGS,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA
        )
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {

                    }
                });
    }

    @Override
    public void initData() {
        super.initData();
    }
    @Override
    public void showDialog(String msg) {
        if (msg == null) {
            msg = "加载中";
        }

        if (progressDialog == null) {
            progressDialog = new MyProgressDialog(this);
        }
        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }
        progressDialog.show();
        progressDialog.setMessage(msg);

    }
    @Override
    public void dismissDialog() {
        if ( progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.ucBaseViewModel.openDialog.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(TextUtils.isEmpty(s)){
                    MyBaseActivity.this.dismissDialog();
                }else{
                    MyBaseActivity.this.showDialog(s);
                }
            }
        });
    }
}
