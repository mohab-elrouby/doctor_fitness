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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.text.DecimalFormat;


public class CalorieCalculator extends Fragment {
    TextView tvMaintain, tvMildLoss, tvLoss, tvExtremeLoss;
    EditText etAge, etHeight, etWeight, etHeightFt, etHeightInch;
    Spinner heightSpinner, weightSpinner, activitySpinner;
    ToggleButton genderSelector;
    Button btnCalculate;
    Double bmr, workoutRatio;
    NavController navController;
    double lb;
    boolean ft;

    private static DecimalFormat df = new DecimalFormat("0.0");


    public CalorieCalculator() {
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
        return inflater.inflate(R.layout.fragment_calorie_calculator, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        heightSpinner = view.findViewById(R.id.height_spinner);
        weightSpinner = view.findViewById(R.id.weight_spinner);
        etHeight = view.findViewById(R.id.et_height);
        etWeight = view.findViewById(R.id.et_weight);
        etHeightFt = view.findViewById(R.id.et_height_ft);
        etHeightInch = view.findViewById(R.id.et_height_inch);
        genderSelector = view.findViewById(R.id.gender_toggle);
        etAge = view.findViewById(R.id.et_age);
        activitySpinner = view.findViewById(R.id.activity_spinner);
        btnCalculate = view.findViewById(R.id.btn_calculate);
        tvMaintain = view.findViewById(R.id.tv_maintain);
        tvMildLoss = view.findViewById(R.id.tv_mild_loss);
        tvLoss = view.findViewById(R.id.tv_weight_loss);
        tvExtremeLoss = view.findViewById(R.id.tv_extreme_weight_loss);
        navController = Navigation.findNavController(view);

        ArrayAdapter<CharSequence> heightAdapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.height_array, android.R.layout.simple_spinner_item);
        heightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        heightSpinner.setAdapter(heightAdapter);
        heightSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        ft = false;
                        etHeight.setVisibility(View.VISIBLE);
                        etHeightFt.setVisibility(View.INVISIBLE);
                        etHeightInch.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        ft = true;
                        etHeight.setVisibility(View.INVISIBLE);
                        etHeightFt.setVisibility(View.VISIBLE);
                        etHeightInch.setVisibility(View.VISIBLE);
                        break;
                    default:
                        ft = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter<CharSequence> weightAdapter = ArrayAdapter.createFromResource(this.getContext(), R.array.weight_array, android.R.layout.simple_spinner_item);
        weightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weightSpinner.setAdapter(weightAdapter);
        weightSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        lb = 1;
                        break;
                    case 1:
                        lb = 0.45;
                        break;
                    default:
                        lb = 1;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter<CharSequence> activityAdapter = ArrayAdapter.createFromResource(this.getContext(), R.array.activity_array, android.R.layout.simple_spinner_item);
        activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activitySpinner.setAdapter(activityAdapter);
        activitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                            workoutRatio=1.2;
                        break;
                    case 1:
                            workoutRatio = 1.375;
                        break;
                    case 2:
                            workoutRatio =1.55;
                        break;
                    case 3:
                            workoutRatio =1.725;
                        break;
                    case 4:
                            workoutRatio = 1.9;
                        break;
                    default:
                        calculateBmr();
                        bmr = calculateBmr()*1.2;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnCalculate.setOnClickListener(v -> {
            try {
                bmr = workoutRatio*calculateBmr();
            tvMaintain.setText(df.format(bmr)+" cal/day");
            tvMildLoss.setText(df.format(bmr*0.9)+" cal/day");
            tvLoss.setText(df.format(bmr*0.79)+" cal/day");
            tvExtremeLoss.setText(df.format(bmr*0.59)+" cal/day");
            }
            catch (Exception e){}
        });

    }

    public double calculateBmr(){
        double height, weight, age, bmr=1;
        weight = Double.parseDouble(etWeight.getText().toString())*lb;
        age = Double.parseDouble(etAge.getText().toString());
        if(!ft){
            height = Double.parseDouble(etHeight.getText().toString());
            if(!genderSelector.isChecked()) {
                bmr = 10*weight + 6.25*height - 5*age + 5;
            }
            else {
                bmr = 10*weight + 6.25*height - 5*age - 161;
            }
        }
        else {
            height = (Double.parseDouble(etHeightFt.getText().toString())*30.5)+
                    (Double.parseDouble(etHeightInch.getText().toString())*2.54);
            if(!genderSelector.isChecked()) {
                bmr = 10*weight + 6.25*height - 5*age + 5;
            }
            else {
                bmr = 10*weight + 6.25*height - 5*age - 161;
            }
        }

        return bmr;
    }
}