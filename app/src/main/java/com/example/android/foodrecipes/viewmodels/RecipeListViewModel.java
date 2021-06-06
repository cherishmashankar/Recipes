package com.example.android.foodrecipes.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.example.android.foodrecipes.models.Recipe;
import com.example.android.foodrecipes.repository.RecipeRepository;

import java.util.List;

public class RecipeListViewModel extends ViewModel {

    private RecipeRepository mRecipeRepository;

    public RecipeListViewModel() {
        mRecipeRepository = RecipeRepository.getInstance();
    }

    public LiveData<List<Recipe>> getRecipes(){
        return mRecipeRepository.getRecipes();
    }

    public void searchRecipeApi(String query, int pageNumber){
        mRecipeRepository.searchRecipeApi(query,pageNumber);
    }
}
