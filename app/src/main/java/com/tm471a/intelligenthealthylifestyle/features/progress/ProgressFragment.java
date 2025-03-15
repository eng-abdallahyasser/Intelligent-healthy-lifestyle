package com.tm471a.intelligenthealthylifestyle.features.progress;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.tm471a.intelligenthealthylifestyle.data.model.MeasurementLog;
import com.tm471a.intelligenthealthylifestyle.data.model.WeightLog;
import com.tm471a.intelligenthealthylifestyle.data.model.WorkoutLog;
import com.tm471a.intelligenthealthylifestyle.databinding.FragmentProgressBinding;

import java.util.ArrayList;
import java.util.List;

public class ProgressFragment extends Fragment {

    private FragmentProgressBinding binding;
    private ProgressViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProgressBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(ProgressViewModel.class);

        setupInputHandlers();
        setupCharts();
        setupObservers();

        return binding.getRoot();
    }

    private void setupInputHandlers() {
        binding.btnLogWeight.setOnClickListener(v -> {
            String weightStr = binding.etWeight.getText().toString();
            if (!weightStr.isEmpty()) {
                float weight = Float.parseFloat(weightStr);
                viewModel.saveWeight(weight);
                binding.etWeight.setText("");
                showToast("Weight logged successfully!");
            } else {
                showToast("Please enter weight");
            }
        });

        binding.btnLogWorkout.setOnClickListener(v -> {
            String countStr = binding.etWorkoutCount.getText().toString();
            if (!countStr.isEmpty()) {
                int count = Integer.parseInt(countStr);
                viewModel.saveWorkout(count);
                binding.etWorkoutCount.setText("");
                showToast("Workout logged successfully!");
            } else {
                showToast("Please enter workout count");
            }
        });

        binding.btnLogMeasurements.setOnClickListener(v -> {
            String chestStr = binding.etChest.getText().toString();
            String waistStr = binding.etWaist.getText().toString();
            String hipsStr = binding.etHips.getText().toString();

            if (!chestStr.isEmpty() && !waistStr.isEmpty() && !hipsStr.isEmpty()) {
                float chest = Float.parseFloat(chestStr);
                float waist = Float.parseFloat(waistStr);
                float hips = Float.parseFloat(hipsStr);

                viewModel.saveBodyMeasurement(chest, waist, hips);
                clearMeasurementFields();
                showToast("Measurements logged successfully!");
            } else {
                showToast("Please fill all measurement fields");
            }
        });
    }

    private void clearMeasurementFields() {
        binding.etChest.setText("");
        binding.etWaist.setText("");
        binding.etHips.setText("");
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
    private void setupCharts() {
        setupWeightChart();
        setupWorkoutChart();
        setupMeasurementChart();
    }

    private void setupWeightChart() {
        LineChart chart = binding.weightChart;
        chart.getDescription().setText("Weight Progress (kg)");
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);

        YAxis yAxis = chart.getAxisLeft();
        yAxis.setGranularity(1f);
        yAxis.setAxisMinimum(0f);

        chart.getAxisRight().setEnabled(false);
    }

    private void setupWorkoutChart() {
        BarChart chart = binding.workoutChart;
        chart.getDescription().setText("Workout Sessions");

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);

        YAxis yAxis = chart.getAxisLeft();
        yAxis.setGranularity(1f);
        yAxis.setAxisMinimum(0f);

        chart.getAxisRight().setEnabled(false);
    }

    private void setupMeasurementChart() {
        LineChart chart = binding.measurementChart;
        chart.getDescription().setText("Body Measurements (cm)");
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);

        YAxis yAxis = chart.getAxisLeft();
        yAxis.setGranularity(1f);
        yAxis.setAxisMinimum(0f);

        chart.getAxisRight().setEnabled(false);
    }

    private void setupObservers() {
        viewModel.getWeightLogs().observe(getViewLifecycleOwner(), this::updateWeightChart);
        viewModel.getWorkoutLogs().observe(getViewLifecycleOwner(), this::updateWorkoutChart);
        viewModel.getMeasurementLogs().observe(getViewLifecycleOwner(), this::updateMeasurementChart);
    }

    private void updateWeightChart(List<WeightLog> weightLogs) {
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < weightLogs.size(); i++) {
            entries.add(new Entry(i,(float) weightLogs.get(i).getWeight()));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Weight");
        dataSet.setColor(Color.BLUE);
        dataSet.setCircleColor(Color.BLUE);

        binding.weightChart.setData(new LineData(dataSet));
        binding.weightChart.invalidate();
    }

    private void updateWorkoutChart(List<WorkoutLog> workoutLogs) {
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < workoutLogs.size(); i++) {
            entries.add(new BarEntry(i, workoutLogs.get(i).getCount()));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Workouts");
        dataSet.setColor(Color.GREEN);

        binding.workoutChart.setData(new BarData(dataSet));
        binding.workoutChart.invalidate();
    }

    private void updateMeasurementChart(List<MeasurementLog> measurementLogs) {
        List<Entry> chestEntries = new ArrayList<>();
        List<Entry> waistEntries = new ArrayList<>();
        List<Entry> hipsEntries = new ArrayList<>();

        for (int i = 0; i < measurementLogs.size(); i++) {
            MeasurementLog log = measurementLogs.get(i);
            chestEntries.add(new Entry(i,(float) log.getChest()));
            waistEntries.add(new Entry(i,(float) log.getWaist()));
            hipsEntries.add(new Entry(i,(float) log.getHips()));
        }

        LineDataSet chestDataSet = new LineDataSet(chestEntries, "Chest");
        chestDataSet.setColor(Color.RED);

        LineDataSet waistDataSet = new LineDataSet(waistEntries, "Waist");
        waistDataSet.setColor(Color.BLUE);

        LineDataSet hipsDataSet = new LineDataSet(hipsEntries, "Hips");
        hipsDataSet.setColor(Color.GREEN);

        binding.measurementChart.setData(new LineData(chestDataSet, waistDataSet, hipsDataSet));
        binding.measurementChart.invalidate();
    }
}