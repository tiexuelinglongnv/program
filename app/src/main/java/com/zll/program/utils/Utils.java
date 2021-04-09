package com.zll.program.utils;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.goldze.mvvmhabit.utils.ToastUtils;

public class Utils {
    public static void setText(Context context, TextView tv,
                               ArrayList<String> str, ArrayList<Integer> color,
                               ClickListener clickListener) {
// 累加数组所有的字符串为一个字符串
        StringBuffer long_str = new StringBuffer();
        for (int i = 0; i < str.size(); i++) {
            long_str.append(str.get(i));
        }
        SpannableString builder = new SpannableString(long_str.toString());
// 设置不同字符串的点击事件
        for (int i = 0; i < str.size(); i++) {
            int p = i;
            int star = long_str.toString().indexOf(str.get(i));
            int end = star + str.get(i).length();
            builder.setSpan(new Clickable(clickListener, p), star, end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

// 设置不同字符串的颜色

        ArrayList<ForegroundColorSpan> foregroundColorSpans = new ArrayList<ForegroundColorSpan>();
        for (int i = 0; i < color.size(); i++) {
            foregroundColorSpans.add(new ForegroundColorSpan(color.get(i)));
        }
        for (int i = 0; i < str.size(); i++) {
            int star = long_str.toString().indexOf(str.get(i));
            int end = star + str.get(i).length();
            builder.setSpan(foregroundColorSpans.get(i), star, end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

// 设置点击后的颜色为透明，否则会一直出现高亮
        tv.setHighlightColor(Color.TRANSPARENT);
        tv.setClickable(true);
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        tv.setText(builder);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    static class Clickable extends ClickableSpan implements View.OnClickListener {
        private final ClickListener clickListener;
        private int position;

        public Clickable(ClickListener clickListener, int position) {
            this.clickListener = clickListener;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            clickListener.click(position);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(Color.WHITE); // 设置文件颜色
            ds.setUnderlineText(false);
        }
    }


    public interface ClickListener {
        public void click(int positon);
    }


    public static String getMd5(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException caught!");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        return md5StrBuff.toString();
    }
    public static void copy(Context ctx, String content) {
        ClipboardManager cm = (ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
        cm.setText(content);
        ToastUtils.showShort("复制成功");
    }
    /**
     * 返回当前程序版本号
     */
    public static String getAppVersionCode(Context context) {
        String versionName=null;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }



    public static int getScreenWidth(Context teamItamActivity) {
        Resources resources = teamItamActivity.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        float density = dm.density;
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        return width;
    }

    public static int getScreenHeight(Context teamItamActivity) {
        Resources resources = teamItamActivity.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        float density = dm.density;
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        return height;
    }
    public static String timeStamp2Date(String seconds, String format) {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds + "000")));
    }
    public static boolean saveBitmapAsFile(Context context, Bitmap bitmap) {
        File saveFile = new File(context.getExternalCacheDir(),
                "zuizhong.jpg");
        boolean saved = false;
        FileOutputStream os = null;
        try {
            Log.d("FileCache", "Saving File To Cache " + saveFile.getPath());
            os = new FileOutputStream(saveFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.flush();
            os.close();
            saved = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return saved;
    }
    public static boolean isNetWorkConn(Context context){
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
        boolean wifi = conn.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        boolean internet = conn.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        if (wifi | internet) {
            return true;
        } else {
            return false;
        }
    }
    public static boolean isPhone(String phone) {
        if (TextUtils.isEmpty(phone) && phone.length() < 11) {
            return false;
        }
        String regex = "^((1[0-9][0-9]))\\d{8}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(phone);
        boolean isMatch = m.matches();
        return isMatch;
    }
}
