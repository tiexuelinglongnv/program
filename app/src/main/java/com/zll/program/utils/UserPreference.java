package com.zll.program.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreference {
    public final static String TAG = UserPreference.class.getSimpleName();

    private static UserPreference mInstance;

    private SharedPreferences mPreferences;

    public static final String USER_LOGIN = "user_login";
    private UserPreference(Context context) {
        mPreferences = context.getSharedPreferences(USER_LOGIN, 0);
    }

    public synchronized static UserPreference getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new UserPreference(context);
        }
        return mInstance;
    }
    public void setDeviceID(String deviceidId){
        mPreferences.edit().putString("deviceidId", deviceidId).commit();
    }
    public void setaccountId(String accountId){
        mPreferences.edit().putString("accountId", accountId).commit();
    }
    public void setmobile(String mobile){
        mPreferences.edit().putString("mobile", mobile).commit();
    }
    public void settoken(String token){
        mPreferences.edit().putString("token", token).commit();
    }

    public void setnickname(String nickname ){
        mPreferences.edit().putString("nickname", nickname ).commit();
    }
    public void setapplyCancelTime(String applyCancelTime ){
        mPreferences.edit().putString("applyCancelTime", applyCancelTime ).commit();
    }
    public void settype (String type ){
        mPreferences.edit().putString("type", type).commit();
    }

    public void isAutoAlound(boolean autoalound ){
        mPreferences.edit().putBoolean("autoalound", autoalound ).commit();
    }
    public String getAccountId(){
       return mPreferences.getString("accountId",null);
    }
    public String getMobile(){
        return mPreferences.getString("mobile",null);
    }
    public String gettoken(){
        return mPreferences.getString("token",null);
    }
    public String getApplyCancelTime(){
        return mPreferences.getString("applyCancelTime",null);
    }
    public String getType(){
        return mPreferences.getString("type",null);
    }
    public String getDeviceID(){
        return mPreferences.getString("deviceidId",null);
    }


    public String getnickname(){
        return mPreferences.getString("nickname",null);
    }
    public boolean getAutoAlound(){
        return mPreferences.getBoolean("autoalound",false);
    }



    public void removeAccountId(){
        mPreferences.edit().remove("accountId").commit();
    }
    public void removeToken(){
        mPreferences.edit().remove("token").commit();
    }

    public void removeAll(){
        mPreferences.edit().clear().commit();
    }

}
