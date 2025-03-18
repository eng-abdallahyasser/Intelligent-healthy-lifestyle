package com.tm471a.intelligenthealthylifestyle.features.myplan;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tm471a.intelligenthealthylifestyle.R;
import com.tm471a.intelligenthealthylifestyle.databinding.FragmentMyPlanBinding;


public class MyPlanFragment extends Fragment {

    private FragmentMyPlanBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMyPlanBinding.inflate(inflater, container, false);

        // Example: Set a title
        binding.tvText.setText("Settings");

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}