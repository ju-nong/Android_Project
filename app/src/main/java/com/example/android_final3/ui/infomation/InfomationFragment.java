package com.example.android_final3.ui.infomation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.android_final3.databinding.FragmentInfomationBinding;

public class InfomationFragment extends Fragment {

    private FragmentInfomationBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        InfomationViewModel infomationViewModel =
//                new ViewModelProvider(this).get(InfomationViewModel.class);
//
//        binding = FragmentInfomationBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();
//
//        final TextView textView = binding.textNotifications;
//        infomationViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
//        return root;

        binding = FragmentInfomationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}