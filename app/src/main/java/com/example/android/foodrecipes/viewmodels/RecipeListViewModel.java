package com.example.android.foodrecipes.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.example.android.foodrecipes.models.Recipe;
import com.example.android.foodrecipes.repository.RecipeRepository;

import java.util.List;

public class RecipeListViewModel extends ViewModel {
    private static final String TAG = "RecipeListViewModel";

    private RecipeRepository mRecipeRepository;
    private Boolean mIsViewingRecipe;
    private Boolean mIsPerformingQuery;

    public RecipeListViewModel() {
        mRecipeRepository = RecipeRepository.getInstance();
        mIsViewingRecipe = false;
        mIsPerformingQuery = false;
    }

    public LiveData<List<Recipe>> getRecipes(){
        return mRecipeRepository.getRecipes();
    }

    public void searchRecipeApi(String query, int pageNumber){
        mIsViewingRecipe = true;
        mIsPerformingQuery = true;
        mRecipeRepository.searchRecipeApi(query,pageNumber);
    }

    public Boolean getIsViewingRecipe() {
        return mIsViewingRecipe;
    }

    public void setIsViewingRecipe(Boolean mIsViewingRecipe) {
        this.mIsViewingRecipe = mIsViewingRecipe;
    }

    public Boolean ifBackPressed(){
        if(mIsPerformingQuery){
            mRecipeRepository.cancelRequest();
            mIsPerformingQuery = false;
        }
        if(mIsViewingRecipe){
            mIsViewingRecipe = false;
            return false;
        }
        return true;
    }

    public Boolean getIsPerformingQuery() {
        return mIsPerformingQuery;
    }

    public void searchNextPage(){
        if(mIsViewingRecipe && !mIsPerformingQuery && !getIsDataExhausted().getValue()) {
            mRecipeRepository.searchNextPage();
           
        }
    }

    public void setIsPerformingQuery(Boolean mIsPerformingQuery) {
        this.mIsPerformingQuery = mIsPerformingQuery;
    }

    public MutableLiveData<Boolean> getIsDataExhausted() {
        return mRecipeRepository.getIsDataExhausted();
    }
}
