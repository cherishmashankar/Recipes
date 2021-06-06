package com.example.android.foodrecipes.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.example.android.foodrecipes.models.Recipe;
import com.example.android.foodrecipes.repository.RecipeRepository;

import java.util.List;

public class RecipeListViewModel extends ViewModel {

    private RecipeRepository mRecipeRepository;
    private Boolean mIsViewingRecipe;

    public RecipeListViewModel() {
        mRecipeRepository = RecipeRepository.getInstance();
        mIsViewingRecipe = false;
    }

    public LiveData<List<Recipe>> getRecipes(){
        return mRecipeRepository.getRecipes();
    }

    public void searchRecipeApi(String query, int pageNumber){
        mIsViewingRecipe = true;
        mRecipeRepository.searchRecipeApi(query,pageNumber);
    }

    public Boolean getIsViewingRecipe() {
        return mIsViewingRecipe;
    }

    public void setIsViewingRecipe(Boolean mIsViewingRecipe) {
        this.mIsViewingRecipe = mIsViewingRecipe;
    }

    public Boolean ifBackPressed(){
        if(mIsViewingRecipe){
            mIsViewingRecipe = false;
            return true;
        }
        return false;
    }
}
