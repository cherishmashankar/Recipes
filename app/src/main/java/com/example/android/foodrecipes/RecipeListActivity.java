package com.example.android.foodrecipes;

import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    private androidx.appcompat.widget.SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        mRecipeListViewModel = ViewModelProviders.of(this).get(RecipeListViewModel.class);
        mRecyclerView = findViewById(R.id.recipe_list_recyclerView);
        mSearchView = findViewById(R.id.search_view);

        initialiseRecyclerView();
        subscribeObserver();
        initSearchView();

        if(!mRecipeListViewModel.getIsViewingRecipe()){
            displaySearchCategory();
        }

        setSupportActionBar((Toolbar) findViewById(R.id.tool_bar));

    }

    @Override
    public void onBackPressed() {
        if (mRecipeListViewModel.ifBackPressed()) {
            super.onBackPressed();
        } else{

            displaySearchCategory();
    }
    }

    private void subscribeObserver(){
        mRecipeListViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                if(recipes != null  && mRecipeListViewModel.getIsViewingRecipe()) {
                    mRecipeListViewModel.setIsPerformingQuery(false);
                    mRecipeRecyclerAdapter.setRecipes(recipes);
                }

            }
        });

        mRecipeListViewModel.getIsDataExhausted().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    Log.e(TAG, "-------------------------------------------------Exhausted" );
                    mRecipeRecyclerAdapter.displayExhausted();
                }
            }
        });
    }

    private void initialiseRecyclerView(){
        mRecipeRecyclerAdapter = new RecipeRecyclerAdapter(this);
        mRecyclerView.setAdapter(mRecipeRecyclerAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if(!mRecyclerView.canScrollVertically(1)){

                    //search next page
                    mRecipeListViewModel.searchNextPage();
                }
            }


        });

    }

    private void initSearchView(){

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mRecipeRecyclerAdapter.displayLoading();
                mRecipeListViewModel.searchRecipeApi(query,1);
                mSearchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    private void displaySearchCategory(){
        mRecipeListViewModel.setIsViewingRecipe(false);
        mRecipeRecyclerAdapter.searchCategories();
    }

    @Override
    public void onRecipeClick(int position) {
        Log.e(TAG, "onRecipeClick: "+ position );
        Intent recipeIntent = new Intent(this, RecipeActivity.class);

        recipeIntent.putExtra("recipe", mRecipeRecyclerAdapter.getSelectedItem(position));
        startActivity(recipeIntent);

    }

    @Override
    public void onCategoryClick(String category) {
        mRecipeRecyclerAdapter.displayLoading();
        mRecipeListViewModel.searchRecipeApi(category,1);
        mSearchView.clearFocus();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_list_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_categories) {
            displaySearchCategory();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}





