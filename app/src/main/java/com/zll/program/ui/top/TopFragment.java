package com.zll.program.ui.top;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.zll.program.BR;
import com.zll.program.R;
import com.zll.program.databinding.FragmentTopBinding;
import com.zll.program.ui.home.HomeTypeEnum;

import androidx.annotation.Nullable;
import androidx.databinding.ObservableField;
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
public class TopFragment extends BaseFragment<FragmentTopBinding, TopViewModel> {
    public static TopFragment newInstance(int channelType, int position) {
        TopFragment fragment = new TopFragment();
        Bundle args = new Bundle();
        args.putInt("channelType", channelType);
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_top;
    }

    @Override
    public int initVariableId() {
        return BR.topviewmodel;
    }

    @Override
    public void initData() {
        super.initData();
        if (getArguments() == null) return;
        int type = getArguments().getInt("channelType");
        int position = getArguments().getInt("position");
        binding.fragmentTopTv.setText(HomeTypeEnum.getTitle(type));
    }
}
