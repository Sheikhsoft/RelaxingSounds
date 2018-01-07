package com.msevgi.relaxingsounds.ui.fragment;

import com.msevgi.relaxingsounds.R;
import com.msevgi.relaxingsounds.utils.ToolbarOptions;

/**
 * Created by mustafasevgi on 5.01.2018.
 */

public class LikedSoundsFragment extends BaseSoundsFragment {

    @Override
    ToolbarOptions getToolbarOptions() {
        return new ToolbarOptions().setToggleType(ToolbarOptions.TOGGLE_NONE).setTitle(getString(R.string.title_favorites));
    }
}
