package com.example.android.foodrecipes.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

public class Recipe implements Parcelable {

    private String title;
    private String publisher;
    private String[] ingredients;
    private String id;
    private String imageUrl;
    private String image_url;
    private float socialUrl;
    private String recipe_id;

    public void setrecipeId(String rID){
        recipe_id = rID;

    }
    public String gerecipeId(){
        return recipe_id;
    }

    public void setimage_url(String image){
        image_url = image;

    }
    public String getimage_url(){
        return image_url;
    }

    public Recipe(String title, String publisher, String[] ingredients, String recipe_id, String image_url, float social_rank, String image_url2, String recipeid ) {
        this.title = title;
        this.publisher = publisher;
        this.ingredients = ingredients;
        this.id = recipe_id;
        this.imageUrl = image_url;
        this.socialUrl = social_rank;
        this.image_url = image_url2;
        this.recipe_id = recipeid;
    }

    public Recipe() {
    }

    protected Recipe(Parcel in) {
        title = in.readString();
        publisher = in.readString();
        ingredients = in.createStringArray();
        id = in.readString();
        imageUrl = in.readString();
        socialUrl = in.readFloat();
        image_url = in.readString();
        recipe_id = in.readString();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage_url() {
        return imageUrl;
    }

    public void setImage_url(String image_url) {
        this.imageUrl = image_url;
    }

    public float getSocialUrl() {
        return socialUrl;
    }

    public void setSocialUrl(float socialUrl) {
        this.socialUrl = socialUrl;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "title='" + title + '\'' +
                ", publisher='" + publisher + '\'' +
                ", ingredients=" + Arrays.toString(ingredients) +
                ", recipe_id='" + id + '\'' +
                ", image_url='" + imageUrl + '\'' +
                ", social_rank=" + socialUrl +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(publisher);
        dest.writeStringArray(ingredients);
        dest.writeString(id);
        dest.writeString(imageUrl);
        dest.writeFloat(socialUrl);
        dest.writeString(image_url);
        dest.writeString(recipe_id);
    }
}
