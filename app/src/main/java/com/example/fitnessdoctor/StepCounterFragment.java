package com.example.fitnessdoctor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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
import android.widget.Toast;

import com.ekn.gruzer.gaugelibrary.ArcGauge;

import java.text.DecimalFormat;
import java.util.Set;


public class StepCounterFragment extends Fragment implements SensorEventListener {
    EditText etMeters;
    Button btnStart;
    ArcGauge arcGauge;
    NavController navController;
    SensorManager sensorManager;
    boolean running = false;
    double numberOfSteps = 0;
    double previousNumberOfSteps = 0;
    double meters=0;

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
        sensorManager = (SensorManager) this.getActivity().getSystemService(Context.SENSOR_SERVICE);

        btnStart.setOnClickListener(v -> {
            try {
                meters = Double.parseDouble(etMeters.getText().toString());
            }
            catch (NumberFormatException e){e.printStackTrace();}
            numberOfSteps = meters*1.31;
            arcGauge.setMaxValue((int)numberOfSteps);
            arcGauge.setValue(0);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        running = true;
        Sensor counterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (counterSensor == null) {
            Toast.makeText(getContext(), "No sensor was detected!", Toast.LENGTH_SHORT).show();
        }
        else {
            sensorManager.registerListener(this, counterSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        running = false;
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (running){
           // int currentSteps = (int)(numberOfSteps - previousNumberOfSteps);
            arcGauge.setValue(event.values[0]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}