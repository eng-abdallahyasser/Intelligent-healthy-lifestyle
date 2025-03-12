package com.tm471a.intelligenthealthylifestyle.features.assistant;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.tm471a.intelligenthealthylifestyle.databinding.FragmentAssistantBinding;

import java.util.Objects;

public class AssistantFragment extends Fragment {

    private FragmentAssistantBinding binding;

    private AssistantViewModel viewModel;
    ChatAdapter adapter = new ChatAdapter();


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAssistantBinding.inflate(inflater, container, false);
        AssistantViewModelFactory factory = new AssistantViewModelFactory(requireActivity().getApplication());
        viewModel = new ViewModelProvider(this, factory).get(AssistantViewModel.class);

        setupChat();
        setupObservers();
        return binding.getRoot();

    }

    private void setupChat() {
        binding.rvMessages.setAdapter(adapter);
        binding.rvMessages.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.submitList(viewModel.getMessages().getValue());

        // Input layout height observer
        binding.inputLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        int inputHeight = binding.inputLayout.getHeight();
                        binding.rvMessages.setPadding(0, 0, 0, inputHeight);

                        // Remove listener after first measurement
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            binding.inputLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        } else {
                            binding.inputLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        }
                    }
                }
        );
        // Send button click listener
        binding.btnSend.setOnClickListener(v -> {
            String message = binding.etMessage.getText().toString();
            if (!message.isEmpty()) {
                viewModel.sendMessage(message);
                binding.etMessage.setText("");
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setupObservers() {
        viewModel.getMessages().observe(getViewLifecycleOwner(), messages -> {
            adapter.submitList(messages);
            Objects.requireNonNull(binding.rvMessages.getAdapter()).notifyDataSetChanged();
            binding.rvMessages.scrollToPosition(messages.size() - 1);
        });

        viewModel.getLoadingStatus().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null) {
                if (isLoading) {
                    // Show loading indicator
                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.btnSend.setEnabled(false);
                } else {
                    // Hide loading indicator
                    binding.progressBar.setVisibility(View.GONE);
                    binding.btnSend.setEnabled(true);
                }
            }
        });
    }
}