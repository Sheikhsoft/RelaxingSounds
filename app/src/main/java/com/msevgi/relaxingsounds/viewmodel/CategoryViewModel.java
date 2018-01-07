package com.msevgi.relaxingsounds.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.msevgi.relaxingsounds.R;
import com.msevgi.relaxingsounds.api.ApiModule;
import com.msevgi.relaxingsounds.api.RetrofitCallback;
import com.msevgi.relaxingsounds.data.BeanFactory;
import com.msevgi.relaxingsounds.data.DataWrapper;
import com.msevgi.relaxingsounds.data.RSError;
import com.msevgi.relaxingsounds.model.Category;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

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
        ApiModule.getInstance().getService().serviceCategoryList().enqueue(new RetrofitCallback<ArrayList<Category>>() {
            @Override
            public void failure(Call<ArrayList<Category>> call, Throwable t) {
                categoryLiveData.setValue(BeanFactory.error(null, new RSError(RSError.DEFAULT_ERROR, R.string.default_error)));
            }

            @Override
            public void success(Call<ArrayList<Category>> call, ArrayList<Category> response) {
                if (response != null && response.size() > 0) {
                    categoryLiveData.setValue(BeanFactory.success(response));
                } else {
                    categoryLiveData.setValue(BeanFactory.error(null, new RSError(RSError.NO_RESULT, R.string.no_category)));
                }
            }

            @Override
            public void noInternetConnection() {
                categoryLiveData.setValue(BeanFactory.error(null, new RSError(RSError.NO_CONNECTION, R.string.no_connection)));
            }
        });

    }

    public MutableLiveData<DataWrapper<List<Category>>> getCategoryLiveData() {
        return categoryLiveData;
    }
}
