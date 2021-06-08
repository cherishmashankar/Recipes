package com.example.android.foodrecipes.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.android.foodrecipes.models.Recipe;
import com.example.android.foodrecipes.requests.RecipeApiClient;

import java.util.List;

public class RecipeRepository {
    private static final String TAG = "RecipeRepository";
    private static RecipeRepository instance;
    private RecipeApiClient mRecipeApiClient;
    private String mQuery;
    private int mPageNumber;
    private MutableLiveData<Boolean> mIsDataExhausted = new MutableLiveData<>();
    private MediatorLiveData<List<Recipe>> mRecipes = new MediatorLiveData<>();


    public RecipeRepository() {
        mRecipeApiClient = RecipeApiClient.getInstance();
        initMediators();

    }

    public static RecipeRepository getInstance(){
        if(instance == null){
            instance = new RecipeRepository();
        }
        return instance;
    }

    public LiveData<List<Recipe>> getRecipes(){
        return mRecipes;
    }

    private void initMediators(){
        final LiveData<List<Recipe>> recipeListSource = mRecipeApiClient.getRecipes();
        mRecipes.addSource(recipeListSource, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                if(recipes != null){
                    mRecipes.setValue(recipes);
                    doneQuery(recipes);
                }else{
                    //check dataBase
                    doneQuery(null);
                }
            }
        });

    }

    private void doneQuery(List<Recipe> list){
        if(list != null){
            if(list.size()%30 != 0 ){
                mIsDataExhausted.setValue(true);
            }
        }else{
            mIsDataExhausted.setValue(true);
        }
    }

    public MutableLiveData<Boolean> getIsDataExhausted() {
        return mIsDataExhausted;
    }

    public LiveData<Recipe> getRecipe(){
        return mRecipeApiClient.getRecipe();
    }

    public LiveData<Boolean> getIsTimeOut(){
        return mRecipeApiClient.getIsTimeOut();
    }

    public void searchRecipeById(String recipeId){
        mRecipeApiClient.searchRecipeById(recipeId);
    }


    public void searchRecipeApi(String query, int pageNumber) {
        if(pageNumber == 0){
            pageNumber = 1;
        }
        mRecipeApiClient.searchRecipeApi(query,pageNumber);
        mIsDataExhausted.setValue(false);
        mQuery = query;
        mPageNumber = pageNumber;

    }

    public void searchNextPage(){
        searchRecipeApi(mQuery,mPageNumber + 1);
    }

    public void cancelRequest(){
        mRecipeApiClient.cancelRequest();
    }
}
