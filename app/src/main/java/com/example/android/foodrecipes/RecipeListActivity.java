package com.example.android.foodrecipes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.android.foodrecipes.adapters.OnRecipeListener;
import com.example.android.foodrecipes.adapters.RecipeRecyclerAdapter;
import com.example.android.foodrecipes.models.Recipe;
import com.example.android.foodrecipes.requests.RecipeApi;
import com.example.android.foodrecipes.requests.ServiceGenerator;
import com.example.android.foodrecipes.requests.response.RecipeResponse;
import com.example.android.foodrecipes.requests.response.RecipeSearchResponse;
import com.example.android.foodrecipes.utils.Constants;
import com.example.android.foodrecipes.utils.Testing;
import com.example.android.foodrecipes.viewmodels.RecipeListViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeListActivity extends BaseActivity implements OnRecipeListener {
    private static final String TAG = "RecipeListActivity";

    //Ui components
    private RecyclerView mRecyclerView;


    //variables
    private RecipeListViewModel mRecipeListViewModel;
    private RecipeRecyclerAdapter mRecipeRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        mRecipeListViewModel = ViewModelProviders.of(this).get(RecipeListViewModel.class);
        mRecyclerView = findViewById(R.id.recipe_list_recyclerView);


        initialiseRecyclerView();
       subscribeObserver();
       testRetro();


    }

    private void subscribeObserver(){
        mRecipeListViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                if(recipes != null) {
                    Testing.printRecipes(recipes, TAG);
                    mRecipeRecyclerAdapter.setRecipes(recipes);
                }

            }
        });
    }

    public void searchRecipeApi(String query, int pageNumber){
        mRecipeListViewModel.searchRecipeApi(query,pageNumber);
    }

    private void testRetro(){
        searchRecipeApi("Indian",1);
    }

    private void initialiseRecyclerView(){
        mRecipeRecyclerAdapter = new RecipeRecyclerAdapter(this);
        mRecyclerView.setAdapter(mRecipeRecyclerAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }


    @Override
    public void onRecipeClick(int position) {

    }

    @Override
    public void onCategoryClick(String category) {

    }
}





