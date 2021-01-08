package com.example.fitnessdoctor;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.text.DecimalFormat;

public class IdealWeightCalculator extends Fragment {

    NavController navController;
    EditText etAge, etHeight, etHeightFt, etHeightInch;
    TextView tvIdealWeight;
    Spinner heightSpinner;
    ToggleButton genderSelector;
    Double idealWeight;
    double lb;
    boolean ft;
    private static DecimalFormat df = new DecimalFormat("0.0");



    public IdealWeightCalculator() {
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
        return inflater.inflate(R.layout.fragment_ideal_weight_calculator, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        heightSpinner = view.findViewById(R.id.height_spinner);
        etHeight = view.findViewById(R.id.et_height);
        etAge = view.findViewById(R.id.et_age);
        etHeightFt = view.findViewById(R.id.et_height_ft);
        etHeightInch = view.findViewById(R.id.et_height_inch);
        tvIdealWeight = view.findViewById(R.id.tv_ideal_weight);
        genderSelector = view.findViewById(R.id.gender_toggle);
        navController = Navigation.findNavController(view);

        etHeight.addTextChangedListener(textWatcher);
        genderSelector.addTextChangedListener(textWatcher);
        etHeightFt.addTextChangedListener(textWatcher);
        etHeightInch.addTextChangedListener(textWatcher);

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


    }

    public double calculateIdealWeight(){
        double height;
        if(!ft){
            height = Double.parseDouble(etHeight.getText().toString());
            if(!genderSelector.isChecked()) {
                idealWeight = 	56.2+ (height-152.4)*0.555;
            }
            else {
                idealWeight = 	53.1+ (height-152.4)*0.535;
            }
            tvIdealWeight.setText("Your Ideal Weight should be: "+df.format(idealWeight)+" kg");
        }

        else {
            height = (Double.parseDouble(etHeightFt.getText().toString())*30.5)+
                    (Double.parseDouble(etHeightInch.getText().toString())*2.54);
            if(!genderSelector.isChecked()) {
                idealWeight = 	56.2+ (height-152.4)*0.555;
            }
            else {
                idealWeight = 	53.1+ (height-152.4)*0.535;
            }
            tvIdealWeight.setText("Your Ideal Weight should be: "+df.format(idealWeight)+" kg");
        }
        return idealWeight;
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try {
                calculateIdealWeight();
            }
            catch (NumberFormatException e){}
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

}