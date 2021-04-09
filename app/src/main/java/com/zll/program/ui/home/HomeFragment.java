package com.zll.program.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.zll.program.BR;
import com.zll.program.R;
import com.zll.program.databinding.FragmentHomeBinding;
import com.zll.program.utils.Utils;

import androidx.annotation.Nullable;
import me.goldze.mvvmhabit.base.BaseFragment;

/**
 * @author zll
 * @class name：
 * @class describe
 * @time
 * @change
 * @chang time
 * @class describe
 */
public class HomeFragment extends BaseFragment<FragmentHomeBinding, HomeViewModel> {
    private String[] titles = new String[]{HomeTypeEnum.NEW.getTitle(), HomeTypeEnum.ING.getTitle(), HomeTypeEnum.ALL.getTitle()};
    private int[] channelTypes = new int[]{HomeTypeEnum.NEW.getOrderType(), HomeTypeEnum.ING.getOrderType(), HomeTypeEnum.ALL.getOrderType()};
    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_home;
    }

    @Override
    public int initVariableId() {
        return BR.homeviewmodel;
    }

    @Override
    public void initData() {
        super.initData();
        HomeFragmentPagerAdapter  homeFragmentPagerAdapter=new HomeFragmentPagerAdapter(getActivity().getSupportFragmentManager(), titles, channelTypes);
        binding.fragmentHomeVp.setAdapter(homeFragmentPagerAdapter);
        binding.fragmentHomeTabLayout.setViewPager(binding.fragmentHomeVp, titles);
        binding.fragmentHomeTabLayout.onPageSelected(0);
        binding.fragmentHomeVp.setOffscreenPageLimit(1);
    }
}
