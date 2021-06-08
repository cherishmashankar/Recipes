package com.example.android.foodrecipes.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.android.foodrecipes.models.Recipe;
import com.example.android.foodrecipes.requests.RecipeApiClient;

import java.util.List;

public class RecipeRepository {
    private static final String TAG = "RecipeRepository";
    private static RecipeRepository instance;
    private RecipeApiClient mRecipeApiClient;
    private String mQuery;
    private int mPageNumber;


    public RecipeRepository() {
        mRecipeApiClient = RecipeApiClient.getInstance();

    }

    public static RecipeRepository getInstance(){
        if(instance == null){
            instance = new RecipeRepository();
        }
        return instance;
    }

    public LiveData<List<Recipe>> getRecipes(){
        return mRecipeApiClient.getRecipes();
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
