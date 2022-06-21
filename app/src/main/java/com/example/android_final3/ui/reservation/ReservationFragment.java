package com.example.android_final3.ui.reservation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.android_final3.databinding.FragmentReservationBinding;

public class ReservationFragment extends Fragment {

    private FragmentReservationBinding binding;

    private ListView list;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        ReservationModel reservationModel =
//                new ViewModelProvider(this).get(ReservationModel.class);
//
//        binding = FragmentReservationBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();
//
//        final TextView textView = binding.txtHome;
//        reservationModel.getText().observe(getViewLifecycleOwner(), textView::setText);
//        return root;
        binding = FragmentReservationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //list = (ListView)root.findViewById(com.example.android_final3.ui.reservation.id.list);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        list = (ListView)view.findViewById(com.example.android_final3.ui.history.)
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}