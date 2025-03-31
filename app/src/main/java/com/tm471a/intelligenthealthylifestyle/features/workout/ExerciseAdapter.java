package com.tm471a.intelligenthealthylifestyle.features.workout;

import android.annotation.SuppressLint;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tm471a.intelligenthealthylifestyle.R;
import com.tm471a.intelligenthealthylifestyle.data.model.Exercise;

import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ViewHolder> {
    private List<Exercise> exerciseList;
    private SparseBooleanArray showDescription = new SparseBooleanArray();


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

    @SuppressLint({"SetTextI18n", "SuspiciousIndentation"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Exercise exercise = exerciseList.get(position);

        // Set click listener on the card
        holder.cardBorderContainer.setOnClickListener(v -> {
            boolean isExpanded = showDescription.get(position, false);
            showDescription.put(position, !isExpanded);
            notifyItemChanged(position);
        });
        // Control expansion state
        if (showDescription.get(position, false)) {
            // Expanded state
            holder.tvFullDescription.setVisibility(View.VISIBLE);
            holder.tvFullDescription.setText(exercise.getDescription());
            holder.tvSetsReps.setVisibility(View.GONE);
            holder.tvEquipment.setVisibility(View.GONE);
            holder.tvPrimaryMuscles.setVisibility(View.GONE);
        } else {
            // Collapsed state
            holder.tvFullDescription.setVisibility(View.GONE);
        }

        // Load GIF using Glide
        Glide.with(holder.itemView.getContext())
                .load(exercise.getUrl())
                .into(holder.ivExerciseGif);

        holder.tvExerciseName.setText(exercise.getName());
        holder.tvFullDescription.setText(exercise.getDescription());
        holder.ibDone.setVisibility(View.GONE);
        if(exercise.getPrimaryMuscles()!=null)
        {
        holder.tvPrimaryMuscles.setText("Primary Muscles: " + String.join(", ", exercise.getPrimaryMuscles()));
        }
        holder.tvEquipment.setText("Equipment: " + String.join(", ", exercise.getEquipment()));
        holder.tvSetsReps.setText("Sets: " + exercise.getSets() + " | Reps: " + exercise.getReps());
    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvExerciseName, tvPrimaryMuscles, tvEquipment, tvSetsReps;
        ImageButton ibDone;
        FrameLayout cardBorderContainer;
        ImageView ivExerciseGif;
        TextView tvFullDescription;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvExerciseName = itemView.findViewById(R.id.tvExerciseName);
            tvPrimaryMuscles = itemView.findViewById(R.id.tvPrimaryMuscles);
            tvEquipment = itemView.findViewById(R.id.tvEquipment);
            tvSetsReps = itemView.findViewById(R.id.tvSetsReps);
            ibDone=itemView.findViewById(R.id.ib_done);
            cardBorderContainer = itemView.findViewById(R.id.cardBorderContainer);
            ivExerciseGif = itemView.findViewById(R.id.ivExerciseGif);
            tvFullDescription = itemView.findViewById(R.id.tvFullDescription);
            cardView = itemView.findViewById(R.id.cv_exercise);
        }
    }
}
