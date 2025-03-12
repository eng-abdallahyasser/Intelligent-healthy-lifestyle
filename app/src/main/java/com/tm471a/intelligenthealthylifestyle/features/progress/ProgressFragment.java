package com.tm471a.intelligenthealthylifestyle.features.progress;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.tm471a.intelligenthealthylifestyle.databinding.FragmentProgressBinding;

public class ProgressFragment extends Fragment {

    private FragmentProgressBinding binding;
    private ProgressViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProgressBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(ProgressViewModel.class);

        setupCharts();
        setupObservers();

        return binding.getRoot();
    }

    private void setupCharts() {

    }

    private void setupObservers() {

    }
}