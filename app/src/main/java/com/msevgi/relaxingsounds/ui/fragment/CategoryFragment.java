package com.msevgi.relaxingsounds.ui.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.msevgi.relaxingsounds.R;
import com.msevgi.relaxingsounds.ui.activity.MediaBaseActivity;
import com.msevgi.relaxingsounds.utils.ToolbarOptions;
import com.msevgi.relaxingsounds.adapter.CategoryRecyclerAdapter;
import com.msevgi.relaxingsounds.data.DataState;
import com.msevgi.relaxingsounds.data.DataWrapper;
import com.msevgi.relaxingsounds.databinding.CategoryListBinding;
import com.msevgi.relaxingsounds.model.Category;
import com.msevgi.relaxingsounds.ui.activity.MainActivity;
import com.msevgi.relaxingsounds.viewmodel.CategoryViewModel;

import java.util.List;

/**
 * Created by mustafasevgi on 4.01.2018.
 */

public class CategoryFragment extends BaseFragment implements CategoryRecyclerAdapter.CategoryItemClickListener {
    private CategoryListBinding mBinding;
    private CategoryViewModel mCategoryViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_category_list, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCategoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        mBinding.setListener(this);
        mCategoryViewModel.getCategoryLiveData().observe(this, new Observer<DataWrapper<List<Category>>>() {
            @Override
            public void onChanged(@Nullable DataWrapper<List<Category>> listDataWrapper) {
                if (listDataWrapper.getState().ordinal() == DataState.SUCCESS.ordinal()) {
                    mBinding.setDataWrapper(listDataWrapper);
                }

            }
        });
    }

    @Override
    ToolbarOptions getToolbarOptions() {
        return new ToolbarOptions().setTitle(getString(R.string.title_category)).setToggleType(ToolbarOptions.TOGGLE_NONE);
    }

    @Override
    public void onItemClick(Category category, int position) {
        ((MainActivity) getActivity()).showFragment(CategorySoundsFragment.newInstance(category), MediaBaseActivity.TRANSITION_TYPE_ADD);
    }
}
