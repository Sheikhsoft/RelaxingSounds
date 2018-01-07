package com.msevgi.relaxingsounds.utils;

import android.databinding.BindingAdapter;
import android.databinding.BindingConversion;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.msevgi.relaxingsounds.R;
import com.msevgi.relaxingsounds.adapter.CategoryRecyclerAdapter;
import com.msevgi.relaxingsounds.adapter.SoundRecyclerAdapter;
import com.msevgi.relaxingsounds.data.DataState;
import com.msevgi.relaxingsounds.data.DataWrapper;
import com.msevgi.relaxingsounds.data.RSError;
import com.msevgi.relaxingsounds.model.Category;
import com.msevgi.relaxingsounds.model.Sound;

import java.util.List;

/**
 * Created by mustafasevgi on 4.01.2018.
 */

public class BindingUtils {
    @BindingAdapter({"categoryEntries", "listener"})
    public static void setCategoryEntries(RecyclerView recyclerView,
                                          List<Category> entries, CategoryRecyclerAdapter.CategoryItemClickListener itemClickListener) {
        if (entries != null) {
            LinearLayoutManager manager = new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(manager);

            recyclerView.setHasFixedSize(true);

            RecyclerViewItemDecoration decoration = new RecyclerViewItemDecoration(recyclerView.getContext());
            recyclerView.addItemDecoration(decoration);

            CategoryRecyclerAdapter adapter = new CategoryRecyclerAdapter(entries, itemClickListener);
            recyclerView.setAdapter(adapter);
        }
    }

    @BindingAdapter({"soundEntries", "listener"})
    public static void setSoundEntries(RecyclerView recyclerView,
                                       List<Sound> entries, SoundRecyclerAdapter.SoundItemClickListener itemClickListener) {
        if (entries != null) {
            LinearLayoutManager manager = new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(manager);

            recyclerView.setHasFixedSize(true);

            RecyclerViewItemDecoration decoration = new RecyclerViewItemDecoration(recyclerView.getContext());
            recyclerView.addItemDecoration(decoration);

            SoundRecyclerAdapter adapter = new SoundRecyclerAdapter(entries, itemClickListener);
            recyclerView.setAdapter(adapter);
        }
    }

    @BindingConversion
    public static <T> int convertStateToVisibility(DataWrapper<T> data) {
        if (data == null || data.getState().ordinal() == DataState.FETCHING.ordinal()) {
            return View.VISIBLE;
        } else
            return View.GONE;
    }

    @BindingAdapter({"android:src"})
    public static void setImageViewResource(ImageView imageView, Sound sound) {
        if (sound.isFavorite()) {
            imageView.setImageResource(R.drawable.ic_like);
        } else
            imageView.setImageResource(R.drawable.ic_unlike);
    }

    @BindingAdapter({"android:text"})
    public static <T> void setText(TextView textView, RSError error) {
        if (error != null) {
            textView.setVisibility(View.VISIBLE);
            textView.setText(textView.getContext().getString(error.getMessageCode()));
        } else textView.setVisibility(View.GONE);

    }

}
