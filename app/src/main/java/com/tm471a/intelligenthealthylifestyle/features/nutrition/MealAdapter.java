package com.tm471a.intelligenthealthylifestyle.features.nutrition;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tm471a.intelligenthealthylifestyle.R;
import com.tm471a.intelligenthealthylifestyle.data.model.Meal;

import java.util.List;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealViewHolder> {
    private List<Meal> meals;

    public MealAdapter(List<Meal> meals) {
        this.meals = meals;
        Log.d("MealAdapter", "MealAdapter() called   meal count = "+meals.size());
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meal, parent, false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {

        Meal meal = meals.get(position);
        Log.d("MealAdapter", "Binding meal: " + meal.getName());
        holder.tvMealName.setText(meal.getName());
        holder.tvCalories.setText(meal.getCalories());
        holder.tvIngredients.setText(meal.getIngredients().toString());
    }

    @Override
    public int getItemCount() {
        Log.d("MealAdapter", "getItemCount() called - size: " + meals.size());
        return meals.size();
    }

    static class MealViewHolder extends RecyclerView.ViewHolder {
        private TextView tvMealName;
        private TextView tvCalories;
        private TextView tvIngredients;
        private RecyclerView rvSuggestions;

        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMealName= itemView.findViewById(R.id.tvMealName);
            tvCalories= itemView.findViewById(R.id.tvCalories);
            tvIngredients= itemView.findViewById(R.id.tvIngredients);
        }

        public void bind(Meal meal) {
            tvMealName.setText(meal.getName());
            tvCalories.setText(meal.getCalories());
            tvIngredients.setText(meal.getIngredients().toString());
        }
    }
}