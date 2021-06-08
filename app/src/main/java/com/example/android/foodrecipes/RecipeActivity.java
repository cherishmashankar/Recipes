package com.example.android.foodrecipes;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.android.foodrecipes.adapters.RecipeViewHolder;
import com.example.android.foodrecipes.models.Recipe;
import com.example.android.foodrecipes.viewmodels.RecipeViewModel;

public class RecipeActivity extends BaseActivity {
    private static final String TAG = "RecipeActivity";

    private ImageView mImage;
    private TextView mTitle;

    private TextView mSocialRank;
    private LinearLayout mIngredientLayout;
    private ScrollView mScrollView;

    private RecipeViewModel mRecipeViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        mImage = findViewById(R.id.recipe_image);
        mTitle = findViewById(R.id.recipe_title);
        mSocialRank = findViewById(R.id.recipe_social_score);
        mIngredientLayout = findViewById(R.id.ingredients_container);
        mScrollView = findViewById(R.id.parent);

        mRecipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
        showProgressBar(true);

        getIncomingIntent();
       subscribeObserver();
    }

    private void getIncomingIntent(){
        if(getIntent().hasExtra("recipe")){
            Recipe  recipe = getIntent().getParcelableExtra("recipe");

            assert recipe != null;
            mRecipeViewModel.searchRecipeById(recipe.getId());


            mIngredientLayout.removeAllViews();




        }
    }

    private void subscribeObserver(){
        mRecipeViewModel.getRecipe().observe(this, new Observer<Recipe>() {
            @Override
            public void onChanged(Recipe recipe) {
                if(recipe != null){

                    if(recipe.gerecipeId().equals(mRecipeViewModel.getRecipeId())){
                        setRecipeProperties(recipe);
                        mRecipeViewModel.setmDidRetrieveData(true);
                    }
                }
                }

        });

        mRecipeViewModel.getIsTimeOut().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean && !mRecipeViewModel.getmDidRetrieveData()){
                    Log.e(TAG, "onChanged:------------------TIME OUT" );
                    displayErrorMessage("Network Error Please check Internet Connection ");
                }
            }
        });
    }

    public void displayErrorMessage(String errorMessage){
        RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.ic_launcher_background);

        Glide.with(this)
                .setDefaultRequestOptions(requestOptions)
                .load(R.drawable.ic_launcher_background)
                .into(mImage);
        //Log.e(TAG, "setRecipeProperties: " +  recipe.getImage_url());

        mTitle.setText("Error retrieving Data");
        mSocialRank.setText("");
        mIngredientLayout.removeAllViews();
        TextView textView = new TextView(this);
        if(!errorMessage.equals("")){
            textView.setText(errorMessage);
        }else{
            textView.setText("Error Please Check The Internet");
        }
        textView.setTextSize(30);
        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mIngredientLayout.addView(textView);

        showParent();
        showProgressBar(false);
        //mRecipeViewModel.setmDidRetrieveData(true);

    }

    private void setRecipeProperties(Recipe recipe) {
            RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.ic_launcher_background);

            Glide.with(this)
                    .setDefaultRequestOptions(requestOptions)
                    .load(recipe.getimage_url())
                    .into(mImage);
        //Log.e(TAG, "setRecipeProperties: " +  recipe.getImage_url());

            mTitle.setText(recipe.getTitle());
            mSocialRank.setText(String.valueOf(Math.round(recipe.getSocialUrl())));
            mIngredientLayout.removeAllViews();

            for (String ingredients : recipe.getIngredients()) {
                TextView textView = new TextView(this);
                textView.setText(ingredients);
                textView.setTextSize(15);
                textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                mIngredientLayout.addView(textView);

            }
            showParent();
            showProgressBar(false);

    }

    private void showParent() {
        mScrollView.setVisibility(View.VISIBLE);
    }
}