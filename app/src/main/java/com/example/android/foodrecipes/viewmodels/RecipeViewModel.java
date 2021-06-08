package com.example.android.foodrecipes.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.foodrecipes.models.Recipe;
import com.example.android.foodrecipes.repository.RecipeRepository;

import java.util.List;

public class RecipeViewModel extends ViewModel {
    private static final String TAG = "RecipeViewModel";
    private RecipeRepository mRecipeRepository;
    private String mRecipeId;

    private Boolean mDidRetrieveData;

    public RecipeViewModel() {
        mRecipeRepository = RecipeRepository.getInstance();
        mDidRetrieveData = false;

    }

    public LiveData<Recipe> getRecipe(){
        return mRecipeRepository.getRecipe();
    }
    public LiveData<Boolean> getIsTimeOut(){
        return mRecipeRepository.getIsTimeOut();
    }


    public void searchRecipeById(String recipeId){
        mRecipeId = recipeId;
        mRecipeRepository.searchRecipeById(recipeId);
    }

    public String getRecipeId() {

        return mRecipeId;
    }

    public Boolean getmDidRetrieveData() {
        return mDidRetrieveData;
    }

    public void setmDidRetrieveData(Boolean mDidRetrieveData) {
        this.mDidRetrieveData = mDidRetrieveData;
    }
}
