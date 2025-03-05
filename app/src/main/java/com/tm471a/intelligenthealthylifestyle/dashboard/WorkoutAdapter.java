package com.tm471a.intelligenthealthylifestyle.dashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tm471a.intelligenthealthylifestyle.R;
import com.tm471a.intelligenthealthylifestyle.data.model.WorkoutPlan;

import java.util.List;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.ViewHolder> {

    private List<WorkoutPlan> workoutPlans;

    public WorkoutAdapter(List<WorkoutPlan> workoutPlans) {
        this.workoutPlans = workoutPlans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_workout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WorkoutPlan plan = workoutPlans.get(position);
        holder.planName.setText(plan.getPlanName());
        holder.duration.setText("Duration: " + plan.getDuration());
        holder.difficulty.setText("Level: " + plan.getDifficulty());
    }

    @Override
    public int getItemCount() {
        return workoutPlans.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView planName, duration, difficulty;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            planName = itemView.findViewById(R.id.tv_plan_name);
            duration = itemView.findViewById(R.id.tv_duration);
            difficulty = itemView.findViewById(R.id.tv_difficulty);
        }
    }
}