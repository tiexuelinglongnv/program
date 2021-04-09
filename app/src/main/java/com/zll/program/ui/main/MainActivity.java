package com.zll.program.ui.main;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import com.zll.program.BR;
import com.zll.program.R;
import com.zll.program.base.MyBaseActivity;
import com.zll.program.databinding.ActivityMainBinding;
import com.zll.program.ui.home.HomeFragment;
import com.zll.program.ui.mine.PersonFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import me.majiajie.pagerbottomtabstrip.NavigationController;
import me.majiajie.pagerbottomtabstrip.item.BaseTabItem;
import me.majiajie.pagerbottomtabstrip.item.NormalItemView;

public class MainActivity extends MyBaseActivity<ActivityMainBinding,MainViewModel> {
    private NavigationController mNavigationController;
    private List<Fragment> fragments;
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public int initVariableId() {
        return BR.mainviewmodel;
    }

    @Override
    public void initData() {
        super.initData();
        mNavigationController = binding.mainDrawerlayout.activityMainTab.custom()
                .addItem(newItem(R.mipmap.home_default, R.mipmap.home_select, "Recents"))
                .addItem(newItem(R.mipmap.person_default, R.mipmap.person_select, "Favorites"))
                .build();
        fragments=new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new PersonFragment());
        binding.mainDrawerlayout.activityMainViewpager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager(),fragments));

        //自动适配ViewPager页面切换
        mNavigationController.setupWithViewPager(binding.mainDrawerlayout.activityMainViewpager);


        //设置消息数
        mNavigationController.setMessageNumber(1, 8);

        //设置显示小圆点
        mNavigationController.setHasMessage(0, true);

        binding.mainDrawerlayout.mainDl.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        viewModel.uc.isOpen.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    binding.mainDrawerlayout.mainDl.openDrawer(Gravity.LEFT);
                } else {
                    binding.mainDrawerlayout.mainDl.closeDrawers();
                }
            }
        });
        binding.mainToolbar.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewModel.uc.isOpen.getValue()){
                    viewModel.uc.isOpen.setValue(false);
                }else{
                    viewModel.uc.isOpen.setValue(true);
                }
            }
        });
    }
    //创建一个Item
    private BaseTabItem newItem(int drawable, int checkedDrawable, String text) {
        NormalItemView normalItemView = new NormalItemView(this);
        normalItemView.initialize(drawable, checkedDrawable, text);
        normalItemView.setTextDefaultColor(Color.GRAY);
        normalItemView.setTextCheckedColor(0xFF009688);
        return normalItemView;
    }
}