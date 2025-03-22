package com.tm471a.intelligenthealthylifestyle.features.workout;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tm471a.intelligenthealthylifestyle.R;
import com.tm471a.intelligenthealthylifestyle.data.model.Exercise;

import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ViewHolder> {
    private List<Exercise> exerciseList;

    public ExerciseAdapter(List<Exercise> exerciseList) {
        this.exerciseList = exerciseList;
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
        Exercise exercise = exerciseList.get(position);

        holder.tvExerciseName.setText(exercise.getName());
        holder.tvExerciseDescription.setText(exercise.getDescription());
        holder.ibDone.setVisibility(View.GONE);
        holder.tvPrimaryMuscles.setText("Primary Muscles: " + String.join(", ", exercise.getPrimaryMuscles()));
        holder.tvEquipment.setText("Equipment: " + String.join(", ", exercise.getEquipment()));
        holder.tvSetsReps.setText("Sets: " + exercise.getSets() + " | Reps: " + exercise.getReps());
    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvExerciseName, tvExerciseDescription, tvPrimaryMuscles, tvEquipment, tvSetsReps;
        ImageButton ibDone;;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvExerciseName = itemView.findViewById(R.id.tvExerciseName);
            tvExerciseDescription = itemView.findViewById(R.id.tvExerciseDescription);
            tvPrimaryMuscles = itemView.findViewById(R.id.tvPrimaryMuscles);
            tvEquipment = itemView.findViewById(R.id.tvEquipment);
            tvSetsReps = itemView.findViewById(R.id.tvSetsReps);
            ibDone=itemView.findViewById(R.id.ib_done);
        }
    }
}
