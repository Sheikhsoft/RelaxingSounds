package com.msevgi.relaxingsounds.api;

import com.msevgi.relaxingsounds.model.Category;
import com.msevgi.relaxingsounds.model.Sound;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by mustafasevgi on 4.01.2018.
 */

public interface Services {
    @GET("categories")
    Call<ArrayList<Category>> serviceCategoryList();

    @GET("sounds")
    Call<ArrayList<Sound>> serviceCategorySoundList();
}
