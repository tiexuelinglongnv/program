package com.zll.program.net;


import com.zll.program.base.BaseBean;

/**
 * Created by Administrator on 2019/8/1.
 */

public interface HttpInterFaceClass<T> {
    void startDialog();

    void dismissloading();

    void noLogin();

    void success(BaseBean<T> result);

    void error(Exception e);
}
