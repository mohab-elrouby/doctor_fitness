package com.example.fitnessdoctor;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.ekn.gruzer.gaugelibrary.ArcGauge;

import java.text.DecimalFormat;


public class StepCounterFragment extends Fragment {
    EditText etMeters;
    Button btnStart;
    ArcGauge arcGauge;
    NavController navController;
    private static DecimalFormat df = new DecimalFormat("0");

    public StepCounterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_step_counter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etMeters = view.findViewById(R.id.et_meters);
        btnStart = view.findViewById(R.id.btn_start);
        arcGauge = view.findViewById(R.id.progress_circular);
        navController = Navigation.findNavController(view);


        btnStart.setOnClickListener(v -> {
            double numberOfSteps;
            double meters=0;
            try {
                meters = Double.parseDouble(etMeters.getText().toString());
            }
            catch (NumberFormatException e){e.printStackTrace();}
            numberOfSteps = meters*1.31;
            arcGauge.setMaxValue(Double.parseDouble(df.format(numberOfSteps)));
            arcGauge.setValue(0);
        });
    }
}