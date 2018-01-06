package com.msevgi.relaxingsounds.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import com.msevgi.relaxingsounds.R;
import com.msevgi.relaxingsounds.ui.activity.MainActivity;
import com.msevgi.relaxingsounds.utils.ToolbarOptions;

/**
 * Created by mustafasevgi on 4.01.2018.
 */

public abstract class BaseFragment extends Fragment {
    abstract ToolbarOptions getToolbarOptions();


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getView() != null) {
            Toolbar toolbar = getView().findViewById(R.id.toolbar);
            if (toolbar != null) {
                MainActivity activity = (MainActivity) getActivity();

                activity.setSupportActionBar(toolbar);
                if (activity.getSupportActionBar() != null) {
                    activity.getSupportActionBar().setTitle(getToolbarOptions().getTitle());
                }

                switch (getToolbarOptions().getToggleType()) {
                    case ToolbarOptions.TOGGLE_BACK:
                        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
                        break;
                    case ToolbarOptions.TOGGLE_NONE:
                        toolbar.setNavigationIcon(null);
                        break;

                }
            }
        }
    }
}
