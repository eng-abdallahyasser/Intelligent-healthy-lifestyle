package com.tm471a.intelligenthealthylifestyle.features.myplan;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tm471a.intelligenthealthylifestyle.R;
import com.tm471a.intelligenthealthylifestyle.data.model.Exercise;
import com.tm471a.intelligenthealthylifestyle.data.model.WorkoutDay;

import java.util.ArrayList;
import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ViewHolder> {
    private WorkoutDay workoutDay;
    private MyPlanViewModel viewModel;
     private List<Boolean> exerciseCompleted ;

    public ExerciseAdapter(WorkoutDay workoutDay, MyPlanViewModel viewModel) {
        this.workoutDay=workoutDay;
        this.exerciseCompleted = workoutDay.getExerciseCompleted();
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_exercise, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Exercise exercise = workoutDay.getExercises().get(position);

        holder.tvExerciseName.setText(exercise.getName());
        holder.tvExerciseDescription.setText(exercise.getDescription());

        // Handle potential null values
        String primaryMuscles = (exercise.getPrimaryMuscles() != null) ? String.join(", ", exercise.getPrimaryMuscles()) : "N/A";
        String equipment = (exercise.getEquipment() != null) ? String.join(", ", exercise.getEquipment()) : "N/A";

        holder.ibDone.setOnClickListener(v -> {
            exerciseCompleted.set(position, !exerciseCompleted.get(position));
            if (viewModel != null) {
                viewModel.updateExerciseCompletion(position, workoutDay.getDay(), exerciseCompleted.get(position));
            }
            notifyItemChanged(position);
        });

        if (exerciseCompleted.get(position)) {
            holder.ibDone.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.getContext(), R.color.primary_color)));

            GradientDrawable borderDrawable = new GradientDrawable();
            borderDrawable.setColor(Color.TRANSPARENT); // Keep the background transparent
            borderDrawable.setStroke(8, ContextCompat.getColor(holder.itemView.getContext(), R.color.primary_color)); // Set border color
            borderDrawable.setCornerRadius(32f); // Adjust radius for a smoother border

            holder.cardBorderContainer.setBackground(borderDrawable);
        } else {
            holder.ibDone.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.getContext(), R.color.slate_grey_color)));

            GradientDrawable borderDrawable = new GradientDrawable();
            borderDrawable.setColor(Color.TRANSPARENT);
            borderDrawable.setStroke(8, ContextCompat.getColor(holder.itemView.getContext(), android.R.color.transparent)); // No visible border

            holder.cardBorderContainer.setBackground(borderDrawable);
        }
        holder.tvPrimaryMuscles.setText("Primary Muscles: " + primaryMuscles);
        holder.tvEquipment.setText("Equipment: " + equipment);
        holder.tvSetsReps.setText("Sets: " + exercise.getSets() + " | Reps: " + exercise.getReps());
    }

    @Override
    public int getItemCount() {
        return exerciseCompleted.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvExerciseName, tvExerciseDescription, tvPrimaryMuscles, tvEquipment, tvSetsReps;
        ImageButton ibDone;
        FrameLayout cardBorderContainer;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvExerciseName = itemView.findViewById(R.id.tvExerciseName);
            tvExerciseDescription = itemView.findViewById(R.id.tvExerciseDescription);
            tvPrimaryMuscles = itemView.findViewById(R.id.tvPrimaryMuscles);
            tvEquipment = itemView.findViewById(R.id.tvEquipment);
            tvSetsReps = itemView.findViewById(R.id.tvSetsReps);
            ibDone = itemView.findViewById(R.id.ib_done);
            cardBorderContainer = itemView.findViewById(R.id.cardBorderContainer);
        }
    }
}
