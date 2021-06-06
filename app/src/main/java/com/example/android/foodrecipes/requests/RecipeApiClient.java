package com.example.android.foodrecipes.requests;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.android.foodrecipes.AppExecutor;
import com.example.android.foodrecipes.models.Recipe;
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
    private MutableLiveData<List<Recipe>> mRecipe = new MutableLiveData<>();
    private RetrieveRecipesRunnable mRetrieveRecipesRunnable;

    public RecipeApiClient() {
        mRecipe = new MutableLiveData<>();
    }

    public static RecipeApiClient getInstance(){
        if(instance == null){
            instance = new RecipeApiClient();
        }
        return instance;
    }

    public LiveData<List<Recipe>> getRecipes(){
        return mRecipe;
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
                    Log.e(TAG, "run: Checpoint 1:" + list);
                    if(pageNumber == 1){
                        mRecipe.postValue(list);
                    }else{
                        List<Recipe> currentRecipe = mRecipe.getValue();
                        currentRecipe.addAll(list);
                    }
                }else{
                    String error = response.errorBody().string();
                    Log.e(TAG, "error: " + error );
                    mRecipe.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                mRecipe.postValue(null);
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
}
