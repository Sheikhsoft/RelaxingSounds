package com.msevgi.relaxingsounds.ui.fragment;

import com.msevgi.relaxingsounds.R;
import com.msevgi.relaxingsounds.model.Sound;
import com.msevgi.relaxingsounds.utils.ToolbarOptions;

/**
 * Created by mustafasevgi on 5.01.2018.
 */

public class LikedSoundsFragment extends BaseSoundsFragment {

    @Override
    public void likeOrUnlike(Sound sound, int position) {
        super.likeOrUnlike(sound, position);
        if (!sound.isFavorite()) {
            mBinding.getDataWrapper().getData().remove(position);
            mBinding.recyclerviewProducts.getAdapter().notifyItemRemoved(position);
            mSoundViewModel.updateEmptyCase();
        }
    }

    @Override
    ToolbarOptions getToolbarOptions() {
        return new ToolbarOptions().setToggleType(ToolbarOptions.TOGGLE_NONE).setTitle(getString(R.string.title_favorites));
    }
}
