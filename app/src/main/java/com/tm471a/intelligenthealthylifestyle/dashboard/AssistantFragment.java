package com.tm471a.intelligenthealthylifestyle.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.tm471a.intelligenthealthylifestyle.data.model.ChatMessage;
import com.tm471a.intelligenthealthylifestyle.databinding.FragmentAssistantBinding;

public class AssistantFragment extends Fragment {

    private FragmentAssistantBinding binding;
    private AssistantViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAssistantBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(AssistantViewModel.class);

        setupChat();
        setupObservers();

        return binding.getRoot();
    }

    private void setupChat() {
        // Initialize
        ChatAdapter adapter = new ChatAdapter();
        binding.rvMessages.setAdapter(adapter);
        binding.rvMessages.setLayoutManager(new LinearLayoutManager(getContext()));

// Add messages
        adapter.addMessage(new ChatMessage("Hello!", false));
        adapter.showLoading(); // Show loading indicator

// When response received
        adapter.hideLoading();
        adapter.addMessage(new ChatMessage("Hi there!", true));

        binding.btnSend.setOnClickListener(v -> {
            String message = binding.etMessage.getText().toString();
            if (!message.isEmpty()) {
                viewModel.sendMessage(message);
                binding.etMessage.setText("");
            }
        });
    }

    private void setupObservers() {
        viewModel.getMessages().observe(getViewLifecycleOwner(), messages -> {
            binding.rvMessages.getAdapter().notifyDataSetChanged();
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