package com.msevgi.relaxingsounds.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.msevgi.relaxingsounds.R;
import com.msevgi.relaxingsounds.databinding.RowSoundBinding;
import com.msevgi.relaxingsounds.model.Sound;
import com.msevgi.relaxingsounds.utils.RealmUtils;

import java.util.List;

/**
 * Created by mustafasevgi on 4.01.2018.
 */

public class SoundRecyclerAdapter extends RecyclerView.Adapter<SoundRecyclerAdapter.SoundViewHolder> {
    private List<Sound> mList;
    private SoundItemClickListener mListener;

    public SoundRecyclerAdapter(List<Sound> list, SoundItemClickListener listener) {
        this.mListener = listener;
        this.mList = list;
    }

    @Override
    public SoundViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowSoundBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_sound, parent, false);
        return new SoundViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(SoundViewHolder holder, int position) {
        holder.bindData(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class SoundViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RowSoundBinding mBinding;

        public SoundViewHolder(RowSoundBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            mBinding.ivLikeUnlike.setOnClickListener(this);
            mBinding.ivPlayPause.setOnClickListener(this);
            mBinding.sbVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    if (mListener != null) {
                        mList.get(getAdapterPosition()).setVolume(seekBar.getProgress());
                        mListener.volumeValue(mList.get(getAdapterPosition()), seekBar.getProgress());
                    }
                }
            });
        }

        public void bindData(Sound sound) {
            mBinding.setSound(sound);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ivLikeUnlike:
                    if (mListener != null) {
                        mListener.likeOrUnlike(mList.get(getAdapterPosition()), getAdapterPosition());
                    }
                    break;
                case R.id.ivPlayPause:
                    if (mListener != null) {
                        mListener.playOrPause(mList.get(getAdapterPosition()), getAdapterPosition());
                    }
                    break;
            }

        }
    }

    public interface SoundItemClickListener {
        void playOrPause(Sound sound, int position);

        void likeOrUnlike(Sound sound, int position);

        void volumeValue(Sound sound, int value);
    }
}
