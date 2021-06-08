package com.example.android.foodrecipes.requests;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.android.foodrecipes.AppExecutor;
import com.example.android.foodrecipes.models.Recipe;
import com.example.android.foodrecipes.requests.response.RecipeResponse;
import com.example.android.foodrecipes.requests.response.RecipeSearchResponse;
import com.example.android.foodrecipes.utils.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

public class RecipeApiClient {
    private static final String TAG = "RecipeApiClient";

    private static RecipeApiClient instance;
    private MutableLiveData<List<Recipe>> mRecipes;
    private RetrieveRecipesRunnable mRetrieveRecipesRunnable;
    private MutableLiveData<Recipe> mRecipe;
    private RetrieveRecipeRunnable mRetrieveRecipeRunnable;
    private MutableLiveData<Boolean> mRecipeTimeOut = new MutableLiveData<>();


    public RecipeApiClient() {
        mRecipes = new MutableLiveData<>();
        mRecipe = new MutableLiveData<>();

    }

    public static RecipeApiClient getInstance(){
        if(instance == null){
            instance = new RecipeApiClient();
        }
        return instance;
    }

    public LiveData<List<Recipe>> getRecipes(){
        return mRecipes;
    }

    public LiveData<Recipe> getRecipe(){
        return mRecipe;
    }
    public LiveData<Boolean> getIsTimeOut(){
        return mRecipeTimeOut;
    }


    public void searchRecipeApi(String query, int pageNumber){

        if(mRetrieveRecipesRunnable != null){
            mRetrieveRecipesRunnable = null;
        }
        mRetrieveRecipesRunnable = new RetrieveRecipesRunnable(query,pageNumber);
        final Future handler = AppExecutor.getInstance().networkIO().submit(mRetrieveRecipesRunnable);

        AppExecutor.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                // Handler gets cancelled after 3000 ms
                //mRecipeTimeOut.postValue(true);
                handler.cancel(true);

            }
        }, Constants.NETWORK_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    public void searchRecipeById(String recipeID){
//        mRecipeTimeOut.setValue(false);
        if(mRetrieveRecipeRunnable != null){
            mRetrieveRecipeRunnable = null;
        }
        mRetrieveRecipeRunnable = new RetrieveRecipeRunnable(recipeID);

        final Future handler = AppExecutor.getInstance().networkIO().submit(mRetrieveRecipeRunnable);
        mRecipeTimeOut.setValue(false);
        AppExecutor.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                // Handler gets cancelled after 3000 ms still work has to be done
                mRecipeTimeOut.postValue(true);
                handler.cancel(true);

            }
        }, Constants.NETWORK_TIMEOUT, TimeUnit.MILLISECONDS);

    }


    private class RetrieveRecipesRunnable implements Runnable{

        private String query;
        private int pageNumber;
        boolean cancelRequest;

        public RetrieveRecipesRunnable(String query, int pageNumber) {
            this.query = query;
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            try {
                Response response = getRecipes(query,pageNumber).execute();


                if(cancelRequest){
                    return;
                }
                if(response.code() == 200){

                    List<Recipe> list = new ArrayList<>(((RecipeSearchResponse)response.body()).getRecipes());

                    if(pageNumber == 1){

                        mRecipes.postValue(list);
                    }else{

                        List<Recipe> currentRecipe = mRecipes.getValue();
                        currentRecipe.addAll(list);
                        mRecipes.postValue(currentRecipe);
                    }
                }else{
                    String error = response.errorBody().string();
                    Log.e(TAG, "error: " + error );
                    mRecipes.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                mRecipes.postValue(null);
            }


        }

        private Call<RecipeSearchResponse> getRecipes(String query, int pageNumber){
            return ServiceGenerator.getRecipeApi().searchRecipe(query,String.valueOf(pageNumber));
        }

        private void cancelRequest(){
            Log.e(TAG, "cancelRequest:");
            cancelRequest = true;
        }
    }

    private class RetrieveRecipeRunnable implements Runnable{

        private String recipeId;
        private int pageNumber;
        boolean cancelRequest;

        public RetrieveRecipeRunnable(String recipeId) {
            this.recipeId = recipeId;
            cancelRequest = false;
        }

        @Override
        public void run() {
            try {
                Response response = getRecipe(recipeId).execute();

                if(cancelRequest){
                    return;
                }
                if(response.code() == 200){
                    Recipe recipe = ((RecipeResponse)response.body()).getRecipe();
                    mRecipe.postValue(recipe);

                }else{
                    String error = response.errorBody().string();
                    Log.e(TAG, "error: " + error );
                    mRecipe.postValue(null);
                }
            }catch (IOException e) {
                e.printStackTrace();
                mRecipe.postValue(null);
            }

        }

        private Call<RecipeResponse> getRecipe(String recipeId){
            return ServiceGenerator.getRecipeApi().getRecipe(recipeId);
        }

        private void cancelRequest(){
            Log.e(TAG, "cancelRequest:");
            cancelRequest = true;
        }
    }






















    public void cancelRequest(){
        if(mRetrieveRecipesRunnable != null){
            mRetrieveRecipesRunnable.cancelRequest();
         }
        if(mRetrieveRecipeRunnable != null){
            mRetrieveRecipeRunnable.cancelRequest();
        }

    }
}
