package com.tm471a.intelligenthealthylifestyle.features.nutrition;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tm471a.intelligenthealthylifestyle.R;
import com.tm471a.intelligenthealthylifestyle.data.model.Suggestion;

import java.util.List;

public class MealSuggestionAdapter extends RecyclerView.Adapter<MealSuggestionAdapter.CategoryViewHolder> {
    private final List<Suggestion> suggestions;

    public MealSuggestionAdapter(List<Suggestion> suggestions) {
        this.suggestions = suggestions;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meal_suggestion, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Suggestion suggestion = suggestions.get(position);
        holder.categoryName.setText(suggestion.getMeal());

        MealAdapter mealAdapter = new MealAdapter(suggestion.getSuggestions());
        holder.mealsRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.mealsRecyclerView.setAdapter(mealAdapter);
    }

    @Override
    public int getItemCount() {
        return suggestions.size();
    }
    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        RecyclerView mealsRecyclerView;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.tv_suggestion_name);
            mealsRecyclerView = itemView.findViewById(R.id.meals_recycler_view);
        }
    }
}
