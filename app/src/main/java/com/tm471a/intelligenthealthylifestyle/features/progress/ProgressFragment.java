package com.tm471a.intelligenthealthylifestyle.features.progress;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.tm471a.intelligenthealthylifestyle.R;
import com.tm471a.intelligenthealthylifestyle.data.model.MeasurementLog;
import com.tm471a.intelligenthealthylifestyle.data.model.WeightLog;
import com.tm471a.intelligenthealthylifestyle.data.model.WorkoutLog;
import com.tm471a.intelligenthealthylifestyle.databinding.FragmentProgressBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

        YAxis yAxis = chart.getAxisLeft();
        yAxis.setGranularity(1f);
        yAxis.setAxisMinimum(0f);

        chart.getAxisRight().setEnabled(false);
        setupDateAxis(xAxis);
    }

    private void setupWorkoutChart() {
        BarChart chart = binding.workoutChart;
        chart.getDescription().setText("Workout Sessions");

        XAxis xAxis = chart.getXAxis();
        setupDateAxis(xAxis);

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
        setupDateAxis(xAxis);

        YAxis yAxis = chart.getAxisLeft();
        yAxis.setGranularity(1f);
        yAxis.setAxisMinimum(0f);

        chart.getAxisRight().setEnabled(false);
    }
    private void setupDateAxis(XAxis xAxis) {
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(86400f); // 1 day in seconds
        xAxis.setValueFormatter(new DateAxisFormatter());
        xAxis.setLabelRotationAngle(-45);
        xAxis.setDrawGridLines(false);
    }


    private void setupObservers() {
        viewModel.getWeightLogs().observe(getViewLifecycleOwner(), this::updateWeightChart);
        viewModel.getWorkoutLogs().observe(getViewLifecycleOwner(), this::updateWorkoutChart);
        viewModel.getMeasurementLogs().observe(getViewLifecycleOwner(), this::updateMeasurementChart);
    }

    private void updateWeightChart(List<WeightLog> weightLogs) {
        List<Entry> entries = new ArrayList<>();
        for (WeightLog log : weightLogs) {
            float timestamp = (float) log.getDate().toDate().getTime();
            entries.add(new Entry(log.getDate().toDate().getTime(), (float) log.getWeight()));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Weight (kg)");
        configureLineDataSet(dataSet, ContextCompat.getColor(getContext(), R.color.primary_color));

        binding.weightChart.setData(new LineData(dataSet));
        binding.weightChart.invalidate();
    }

    private void updateWorkoutChart(List<WorkoutLog> workoutLogs) {
        List<BarEntry> entries = new ArrayList<>();
        for (WorkoutLog log: workoutLogs) {
            float timestamp = (float) log.getDate().toDate().getTime();
            entries.add(new BarEntry(log.getDate().toDate().getTime(), log.getCount()));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Workouts");
        dataSet.setColor(ContextCompat.getColor(getContext(), R.color.primary_color));

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(3600000);

        binding.workoutChart.setData(barData);
        binding.workoutChart.invalidate();
    }

    private void updateMeasurementChart(List<MeasurementLog> measurementLogs) {
        List<Entry> chestEntries = new ArrayList<>();
        List<Entry> waistEntries = new ArrayList<>();
        List<Entry> hipsEntries = new ArrayList<>();

        for (MeasurementLog log: measurementLogs) {
            float timestamp = (float) log.getDate().toDate().getTime();

            chestEntries.add(new Entry(log.getDate().toDate().getTime(),(float) log.getChest()));
            waistEntries.add(new Entry(log.getDate().toDate().getTime(),(float) log.getWaist()));
            hipsEntries.add(new Entry(log.getDate().toDate().getTime(),(float) log.getHips()));
        }

        LineDataSet chestDataSet = new LineDataSet(chestEntries, "Chest");
        chestDataSet.setColor(Color.CYAN);

        LineDataSet waistDataSet = new LineDataSet(waistEntries, "Waist");
        waistDataSet.setColor(ContextCompat.getColor(getContext(), R.color.primary_color));

        LineDataSet hipsDataSet = new LineDataSet(hipsEntries, "Hips");
        hipsDataSet.setColor(ContextCompat.getColor(getContext(), R.color.accent_color));

        binding.measurementChart.setData(new LineData(chestDataSet, waistDataSet, hipsDataSet));
        binding.measurementChart.invalidate();
    }
    private void configureLineDataSet(LineDataSet dataSet, int color) {
        dataSet.setColor(color);
        dataSet.setCircleColor(color);
        dataSet.setCircleRadius(4f);
        dataSet.setLineWidth(2f);
        dataSet.setValueTextSize(10f);
        dataSet.setDrawValues(false);
    }
    private long getBaseTimestamp() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2025, Calendar.MARCH, 1, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }
    public class DateAxisFormatter extends ValueFormatter {
        private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());

        @Override
        public String getFormattedValue(float value) {
            long millis = (long) value;
            return dateFormat.format(new Date(millis));
        }
    }
}