package com.zll.program.ui.mine;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.zll.program.BR;
import com.zll.program.R;
import com.zll.program.databinding.FragmentPersonBinding;

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
public class PersonFragment extends BaseFragment<FragmentPersonBinding,PersonViewModel> {
    private String str;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_person;
    }

    @Override
    public int initVariableId() {
        return BR.personviewmodel;
    }

    @Override
    public void initData() {
        super.initData();
        if(getArguments()!=null) {
            str = getArguments().getString("main");
            viewModel.str = str;
        }
    }
}
