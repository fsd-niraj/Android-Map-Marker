package com.example.mapsandroid;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class AddLocationFormSheet extends BottomSheetDialogFragment {

    private double lang, lat;
    private HandleBottomSheetBehaviour listener;

    public AddLocationFormSheet(double lang, double lat, HandleBottomSheetBehaviour listener) {
        this.lang = lang;
        this.lat = lat;
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.location_save_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextInputEditText title = view.findViewById(R.id.loc_title);
        TextInputEditText description = view.findViewById(R.id.loc_description);
        RatingBar rating = view.findViewById(R.id.loc_rating);
        PreferenceManager preferenceManager = new PreferenceManager(getActivity());
        view.findViewById(R.id.btn_save).setOnClickListener(v -> {
            String titleText = title.getText().toString();
            if (titleText == null || titleText.equals("")) {
                Toast.makeText(getActivity(), "Please enter a title", Toast.LENGTH_SHORT).show();
                return;
            }
            ArrayList<Location> locations = preferenceManager.getTodoList();
            locations.add(new Location(
                    title.getText().toString(),
                    description.getText().toString(),
                    rating.getRating(),
                    this.lat,
                    this.lang
                    )
            );
            preferenceManager.saveTodoList(locations);
            listener.handleClose();
            dismiss();
        });
        view.findViewById(R.id.btn_cancel).setOnClickListener(v -> dismiss());
    }
}