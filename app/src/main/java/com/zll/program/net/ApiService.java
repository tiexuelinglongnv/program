package com.zll.program.net;

import com.zll.program.base.BaseBean;
import com.zll.program.ui.login.LoginBean;

import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author zll
 * @class name：
 * @class describe
 * @time
 * @change
 * @chang time
 * @class describe
 */
public interface ApiService {

    /**
     * 发送短信验证码接口
     *
     * @param mobile 手机号
     * @param type   短息操作类型 1007绑定银行卡
     */
    @POST("/api/app/v2/user/sms")
    Observable<BaseBean<String>> sendSms(@Query("mobile") String mobile, @Query("type") int type);
    /**
     * 手机号验证码登录
     *
     * @param account
     * @param code
     * @param device
     * @return
     */
    @POST("api/app/v2/user/mobile/login")
    Observable<BaseBean<LoginBean>> mobileLogin(@Query("account") String account, @Query("code") String code, @Query("device") String device);

    /**
     * //     * 手机号密码登录
     * //     *
     * //     * @param account
     * //     * @param pwd
     * //     * @param device
     * //     * @return
     * //
     */
    @POST("/api/app/v2/user/login")
    Observable<BaseBean<LoginBean>> passwordLogin(@Query("account") String account, @Query("pwd") String pwd, @Query("device") String device);


    @POST("/standard/account/getNewAccountId")
    Observable<BaseBean<String>> getNewAccountId(@Body Map<String, Object> map);

    @POST("/standard/account/sendVerifyCode")
    Observable<BaseBean> sendVerifyCode(@Body Map<String, Object> map);


    @POST("/standard/account/login")
    Observable<BaseBean<LoginBean>> loginByMobile(@Body Map<String, Object> map);
}
