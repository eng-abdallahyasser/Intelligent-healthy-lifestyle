package com.tm471a.intelligenthealthylifestyle.features.myplan;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tm471a.intelligenthealthylifestyle.R;
import com.tm471a.intelligenthealthylifestyle.data.model.WorkoutDay;

import java.util.List;

public class WorkoutDayAdapter extends RecyclerView.Adapter<WorkoutDayAdapter.ViewHolder> {
    private final List<WorkoutDay> workoutDays;
    private final MyPlanViewModel viewModel;

    public WorkoutDayAdapter(List<WorkoutDay> suggestions, MyPlanViewModel viewModel) {
        this.workoutDays = suggestions;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_workout_day, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        WorkoutDay workoutDay = workoutDays.get(position);

        viewModel.subscribedPlan.observe((LifecycleOwner) holder.itemView.getContext(), workoutPlan -> {
            if(workoutDay.getDayCompleted()){
                holder.textViewDay.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.primary_color));
                holder.textViewDay.setText(workoutDay.getDay()+" (Completed)");
            }else{
                holder.textViewDay.setText(workoutDay.getDay());
                holder.textViewDay.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.slate_grey_color));
            }
        });
        ExerciseAdapter exerciseAdapter = new ExerciseAdapter(workoutDay,viewModel);
        holder.exerciseRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.exerciseRecyclerView.setAdapter(exerciseAdapter);
    }

    @Override
    public int getItemCount() {
        return workoutDays.size();
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