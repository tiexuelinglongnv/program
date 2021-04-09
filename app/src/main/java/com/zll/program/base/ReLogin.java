package com.zll.program.base;

import me.goldze.mvvmhabit.http.ResponseThrowable;

public class ReLogin extends ResponseThrowable {


    public ReLogin(Throwable throwable, int code) {
        super(throwable, code);
    }

}
