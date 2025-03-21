package com.tm471a.intelligenthealthylifestyle.features.workout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tm471a.intelligenthealthylifestyle.R;
import com.tm471a.intelligenthealthylifestyle.data.model.Suggestion;
import com.tm471a.intelligenthealthylifestyle.data.model.WorkoutDay;
import com.tm471a.intelligenthealthylifestyle.features.nutrition.MealAdapter;
import com.tm471a.intelligenthealthylifestyle.features.nutrition.MealSuggestionAdapter;

import java.util.List;

public class WorkoutDayAdapter extends RecyclerView.Adapter<WorkoutDayAdapter.ViewHolder> {
    private final List<WorkoutDay> suggestions;

    public WorkoutDayAdapter(List<WorkoutDay> suggestions) {
        this.suggestions = suggestions;
    }

    @NonNull
    @Override
    public WorkoutDayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_workout_day, parent, false);
        return new WorkoutDayAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        WorkoutDay workoutDay = suggestions.get(position);
        holder.textViewDay.setText(workoutDay.getDay());

        ExerciseAdapter mealAdapter = new ExerciseAdapter(workoutDay.getExercises());
        holder.exerciseRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.exerciseRecyclerView.setAdapter(mealAdapter);
    }


    @Override
    public int getItemCount() {
        return suggestions.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDay;
        RecyclerView exerciseRecyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDay = itemView.findViewById(R.id.tv_day_name);
            exerciseRecyclerView = itemView.findViewById(R.id.rv_exercises);
        }
    }
}