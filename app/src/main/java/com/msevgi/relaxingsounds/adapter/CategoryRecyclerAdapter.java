package com.msevgi.relaxingsounds.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.msevgi.relaxingsounds.R;
import com.msevgi.relaxingsounds.databinding.RowCategoryBinding;
import com.msevgi.relaxingsounds.model.Category;

import java.util.List;

/**
 * Created by mustafasevgi on 4.01.2018.
 */

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.CategoryViewHolder> {
    private List<Category> mList;
    private CategoryItemClickListener mListener;

    public CategoryRecyclerAdapter(List<Category> list, CategoryItemClickListener listener) {
        this.mList = list;
        mListener = listener;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowCategoryBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_category, parent, false);
        return new CategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        holder.bindData(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        RowCategoryBinding mBinding;

        public CategoryViewHolder(RowCategoryBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            mBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onItemClick(mList.get(getAdapterPosition()), getAdapterPosition());
                    }
                }
            });
        }

        public void bindData(Category category) {
            mBinding.setCategory(category);
        }
    }

    public interface CategoryItemClickListener {
        void onItemClick(Category category, int position);
    }
}
