package com.example.fitnessdoctor;

import android.graphics.Color;
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

import com.ekn.gruzer.gaugelibrary.HalfGauge;
import com.ekn.gruzer.gaugelibrary.Range;

import java.text.DecimalFormat;


public class BMIcalculator extends Fragment {

    EditText etAge, etHeight, etWeight, etHeightFt, etHeightInch;
    Spinner heightSpinner, weightSpinner;
    ToggleButton genderSelector;
    HalfGauge halfGauge;
    TextView normalWeight, category;
    NavController navController;
    double lb;
    boolean ft;

    private static DecimalFormat df = new DecimalFormat("0.0");

    public BMIcalculator() {
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
        return inflater.inflate(R.layout.fragment_b_m_icalculator, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        heightSpinner = view.findViewById(R.id.height_spinner);
        weightSpinner = view.findViewById(R.id.weight_spinner);
        halfGauge = view.findViewById(R.id.progress_circular);
        etHeight = view.findViewById(R.id.et_height);
        etWeight = view.findViewById(R.id.et_weight);
        etHeightFt = view.findViewById(R.id.et_height_ft);
        etHeightInch = view.findViewById(R.id.et_height_inch);
        normalWeight = view.findViewById(R.id.tv_weight_range);
        category = view.findViewById(R.id.tv_category);
        navController = Navigation.findNavController(view);

        etHeight.addTextChangedListener(textWatcher);
        etWeight.addTextChangedListener(textWatcher);
        etHeightFt.addTextChangedListener(textWatcher);
        etHeightInch.addTextChangedListener(textWatcher);

        halfGauge.setMinValue(15);
        halfGauge.setMaxValue(40);

        Range range1 = makeRange(15, 18.5, "#87b1d9");
        Range range2 = makeRange(18.5, 25, "#3dd365");
        Range range3 = makeRange(25, 30, "#ede232");
        Range range4 = makeRange(30, 35, "#fd802e");
        Range range5 = makeRange(35, 40, "#f95353");

        halfGauge.addRange(range1);
        halfGauge.addRange(range2);
        halfGauge.addRange(range3);
        halfGauge.addRange(range4);
        halfGauge.addRange(range5);

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

    }

    public double calculateBmi(){

        double height, weight, bmi;
        if(!ft){
            String s1 = etHeight.getText().toString();
            String s2 = etWeight.getText().toString();

            height = Double.parseDouble(s1)/100;
            weight = Double.parseDouble(s2)*lb;
            bmi = (weight / ((height)*(height)));
        }

        else {
            String s1 = etHeightFt.getText().toString();
            String s3 = etHeightInch.getText().toString();
            String s2 = etWeight.getText().toString();

            height = (Double.parseDouble(s1)*30.5/100)+(Double.parseDouble(s3)*2.54/100);
            weight = Double.parseDouble(s2)*lb;
            bmi = (weight / ((height)*(height)));
        }
        halfGauge.setValue(Double.parseDouble(df.format(bmi)));
        return bmi;
    }

    public void calculateNormalWeight(){
        if(!ft){
            String s1 = etHeight.getText().toString();
            double height = Double.parseDouble(s1)/100;

            double w1 = height*height*18.5;
            double w2 = height*height*25;

            String weightFrom = df.format(w1);
            String weightTo = df.format(w2);

            normalWeight.setText(weightFrom+" - "+weightTo+" kg");
        }
        else {
            String s1 = etHeightFt.getText().toString();
            String s2 = etHeightInch.getText().toString();
            double height = (Double.parseDouble(s1)*30.5/100)+(Double.parseDouble(s2)*2.54/100);

            double w1 = height*height*18.5;
            double w2 = height*height*25;

            String weightFrom = df.format(w1);
            String weightTo = df.format(w2);

            normalWeight.setText(weightFrom+" - "+weightTo+" kg");
        }
    }

    public Range makeRange(double from, double to, String color ){
        Range range = new Range();
        range.setColor(Color.parseColor(color));
        range.setFrom(from);
        range.setTo(to);
        return range;
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try {
                calculateBmi();
                calculateNormalWeight();
                setTvCategory(calculateBmi());
            }
            catch (NumberFormatException e){
                e.printStackTrace();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public void setTvCategory(double bmi) {
        if (bmi < 18.5) {
            category.setText("UNDERWEIGHT");
            category.setTextColor(Color.parseColor("#87b1d9"));
        } else if (bmi < 25 && bmi >= 18.5) {
            category.setText("NORMAL");
            category.setTextColor(Color.parseColor("#3dd365"));
        } else if (bmi < 30 && bmi >= 25) {
            category.setText("OVERWEIGHT");
            category.setTextColor(Color.parseColor("#ede232"));
        } else if (bmi < 35 && bmi >= 30) {
            category.setText("OBESE");
            category.setTextColor(Color.parseColor("#fd802e"));
        } else {
            category.setText("EXTREME OBESE");
            category.setTextColor(Color.parseColor("#f95353"));
        }
    }
}