package com.msevgi.relaxingsounds.ui.fragment;

import android.os.Bundle;

import com.msevgi.relaxingsounds.R;
import com.msevgi.relaxingsounds.model.Category;
import com.msevgi.relaxingsounds.utils.ToolbarOptions;

/**
 * Created by mustafasevgi on 5.01.2018.
 */

public class CategorySoundsFragment extends BaseSoundsFragment {

    public static CategorySoundsFragment newInstance(Category category) {
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_CATEGORY, category);
        CategorySoundsFragment fragment = new CategorySoundsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    ToolbarOptions getToolbarOptions() {
        return new ToolbarOptions().setToggleType(ToolbarOptions.TOGGLE_BACK).setTitle(getString(R.string.title_category_sounds, ((Category) getArguments().getParcelable(EXTRA_CATEGORY)).getName()));
    }

}
