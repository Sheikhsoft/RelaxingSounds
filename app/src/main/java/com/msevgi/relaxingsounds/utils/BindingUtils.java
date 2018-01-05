package com.msevgi.relaxingsounds.utils;

import android.databinding.BindingAdapter;
import android.databinding.BindingConversion;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.msevgi.relaxingsounds.adapter.CategoryRecyclerAdapter;
import com.msevgi.relaxingsounds.adapter.SoundRecyclerAdapter;
import com.msevgi.relaxingsounds.data.DataState;
import com.msevgi.relaxingsounds.data.DataWrapper;
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
}
