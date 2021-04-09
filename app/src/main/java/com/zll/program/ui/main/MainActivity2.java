package com.zll.program.ui.main;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.zll.program.BR;
import com.zll.program.R;
import com.zll.program.base.MyBaseActivity;
import com.zll.program.databinding.ActivityMain2Binding;
import com.zll.program.ui.home.HomeFragment;
import com.zll.program.ui.mine.PersonFragment;
import com.zll.program.widget.manager.WrappedLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

/**
 * @author zll
 * @class name：
 * @class describe
 * @time
 * @change
 * @chang time
 * @class describe
 */
public class MainActivity2 extends MyBaseActivity<ActivityMain2Binding, MainViewModel2> {
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_main2;
    }

    @Override
    public int initVariableId() {
        return BR.mainviewmodel2;
    }

    @Override
    public void initData() {
        super.initData();
        binding.llMenuContainer.srcvMenuDisplay.setLayoutManager(new WrappedLinearLayoutManager(this));
        List<String> list = new ArrayList<>();
        list.add("好友");
        list.add("邀请");
        MenuDisplayAdapter menuDisplayAdapter = new MenuDisplayAdapter(list);
        binding.llMenuContainer.srcvMenuDisplay.setAdapter(menuDisplayAdapter);
        menuDisplayAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Toast.makeText(MainActivity2.this, position+"", Toast.LENGTH_SHORT).show();
//                if (position == 0) {
//                    FriendsActivity.start(MainActivity.this);
//                } else {
//                    InvitationActivity.start(MainActivity.this);
//                }
            }
        });
        binding.rgActivityMainBottomContainer.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_activity_main_bottom_index) {
                binding.wvpActivityMainDisplay.setCurrentItem(3, false);
            } else if (checkedId == R.id.rb_activity_main_bottom_public) {
                binding.wvpActivityMainDisplay.setCurrentItem(1, false);
            } else if (checkedId == R.id.rb_activity_main_bottom_center) {
                binding.wvpActivityMainDisplay.setCurrentItem(2, false);
            } else if (checkedId == R.id.rb_activity_main_bottom_person) {
                binding.wvpActivityMainDisplay.setCurrentItem(4, false);
            } else if (checkedId == R.id.rb_activity_main_bottom_chat) {
                binding.wvpActivityMainDisplay.setCurrentItem(0, false);
            }
        });
        List<Fragment> fragmentList = new ArrayList<>();
        PersonFragment personFragment=new PersonFragment();
        Bundle bundle=new Bundle();
        bundle.putString("main","main");
        personFragment.setArguments(bundle);
        fragmentList.add(personFragment);
        fragmentList.add(new HomeFragment());
        fragmentList.add(new HomeFragment());
        fragmentList.add(new HomeFragment());
        fragmentList.add(new HomeFragment());
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.setTitleAndFragments(null, fragmentList);
        binding.wvpActivityMainDisplay.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                binding.dlActivityMainDragContainer.setIntercept(true);
                switch (position) {
                    case 0:
                        binding.rbActivityMainBottomChat.setChecked(true);
                        binding.dlActivityMainDragContainer.setIntercept(false);
                        show(binding.rgActivityMainBottomContainer);
                        break;
                    case 1:
                        binding.rbActivityMainBottomPublic.setChecked(true);
                        break;
                    case 2:
                        binding.rbActivityMainBottomCenter.setChecked(true);
                        show(binding.rgActivityMainBottomContainer);
                        break;
                    case 3:
                        binding.rbActivityMainBottomIndex.setChecked(true);
                        break;
                    case 4:
                        binding.rbActivityMainBottomPerson.setChecked(true);
                        show(binding.rgActivityMainBottomContainer);
                        break;
                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        binding.wvpActivityMainDisplay.setAdapter(viewPagerAdapter);
    }
    private void show(final View view) {
        if (view.getTranslationY() != 0) {
            ObjectAnimator.ofFloat(view, "translationY", view.getTranslationY(), 0).setDuration(200).start();
        }
    }
}
