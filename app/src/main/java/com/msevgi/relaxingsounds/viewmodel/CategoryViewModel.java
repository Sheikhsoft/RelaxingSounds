package com.msevgi.relaxingsounds.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.msevgi.relaxingsounds.api.ApiModule;
import com.msevgi.relaxingsounds.data.BeanFactory;
import com.msevgi.relaxingsounds.data.DataWrapper;
import com.msevgi.relaxingsounds.model.Category;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mustafasevgi on 3.01.2018.
 */

public class CategoryViewModel extends ViewModel {
    private final MutableLiveData<DataWrapper<List<Category>>> categoryLiveData;

    public CategoryViewModel() {
        categoryLiveData = new MutableLiveData<>();
        getCategories();
    }

    private void getCategories() {
        categoryLiveData.setValue(BeanFactory.fetching(null));
        ApiModule.getInstance().getService().serviceCategoryList().enqueue(new Callback<ArrayList<Category>>() {
            @Override
            public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {
                if (response.body() != null && response.body().size() > 0) {
                    categoryLiveData.setValue(BeanFactory.success(response.body()));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Category>> call, Throwable t) {
                categoryLiveData.setValue(BeanFactory.error(null, null));
            }
        });

    }

    public MutableLiveData<DataWrapper<List<Category>>> getCategoryLiveData() {
        return categoryLiveData;
    }
}
